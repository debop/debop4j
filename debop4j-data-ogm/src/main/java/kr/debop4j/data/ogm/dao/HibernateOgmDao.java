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
import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.PaginatedList;
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
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * Hibernate-OGM 용 DAO 입니다.<br />
 * hibernate의 Criteria 기능을 제공할 수 없어, Criteria 부분은 hibernate-search를 사용합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 15. 오후 5:42
 */
@Component
@SuppressWarnings( "unchecked" )
public class HibernateOgmDao implements IHibernateOgmDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateOgmDao.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Override
    public synchronized final Session getSession() {
        return UnitOfWorks.getCurrentSession();
    }

    @Override
    public synchronized final FullTextSession getFullTextSession() {
        FullTextSession fts = Local.get(IHibernateOgmDao.FULL_TEXT_SESSION_KEY, FullTextSession.class);
        if (fts == null || !fts.isOpen()) {
            fts = Search.getFullTextSession(getSession());
            Local.put(IHibernateOgmDao.FULL_TEXT_SESSION_KEY, fts);
            log.debug("새로운 FullTextSession을 생성했습니다.");
        }
        return fts;
    }

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
        return (T) getFullTextSession().get(clazz, id);
    }

    @Override
    public <T> List<T> find(Class<T> clazz) {
        return find(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery) {
        return find(clazz, luceneQuery, -1, -1, null, null);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort luceneSort) {
        return find(clazz, luceneQuery, -1, -1, luceneSort, null);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort) {
        return find(clazz, luceneQuery, firstResult, maxResults, luceneSort, null);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort, Criteria criteria) {
        if (isTraceEnabled)
            log.trace("엔티티 조회. clazz=[{}], luceneQuery=[{}], fitstResult=[{}], maxResults=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, firstResult, maxResults, luceneSort, criteria);

        if (luceneQuery == null)
            luceneQuery = getQueryBuilder(clazz).all().createQuery();

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, firstResult, maxResults);

        if (luceneSort != null)
            ftq.setSort(luceneSort);
        if (criteria != null)
            ftq.setCriteriaQuery(criteria);

        return ftq.list();
    }

    @Override
    public List<Serializable> findIds(Class<?> clazz, Query luceneQuery) {
        return findIds(clazz, luceneQuery, -1, -1, null, null);
    }

    @Override
    public List<Serializable> findIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort) {
        return findIds(clazz, luceneQuery, firstResult, maxResults, luceneSort, null);
    }

    @Override
    public List<Serializable> findIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort, Criteria criteria) {

        List<Object[]> fields = findProjections(clazz,
                                                luceneQuery,
                                                new String[] { ProjectionConstants.ID },
                                                firstResult,
                                                maxResults,
                                                luceneSort,
                                                criteria);
        List<Serializable> ids = new ArrayList<>();

        for (Object[] field : fields) {
            ids.add((Serializable) field[0]);
        }
        return ids;
    }

    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields) {
        return findProjections(clazz, luceneQuery, fields, -1, -1, null, null);
    }

    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort luceneSort) {
        return findProjections(clazz, luceneQuery, fields, -1, -1, luceneSort, null);
    }

    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort luceneSort, Criteria criteria) {
        return findProjections(clazz, luceneQuery, fields, -1, -1, luceneSort, criteria);
    }

    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields,
                                          int firstResult, int maxResults, Sort luceneSort, Criteria criteria) {
        shouldNotBeNull(fields, "fields");
        Guard.shouldBe(fields.length > 0, "조회할 필드 수가 있어야합니다.");

        if (isTraceEnabled)
            log.trace("Project 조회. clazz=[{}], luceneQuery=[{}], fields=[{}], firstResult=[{}], maxResults=[{}], luceneSort=[{}], criteria=[{}]",
                      clazz, luceneQuery, StringTool.listToString(fields), firstResult, maxResults, luceneSort, criteria);

        if (luceneQuery == null)
            luceneQuery = getQueryBuilder(clazz).all().createQuery();

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, firstResult, maxResults);
        ftq.setProjection(fields);

        if (luceneSort != null)
            ftq.setSort(luceneSort);
        if (criteria != null)
            ftq.setCriteriaQuery(criteria);

        return ftq.list();
    }


    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, null, null);
    }

    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, luceneSort, null);
    }

    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort, Criteria criteria) {

            log.trace("엔티티 페이징 조회. clazz=[{}], luceneQuery=[{}], pageNo=[{}], pageSize=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, pageNo, pageSize, luceneSort, criteria);

        long totalCount = count(clazz, luceneQuery);
        FullTextQuery ftq = this.getFullTextQuery(luceneQuery, clazz);

        HibernateTool.setPaging(ftq, (pageNo - 1) * pageSize, pageSize);

        if (luceneSort != null)
            ftq.setSort(luceneSort);
        if (criteria != null)
            ftq.setCriteriaQuery(criteria); // fetching strategy 같은 겻을 제공할 수 있다.

        return new PaginatedList<T>(ftq.list(), pageNo, pageSize, totalCount);
    }

    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize) {
        return getIdPage(clazz, luceneQuery, pageNo, pageSize, null, null);
    }

    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort) {
        return getIdPage(clazz, luceneQuery, pageNo, pageSize, luceneSort, null);
    }

    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort, Criteria criteria) {
        IPagedList<Object[]> list = getProjectionPage(clazz, luceneQuery, new String[] { FullTextQuery.ID }, pageNo, pageSize, luceneSort, criteria);
        List<Serializable> ids = Lists.newArrayList();
        for (Object[] fields : list.getList()) {
            ids.add((Serializable) fields[0]);
        }
        return new PaginatedList<>(ids, pageNo, pageSize, list.getItemCount());
    }

    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize) {
        return getProjectionPage(clazz, luceneQuery, fields, pageNo, pageSize, null, null);
    }

    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort luceneSort) {
        return getProjectionPage(clazz, luceneQuery, fields, pageNo, pageSize, luceneSort, null);
    }


    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                                  int pageNo, int pageSize, Sort luceneSort, Criteria criteria) {
        long count = this.count(clazz, luceneQuery);
        List<Object[]> projections = this.findProjections(clazz, luceneQuery, fields,
                                                          (pageNo - 1) * pageSize, pageSize, luceneSort, criteria);

        return new PaginatedList<>(projections, pageNo, pageSize, count);
    }

    @Override
    public long count(Class<?> clazz) {
        return count(clazz, getQueryBuilder(clazz).all().createQuery(), null);
    }

    @Override
    public long count(Class<?> clazz, Query luceneQuery) {
        return count(clazz, luceneQuery, null);
    }

    @Override
    public long count(Class<?> clazz, Query luceneQuery, Criteria criteria) {
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);

        if (criteria != null)
            ftq.setCriteriaQuery(criteria);

        int count = ftq.getResultSize();

        if (isTraceEnabled)
            log.trace("entity counting. entity=[{}], query=[{}], count=[{}]", clazz.getSimpleName(), luceneQuery, count);

        return count;
    }

    @Override
    public boolean exists(Class<?> clazz) {
        return exists(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    @Override
    public boolean exists(Class<?> clazz, Query luceneQuery) {
        List<Serializable> ids = findIds(clazz, luceneQuery, 0, 1, null);
        return (ids != null && ids.size() > 0);
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
        if (entity == null) return;
        getFullTextSession().delete(entity);
    }

    @Override
    public void deleteById(Class<?> clazz, Serializable id) {
        Object entity = getFullTextSession().load(clazz, id);
        delete(entity);
    }

    @Override
    public void deleteByIds(Class<?> clazz, Collection<? extends Serializable> ids) {
        if (isTraceEnabled)
            log.trace("Id 컬렉션에 해당하는 엔티티들을 삭제합니다. clazz=[{}], ids=[{}]", clazz, StringTool.listToString(ids));

        FullTextSession fts = getFullTextSession();
        for (Serializable id : ids)
            delete(fts.load(clazz, id));
    }

    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    @Override
    public void deleteAll(Class<?> clazz, Query luceneQuery) {
        deleteByIds(clazz, findIds(clazz, luceneQuery));
    }

    @Override
    public void deleteAll(Collection<?> entities) {
        if (ArrayTool.isEmpty(entities))
            return;

        if (isDebugEnabled)
            log.debug("엔티티 컬렉션을 모두 삭제합니다... entity count=[{}]", entities.size());

        FullTextSession fts = getFullTextSession();
        for (Object entity : entities) {
            fts.delete(entity);
        }
    }

    @Override
    public void purge(Class<?> clazz, Serializable id) {
        if (isTraceEnabled) log.trace("인덱스를 제거합니다. clazz=[{}], id=[{}]", clazz, id);
        getFullTextSession().purge(clazz, id);
    }

    @Override
    public void purgeAll(Class<?> clazz) {
        log.info("지정된 수형의 모든 엔티티들의 인덱스를 제거합니다. clazz=[{}]", clazz);
        getFullTextSession().purgeAll(clazz);
    }

    @Override
    public void flushToIndexes() {
        getFullTextSession().flushToIndexes();
    }

    @Override
    public <T> void index(T entity) {
        shouldNotBeNull(entity, "entity");
        if (isTraceEnabled) log.trace("수동으로 재 인덱스를 수행합니다. entity=[{}]", entity);

        getFullTextSession().index(entity);
    }

    @Override
    public void indexAll(Class<?> clazz, int batchSize) {
        if (isDebugEnabled)
            log.debug("수형[{}]의 모든 엔티티에 대해 인덱싱을 수행합니다...", clazz);

        clearIndex(clazz);

        if (batchSize < DEFAUALT_BATCH_SIZE)
            batchSize = DEFAUALT_BATCH_SIZE;

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
                fts.index(results.get(0));
                if (++index % batchSize == 0) {
                    fts.flushToIndexes();
                    fts.clear();
                    if (isTraceEnabled) log.trace("인덱싱 수행중입니다. index=[{}]", index);
                }
            }
            fts.flushToIndexes();
            tx.commit();

            log.info("수형[{}]의 모든 엔티티 [{}]개 대해 재 인덱싱을 수행했습니다!!!", clazz, index);
        } finally {
            fts.setFlushMode(currentFlushMode);
            fts.setCacheMode(currentCacheMode);
        }
    }

    @Override
    public Future<Void> indexAllAsync(final Class<?> clazz, final int batchSize) {
        if (isDebugEnabled)
            log.debug("비동기 방식으로 엔티티에 대해 인덱싱을 수행합니다... clazz=[{}], batchSize=[{}]", clazz, batchSize);

        // TODO: Session이 Thread-safe 하지 않으므로, 새로운 Thread를 만들면 안됩니다.
        indexAll(clazz, batchSize);
        return AsyncTool.getTaskHasResult(null);

//        final FullTextSession fts = getFullTextSession();
//        return AsyncTool.startNew(new Callable<Void>() {
//            @Override
//            public Void call() {
//                try {
//                    indexAll(clazz, batchSize);
//                    return null;
//                } finally {
//                    fts.close();
//                }
//            }
//        });
    }

    @Override
    public void clearIndex(Class<?> clazz) {
        if (isDebugEnabled) log.debug("엔티티에 대한 모든 인덱스 정보를 삭제합니다... clazz=[{}]", clazz);

        getFullTextSession().purgeAll(clazz);       // remove obsolete index
        getFullTextSession().flushToIndexes();      // apply purge before optimize
        optimize(clazz);                            // physically clear space
    }

    @Override
    public void clearIndexAll() {
        log.info("모든 엔티티에 대해 모든 인덱스 정보를 삭제합니다...");

        FullTextSession fts = getFullTextSession();
        for (Class clazz : SearchTool.getIndexedClasses(fts.getSessionFactory())) {
            fts.purgeAll(clazz);
            fts.flushToIndexes();
        }
        optimizeAll();
    }

    @Override
    public void optimize(Class<?> clazz) {
        log.info("지정된 수형의 인덱스를 최적화합니다. clazz=[{}]", clazz);
        getFullTextSession().getSearchFactory().optimize(clazz);
    }

    @Override
    public void optimizeAll() {
        log.info("모든 수형의 인덱스를 최적화합니다.");
        getFullTextSession().getSearchFactory().optimize();
    }

    @Override
    public void flush() {
        if (isDebugEnabled) log.debug("세션의 모든 변경 정보를 저장소에 적용합니다...");
        getFullTextSession().flush();
    }

    @Override
    public void flushIndexes() {
        if (isDebugEnabled) log.debug("세션의 모든 인덱스 변경 정보를 저장합니다...");
        getFullTextSession().flushToIndexes();
    }
}
