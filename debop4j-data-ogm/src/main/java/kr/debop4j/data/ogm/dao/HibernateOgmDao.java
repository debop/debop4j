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

package kr.debop4j.data.ogm.dao;

import com.google.common.collect.Lists;
import kr.debop4j.core.Local;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.SimplePagedList;
import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
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
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * hibernate-ogm 용 DAO 입니다.<br />
 * hibernate의 Criteria 기능을 제공할 수 없어, Criteria 부분은 hibernate-search를 사용합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 5:42
 */
@Component
@SuppressWarnings("unchecked")
public class HibernateOgmDao implements IHibernateOgmDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateOgmDao.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private static final String SESSION_KEY = HibernateOgmDao.class.getName() + ".Session";
    private static final String FULL_TEXT_SESSION_KEY = HibernateOgmDao.class.getName() + ".FullTextSession";

    private static final int BATCH_SIZE = 100;

    private final SessionFactory sessionFactory;

    public HibernateOgmDao() {
        this(UnitOfWorks.getCurrentSessionFactory());
    }

    public HibernateOgmDao(SessionFactory sessionFactory) {
        assert sessionFactory != null;
        this.sessionFactory = sessionFactory;
    }

    public HibernateOgmDao(Session session) {
        this.sessionFactory = session.getSessionFactory();
        Local.put(SESSION_KEY, session);
    }

    /**
     * hibernate session을 반환합니다.
     */
    @Override
    public synchronized final Session getSession() {
        Session session = (Session) Local.get(SESSION_KEY);
        if (session == null || !session.isOpen()) {
            if (log.isDebugEnabled())
                log.debug("현 ThreadContext에 새로운 Session을 엽니다...");

            session = sessionFactory.openSession();
            Local.put(SESSION_KEY, session);
        }
        return session;
    }

    /**
     * hibernate-search의 {@link FullTextSession} 을 반환합니다.
     */
    @Override
    public final synchronized FullTextSession getFullTextSession() {
        FullTextSession fts = (FullTextSession) Local.get(FULL_TEXT_SESSION_KEY);

        if (fts == null || !fts.isOpen()) {
            if (isDebugEnabled)
                log.debug("현 ThreadContext에 새로운 FullTextSession을 생성합니다...");

            fts = Search.getFullTextSession(getSession());
            Local.put(FULL_TEXT_SESSION_KEY, fts);
        }
        return fts;
    }

    /**
     * 지정한 형식에 대한 질의 빌더를 생성합니다.
     */
    @Override
    public final QueryBuilder getQueryBuilder(Class<?> clazz) {
        return getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
    }

    @Override
    public FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entities) {
        if (isTraceEnabled)
            log.trace("FullTextQuery를 생성합니다... luceneQuery=[{}], entities=[{}]",
                      luceneQuery, StringTool.listToString(entities));

        FullTextQuery ftq = getFullTextSession().createFullTextQuery(luceneQuery, entities);
        // 필수!!! object lookup 및 DB 조회 방법 설정
        //
        ftq.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.FIND_BY_ID);
        return ftq;
    }

    @Override
    public <T> T get(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    /**
     * 엔티티 수형에 해당하는 모든 엔티티를 조회합니다.
     */
    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        return findAll(clazz, null);
    }

    /**
     * 엔티티 수형에 해당하는 모든 엔티티를 조회합니다.
     */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Sort luceneSort) {
        if (isTraceEnabled)
            log.trace("엔티티 수형 [{}]의 모든 레코드를 조회합니다...", clazz);

        Query luceneQuery = getQueryBuilder(clazz).all().createQuery();
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        if (luceneSort != null) ftq.setSort(luceneSort);

        return (List<T>) ftq.list();
    }

    /**
     * 조회
     */
    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery) {
        return find(clazz, luceneQuery, -1, -1, null, null);
    }

    /**
     * 조회
     */
    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort sort) {
        return find(clazz, luceneQuery, -1, -1, sort, null);
    }

    /**
     * 조회
     */
    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort) {
        return find(clazz, luceneQuery, firstResult, maxResults, sort, null);
    }

    /**
     * 조회
     */
    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria) {
        if (log.isTraceEnabled())
            log.trace("엔티티 조회. clazz=[{}], luceneQuery=[{}], fitstResult=[{}], maxResults=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, firstResult, maxResults, sort, criteria);

        FullTextQuery ftq = this.getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, firstResult, maxResults);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);

        return ftq.list();
    }

    /**
     * 페이징 조회
     */
    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, sort, null);
    }

    /**
     * 페이징 조회
     */
    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria) {
        if (log.isTraceEnabled())
            log.trace("엔티티 페이징 조회. clazz=[{}], luceneQuery=[{}], pageNo=[{}], pageSize=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, pageNo, pageSize, sort, criteria);

        long totalCount = count(clazz, luceneQuery);
        FullTextQuery ftq = this.getFullTextQuery(luceneQuery, clazz);

        HibernateTool.setPaging(ftq, (pageNo - 1) * pageSize, pageSize);

        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria); // fetching strategy 같은 겻을 제공할 수 있다.

        return new SimplePagedList<T>(ftq.list(), pageNo, pageSize, totalCount);
    }

    /**
     * 엔티티의 Id를 페이징 조회
     */
    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria) {
        IPagedList<Object[]> list = getProjectionPage(clazz, luceneQuery, new String[]{ FullTextQuery.ID }, pageNo, pageSize, sort, criteria);
        List<Serializable> ids = Lists.newArrayList();
        for (Object[] fields : list.getList()) {
            ids.add((Serializable) fields[0]);
        }
        return new SimplePagedList(ids, pageNo, pageSize, list.getItemCount());
    }

    /**
     * 루씬 부가 정보를 페이징 조회합니다. (FullTextQuery.ID, FullTextQuery.DOCUMENT_ID, FullTextQuery.SCORE 등)
     */
    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort sort, Criteria criteria) {
        if (isTraceEnabled)
            log.trace("엔티티 Projection 조회. clazz=[{}], luceneQuery=[{}], fields=[{}], pageNo=[{}], pageSize=[{}], sort=[{}]",
                      clazz, luceneQuery, StringTool.listToString(fields), pageNo, pageSize, sort);

        FullTextQuery ftq = this.getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, (pageNo - 1) * pageSize, pageSize);
        ftq.setProjection(fields);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);

        List<Object[]> list = ftq.list();
        return new SimplePagedList<Object[]>(list, pageNo, pageSize, count(clazz, luceneQuery));
    }

    @Override
    public long count(Class<?> clazz) {
        throw new UnsupportedOperationException("HashMap에서는 지원하지 않습니다.");
    }

    @Override
    public long count(Class<?> clazz, Query luceneQuery) {
        throw new UnsupportedOperationException("HashMap에서는 지원하지 않습니다.");
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

    @Override
    public void deleteById(Class<?> clazz, Serializable id) {
        Object entity = getFullTextSession().load(clazz, id);
        delete(entity);
    }

    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(findAll(clazz));
    }

    @Override
    public <T> void deleteAll(Collection<T> entities) {
        if (ArrayTool.isEmpty(entities)) return;
        if (isTraceEnabled)
            log.trace("엔티티 컬렉션을 모두 삭제합니다... entity count=[{}]", entities.size());

        Session session = getFullTextSession();
        for (T entity : entities) {
            session.delete(entity);
        }
    }

    /**
     * 해당 엔티티의 인덱스 정보를 제거합니다.
     */
    @Override
    public <T> void purge(Class<T> clazz, Serializable id) {
        if (isTraceEnabled)
            log.trace("인덱스를 제거합니다. clazz=[{}], id=[{}]", clazz, id);
        getFullTextSession().purge(clazz, id);
    }

    /**
     * 지정된 수형의 모든 엔티티들의 인덱스 정보를 제거합니다.
     */
    @Override
    public <T> void purgeAll(Class<T> clazz) {
        if (isDebugEnabled)
            log.debug("지정된 수형의 모든 엔티티들의 인덱스를 제거합니다. clazz=[{}]", clazz);
        getFullTextSession().purgeAll(clazz);
    }

    /**
     * Session에 남아있는 인덱싱 작업을 강제로 수행하도록 합니다.
     */
    @Override
    public void flushToIndexes() {
        getFullTextSession().flushToIndexes();
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

    /**
     * 지정된 수형의 모든 엔티티들을 인덱싱 합니다.
     */
    @Override
    public void indexAll(Class<?> clazz, int batchSize) {
        if (log.isDebugEnabled())
            log.debug("수형[{}]의 모든 엔티티에 대해 인덱싱을 수행합니다...", clazz);

        clearIndex(clazz);

        if (batchSize < BATCH_SIZE)
            batchSize = BATCH_SIZE;

        FullTextSession fts = getFullTextSession();

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

    /**
     * 해당 수형의 모든 인덱스를 비동기 방식으로 구성합니다.
     */
    @Override
    public Future<Void> indexAllAsync(final Class<?> clazz, final int batchSize) {
        if (log.isDebugEnabled())
            log.debug("비동기 방식으로 엔티티에 대해 인덱싱을 수행합니다... clazz=[{}], batchSize=[{}]", clazz, batchSize);

        return AsyncTool.startNew(new Callable<Void>() {
            @Override
            public Void call() {
                final FullTextSession fts = getFullTextSession();
                try {
                    indexAll(clazz, batchSize);
                    return null;
                } finally {
                    fts.close();
                }
            }
        });
    }

    /**
     * 해당 수형의 모든 인덱스 정보를 삭제합니다.
     */
    @Override
    public void clearIndex(Class<?> clazz) {
        if (isDebugEnabled)
            log.debug("엔티티에 대한 모든 인덱스 정보를 삭제합니다... clazz=[{}]", clazz);

        FullTextSession fts = getFullTextSession();
        fts.purgeAll(clazz);                        // remove obsolete index
        fts.flushToIndexes();                       // apply purge before optimize
        fts.getSearchFactory().optimize(clazz);     // physically clear space
    }

    /**
     * 모든 인덱스를 삭제합니다.
     */
    @Override
    public void clearIndexAll() {
        if (isDebugEnabled)
            log.debug("모든 엔티티에 대해 모든 인덱스 정보를 삭제합니다...");

        FullTextSession fts = getFullTextSession();
        for (Class clazz : SearchTool.getIndexedClasses(getSession().getSessionFactory())) {
            fts.purgeAll(clazz);
            fts.flushToIndexes();
        }
        optimizeAll();
    }

    /**
     * 해당 수형의 인덱스를 최적화합니다.
     */
    @Override
    public void optimize(Class<?> clazz) {
        if (isTraceEnabled)
            log.trace("지정된 수형의 인덱스를 최적화합니다. clazz=[{}]", clazz);
        getFullTextSession().getSearchFactory().optimize(clazz);
    }

    /**
     * 모든 엔티티의 인덱스를 최적화합니다.
     */
    @Override
    public void optimizeAll() {
        if (isTraceEnabled)
            log.trace("모든 수형의 인덱스를 최적화합니다.");
        getFullTextSession().getSearchFactory().optimize();
    }
}
