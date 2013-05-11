/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.search.dao;

import com.google.common.collect.Lists;
import kr.debop4j.core.Local;
import kr.debop4j.core.collection.PaginatedList;
import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWorkFactory;
import kr.debop4j.search.tools.SearchTool;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.*;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * hibernate-ogm을 기반으로 하는 Data Access Object 의 구현체입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 4. 오후 7:07
 */
@Repository
@SuppressWarnings("unchecked")
public class SearchDaoImpl implements SearchDao {

    private static final Logger log = LoggerFactory.getLogger(SearchDaoImpl.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();


    private static final int BATCH_SIZE = 100;

    @Autowired
    private final SessionFactory sessionFactory;

    /** 현 Thread-context에서 사용할 {@link org.hibernate.Session} 의 저장소 키 (참고: {@link kr.debop4j.core.Local}) */
    public String SESSION_KEY = IUnitOfWorkFactory.CURRENT_HIBERNATE_SESSION;

    @Autowired
    public SearchDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /** 현 Thread-context 에서 사용할 Session 를 빈환합니다. */
    @Override
    public synchronized final Session getSession() {
        Session session = Local.get(SearchDao.SESSION_KEY, Session.class);

        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            Local.put(SearchDao.SESSION_KEY, session);
            log.debug("새로운 Session을 생성했습니다.");
        }
        return session;
    }

    /** 현 Thread-context 에서 사용할 hibernate-search 의 {@link  org.hibernate.search.FullTextSession} 을 반환합니다. */
    @Override
    public synchronized final FullTextSession getFullTextSession() {
        FullTextSession fts = Local.get(SearchDaoImpl.FULL_TEXT_SESSION_KEY, FullTextSession.class);

        if (fts == null || !fts.isOpen()) {
            fts = Search.getFullTextSession(getSession());
            Local.put(SearchDaoImpl.FULL_TEXT_SESSION_KEY, fts);
            log.debug("새로운 FullTextSession을 생성했습니다.");
        }
        return fts;
    }

    /** lucene을 이용한 Full text search를 위한 {@link org.hibernate.search.FullTextQuery}를 반환합니다. */
    @Override
    public FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entities) {

        FullTextQuery ftq = getFullTextSession().createFullTextQuery(luceneQuery, entities);

        /* hibernate-ogm 에서는 꼭 DatabaseRetrievalMethod.FIND_BY_ID 를 사용해야 합니다. */
        ftq.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.FIND_BY_ID);
        return ftq;
    }

    /** {@link org.apache.lucene.search.Query}를 빌드해주는 {@link org.hibernate.search.query.dsl.QueryBuilder}를 반환합니다. */
    @Override
    public QueryBuilder getQueryBuilder(Class<?> clazz) {
        return getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
    }

    /** 지정한 Id 값을 가지는 엔티티를 로드합니다. 없으면 null 을 반환합니다. */
    @Override
    public <T> T get(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    /** 지정한 수형의 모든 엔티티를 조회합니다. */
    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        return findAll(clazz, null);
    }

    /** 지정한 수형의 모든 엔티티를 조회합니다. */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Sort luceneSort) {
        if (isTraceEnabled)
            log.trace("엔티티[{}]의 모든 레코드를 조회합니다...", clazz);

        Query luceneQuery = getQueryBuilder(clazz).all().createQuery();
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        if (luceneSort != null)
            ftq.setSort(luceneSort);

        return ftq.list();
    }

    /** 지정한 엔티티를 조회합니다. */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Query luceneQuery, Sort sort, Criteria criteria) {
        return findAll(clazz, luceneQuery, -1, -1, sort, criteria);
    }

    /** 지정한 엔티티를 조회합니다. */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort) {
        return findAll(clazz, luceneQuery, firstResult, maxResults, sort, null);
    }

    /** 지정한 엔티티를 조회합니다. */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria) {
        if (isTraceEnabled)
            log.trace("엔티티 조회. clazz=[{}], luceneQuery=[{}], fitstResult=[{}], maxResults=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, firstResult, maxResults, sort, criteria);

        if (luceneQuery == null)
            luceneQuery = getQueryBuilder(clazz).all().createQuery();

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, firstResult, maxResults);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);

        return ftq.list();
    }

    /** Page 단위로 엔티티를 조회합니다. */
    @Override
    public <T> PaginatedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, null, null);
    }

    /** Page 단위로 엔티티를 조회합니다. */
    @Override
    public <T> PaginatedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, sort, null);
    }

    /** Page 단위로 엔티티를 조회합니다. */
    @Override
    public <T> PaginatedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria) {
        if (isDebugEnabled)
            log.debug("엔티티 조회. clazz=[{}], luceneQuery=[{}], pageNo=[{}], pageSize=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, pageNo, pageSize, sort, criteria);

        if (luceneQuery == null)
            luceneQuery = getQueryBuilder(clazz).all().createQuery();

        int itemCount = count(clazz, luceneQuery);

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, (pageNo - 1) * pageSize, pageSize);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);

        List<T> list = ftq.list();
        return new PaginatedList<>(list, pageNo, pageSize, itemCount);
    }

    /** 특정 엔티티에 대해 Id 만 조회한다. */
    @Override
    public List<Serializable> getAllIds(Class<?> clazz, Query luceneQuery) {
        return getAllIds(clazz, luceneQuery, -1, -1, null, null);
    }

    /** 특정 엔티티에 대해 Id 만 조회한다. */
    @Override
    public List<Serializable> getAllIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort) {
        return getAllIds(clazz, luceneQuery, firstResult, maxResults, sort, null);
    }

    /** 엔티티에 대해 Id 만 조회한다. */
    @Override
    public List<Serializable> getAllIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria) {
        List<Object[]> list = getProjections(clazz, luceneQuery, new String[] { FullTextQuery.ID }, firstResult, maxResults, sort, criteria);
        List<Serializable> ids = Lists.newArrayList();
        for (Object[] fields : list) {
            ids.add((Serializable) fields[0]);
        }
        return ids;
    }

    /** 페이지 단위로 엔티티에 대해 Id 만 조회한다. */
    @Override
    public PaginatedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize) {
        return getIdPage(clazz, luceneQuery, pageNo, pageSize, null, null);
    }

    /** 페이지 단위로 엔티티에 대해 Id 만 조회한다. */
    @Override
    public PaginatedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort) {
        return getIdPage(clazz, luceneQuery, pageNo, pageSize, sort, null);
    }

    /** 페이지 단위로 엔티티에 대해 Id 만 조회한다. */
    @Override
    public PaginatedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria) {

        int totalCount = count(clazz, luceneQuery);
        List<Object[]> list = getProjections(clazz, luceneQuery, new String[] { FullTextQuery.ID },
                                             (pageNo - 1) * pageSize, pageSize, sort, criteria);
        List<Serializable> ids = Lists.newArrayList();
        for (Object[] fields : list) {
            ids.add((Serializable) fields[0]);
        }
        return new PaginatedList<>(ids, pageNo, pageSize, totalCount);
    }

    /** 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다. */
    @Override
    public List<Object[]> getProjections(Class<?> clazz, Query luceneQuery, String[] fields) {
        return getProjections(clazz, luceneQuery, fields, -1, -1, null, null);
    }

    /** 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다. */
    @Override
    public List<Object[]> getProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort sort) {
        return getProjections(clazz, luceneQuery, fields, -1, -1, sort, null);
    }

    /** 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다. */
    @Override
    public List<Object[]> getProjections(Class<?> clazz, Query luceneQuery, String[] fields,
                                         int firstResult, int maxResults, Sort sort, Criteria criteria) {
        assert fields != null;
        assert fields.length > 0;

        if (luceneQuery == null)
            luceneQuery = getQueryBuilder(clazz).all().createQuery();

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, firstResult, maxResults);
        ftq.setProjection(fields);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);

        return ftq.list();
    }

    /** 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다. */
    @Override
    public PaginatedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                                     int pageNo, int pageSize) {
        return getProjectionPage(clazz, luceneQuery, fields, pageNo, pageSize, null, null);
    }

    /** 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다. */
    @Override
    public PaginatedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                                     int pageNo, int pageSize, Sort sort) {
        return getProjectionPage(clazz, luceneQuery, fields, pageNo, pageSize, sort, null);
    }

    /** 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다. */
    @Override
    public PaginatedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                                     int pageNo, int pageSize, Sort sort, Criteria criteria) {
        int totalCount = count(clazz, luceneQuery, criteria);
        List<Object[]> list = getProjections(clazz, luceneQuery, fields, (pageNo - 1) * pageSize, pageSize, sort, criteria);
        return new PaginatedList<>(list, pageNo, pageSize, totalCount);
    }

    /** 해당 수형의 엔티티의 모든 레코드 수를 가져온다. */
    @Override
    public int count(Class<?> clazz) {
        return count(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    /** 특정 엔티티에 조건에 맞는 레코드 수를 가져온다. */
    @Override
    public int count(Class<?> clazz, Query luceneQuery) {
        return count(clazz, luceneQuery, null);
    }

    /** 특정 엔티티에 조건에 맞는 레코드 수를 가져온다. */
    @Override
    public int count(Class<?> clazz, Query luceneQuery, Criteria criteria) {
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        if (criteria != null) ftq.setCriteriaQuery(criteria);
        int count = ftq.getResultSize();

        if (isTraceEnabled)
            log.trace("Entity=[{}], query=[{}] => count=[{}]", clazz, luceneQuery, count);

        return count;
    }

    @Override
    public void persist(Object entity) {
        getFullTextSession().persist(entity);
    }

    @Override
    public Object merge(Object entity) {
        return getFullTextSession().merge(entity);
    }

    @Override
    public Serializable save(Object entity) {
        return getFullTextSession().save(entity);
    }

    @Override
    public void saveOrUpdate(Object entity) {
        getFullTextSession().saveOrUpdate(entity);
    }

    @Override
    public void update(Object entity) {
        getFullTextSession().update(entity);
    }

    @Override
    public void delete(Object entity) {
        getFullTextSession().delete(entity);
    }

    /** 지정한 Id 값을 가진 엔티티를 삭제합니다. */
    @Override
    public void deleteById(Class<?> clazz, Serializable id) {
        try {
            Object entity = getSession().load(clazz, id);
            delete(entity);
        } catch (Exception t) {
            log.warn("엔티티 삭제에 실패했습니다. 엔티티가 없을 수 있습니다. clazz=[{}], id=[{}]", clazz, id);
        }
    }

    @Override
    public void deleteByIds(Class<?> clazz, Collection<? extends Serializable> ids) {
        if (isTraceEnabled)
            log.trace("Ids에 해당하는 엔티티들을 삭제합니다. class=[{}], ids=[{}]", clazz, StringTool.listToString(ids));

        Session session = getSession();
        List<Object> entities = new ArrayList<>();

        for (Serializable id : ids)
            entities.add(session.load(clazz, id));
        deleteAll(entities);
    }

    /** 해당 수형의 모든 엔티티를 삭제합니다. */
    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    /** 쿼리 결과에 해당하는 엔티티들을 모두 삭제합니다. */
    @Override
    public void deleteAll(Class<?> clazz, Query luceneQuery) {
        List<Serializable> ids = getAllIds(clazz, luceneQuery);
        deleteByIds(clazz, ids);
    }

    /** 엔티티 컬렉션의 모든 엔티티를 삭제합니다. * */
    @Override
    public void deleteAll(Collection<?> entities) {
        if (ArrayTool.isEmpty(entities)) return;
        if (isTraceEnabled)
            log.trace("엔티티 컬렉션을 모두 삭제합니다... entity count=[{}]", entities.size());

        Session session = getFullTextSession();

        for (Object entity : entities) {
            session.delete(entity);
        }
    }

    /** 지정한 Id를 가진 엔티티의 인덱스 정보를 삭제합니다. */
    @Override
    public void purge(Class<?> clazz, Serializable id) {
        getFullTextSession().purge(clazz, id);
    }

    /** 지정한 수형의 인덱스 정보를 삭제합니다. */
    @Override
    public void purgeAll(Class<?> clazz) {
        getFullTextSession().purgeAll(clazz);
    }

    /**
     * 엔티티를 수동으로 재 인덱싱합니다.<br/>
     * Force the (re)indexing of a given <b>managed</b> object.
     */
    @Override
    public <T> void index(T entity) {
        assert entity != null;
        if (isTraceEnabled)
            log.trace("수동으로 재 인덱스를 수행합니다. entity=[{}]", entity);
        getFullTextSession().index(entity);
    }

    /** 지정된 수형의 모든 엔티티들을 인덱싱 합니다. */
    @Override
    public void indexAll(Class<?> clazz, int batchSize) {
        if (log.isDebugEnabled())
            log.debug("수형[{}]의 모든 엔티티에 대해 인덱싱을 수행합니다...", clazz);

        clearIndex(clazz);

        if (batchSize < BATCH_SIZE)
            batchSize = BATCH_SIZE;

        final FullTextSession fts = getFullTextSession();

        FlushMode currentFlushMode = fts.getFlushMode();
        CacheMode currentCacheMode = fts.getCacheMode();
        fts.setFlushMode(FlushMode.MANUAL);
        fts.setCacheMode(CacheMode.IGNORE);

        try {
            Transaction tx = fts.beginTransaction();
            ScrollableResults results = fts.createCriteria(clazz).scroll(ScrollMode.FORWARD_ONLY);
            int index = 0;
            while (results.next()) {
                index++;
                fts.index(results.get(0));
                if (index % batchSize == 0) {
                    fts.flushToIndexes();
                    fts.clear();
                }
            }
            fts.flushToIndexes();
            tx.commit();

            if (log.isDebugEnabled())
                log.debug("수형[{}]의 모든 엔티티 [{}]개 대해 재 인덱싱을 수행했습니다!!!", clazz, index);
        } finally {
            fts.setFlushMode(currentFlushMode);
            fts.setCacheMode(currentCacheMode);
        }
    }

    /** 해당 수형의 모든 인덱스를 비동기 방식으로 구성합니다. */
    @Override
    public Future<Void> indexAllAsync(final Class<?> clazz, final int batchSize) {
        if (isTraceEnabled)
            log.trace("비동기 방식으로 엔티티에 대해 인덱싱을 수행합니다... clazz=[{}], batchSize=[{}]", clazz, batchSize);

        final FullTextSession fts = getFullTextSession();
        return AsyncTool.startNew(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    indexAll(clazz, batchSize);
                    return null;
                } finally {
                    fts.close();
                }
            }
        });
    }

    /** 해당 수형의 모든 인덱스 정보를 삭제합니다. */
    @Override
    public void clearIndex(Class<?> clazz) {
        if (isDebugEnabled)
            log.debug("엔티티에 대한 모든 인덱스 정보를 삭제합니다... clazz=[{}]", clazz);

        getFullTextSession().purgeAll(clazz);       // remove obsolete index
        getFullTextSession().flushToIndexes();      // apply purge before optimize
        optimize(clazz);                            // physically clear space
    }

    /** 모든 인덱스를 삭제합니다. */
    @Override
    public void clearIndexAll() {
        if (isDebugEnabled) log.debug("모든 엔티티에 대해 모든 인덱스 정보를 삭제합니다...");

        FullTextSession fts = getFullTextSession();
        for (Class clazz : SearchTool.getIndexedClasses(getSession().getSessionFactory())) {
            fts.purgeAll(clazz);
            fts.flushToIndexes();
        }
        optimizeAll();
    }

    /** 해당 수형의 인덱스를 최적화합니다. */
    @Override
    public void optimize(Class<?> clazz) {
        if (isTraceEnabled) log.trace("지정된 수형의 인덱스를 최적화합니다. clazz=[{}]", clazz);
        getFullTextSession().getSearchFactory().optimize(clazz);
    }

    /** 모든 엔티티의 인덱스를 최적화합니다. */
    @Override
    public void optimizeAll() {
        if (isTraceEnabled) log.trace("모든 수형의 인덱스를 최적화합니다.");
        getFullTextSession().getSearchFactory().optimize();
    }

    /** 세션의 모든 변경을 저장소에 적용한다. */
    @Override
    public void flush() {
        if (isTraceEnabled) log.trace("세션의 모든 변경 정보를 저장소에 적용합니다...");
        getFullTextSession().flush();
    }

    /** Session에 남아있는 인덱싱 작업을 강제로 수행하도록 합니다. */
    @Override
    public void flushToIndexes() {
        if (isTraceEnabled) log.trace("Session에 남아있는 인덱싱 작업을 강제로 수행하도록 하고, 기다립니다.");
        getFullTextSession().flushToIndexes();
    }
}
