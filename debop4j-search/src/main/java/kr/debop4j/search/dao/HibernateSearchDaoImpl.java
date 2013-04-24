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

import kr.debop4j.core.Local;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.SimplePagedList;
import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.impl.HibernateDao;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;

/**
 * hibernate-search 를 이용하여 엔티티를 관리하는 Data Access Object 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 20. 오후 10:03
 */
@Slf4j
@SuppressWarnings("unchecked")
public class HibernateSearchDaoImpl extends HibernateDao {

    private static final String SESSION_KEY = "kr.debop4j.search.dao.HibernateSearchDao.Session";
    private static final String FULL_TEXT_SESSION_KEY = "kr.debop4j.search.dao.HibernateSearchDao.FullTextSession";
    private static final int BATCH_SIZE = 100;

    private final SessionFactory sessionFactory;

    public HibernateSearchDaoImpl() {
        this(UnitOfWorks.getCurrentSessionFactory());
    }

    public HibernateSearchDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public HibernateSearchDaoImpl(Session session) {
        this(session.getSessionFactory());
        Local.put(SESSION_KEY, session);
    }

    public synchronized Session getSession() {
        Session session = (Session) Local.get(SESSION_KEY);
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            Local.put(SESSION_KEY, session);

            if (log.isDebugEnabled())
                log.debug("새로운 Session을 open 했습니다.");
        }
        return session;
    }

    public synchronized FullTextSession getFullTextSession() {
        FullTextSession fts = (FullTextSession) Local.get(FULL_TEXT_SESSION_KEY);

        if (fts == null || !fts.isOpen()) {
            fts = Search.getFullTextSession(getSession());
            Local.put(FULL_TEXT_SESSION_KEY, fts);

            if (log.isDebugEnabled())
                log.debug("Current Thread Context에 새로운 FullTextSession을 생성했습니다. threadName=[{}]",
                          Thread.currentThread().getName());
        }
        return fts;
    }

    public synchronized FullTextQuery createFullTextQuery(Query luceneQuery, Class<?>... classes) {
        FullTextQuery ftq = getFullTextSession().createFullTextQuery(luceneQuery, classes);

        // hibernate-ogm 에서는 DataRetrievalMethod.FIND_BY_ID 를 사용해야 합니다.
        //
        ftq.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.QUERY);

        return ftq;
    }

    public <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort sort) {
        FullTextQuery ftq = this.createFullTextQuery(luceneQuery, clazz);
        if (sort != null)
            ftq.setSort(sort);

        return (List<T>) ftq.list();
    }

    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort) {
        return find(clazz, luceneQuery, firstResult, maxResults, sort, null);
    }

    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria) {
        if (log.isTraceEnabled())
            log.trace("엔티티 조회. clazz=[{}], luceneQuery=[{}], fitstResult=[{}], maxResults=[{}], sort=[{}], criteria=[{}]",
                      clazz, luceneQuery, firstResult, maxResults, sort, criteria);

        FullTextQuery ftq = this.createFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, firstResult, maxResults);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);
        return ftq.list();
    }

    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, sort, null);
    }

    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria) {
        if (log.isTraceEnabled())
            log.trace("엔티티 페이징 조회. clazz=[{}], luceneQuery=[{}], pageNo=[{}], pageSize=[{}], sort=[{}]",
                      clazz, luceneQuery, pageNo, pageSize, sort);

        long totalCount = count(clazz, luceneQuery);
        FullTextQuery ftq = this.createFullTextQuery(luceneQuery, clazz);

        HibernateTool.setPaging(ftq, (pageNo - 1) * pageSize, pageSize);
        if (sort != null) ftq.setSort(sort);
        if (criteria != null) ftq.setCriteriaQuery(criteria);  // fetching strategy 같은 겻을 제공할 수 있다.

        return new SimplePagedList<T>(ftq.list(), pageNo, pageSize, totalCount);
    }

    /**
     * 지정한 쿼리를 수행하여 해당 엔티티의 ID 값만 가져옵니다.
     */
    public IPagedList<Serializable> getProjectionPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort) {
        return getProjectionPage(clazz, luceneQuery, new String[]{ FullTextQuery.ID }, pageNo, pageSize, sort);
    }

    /**
     * 지정한 쿼리를 수행하여 해당 필드의 값들만 뽑아온다.
     */
    public IPagedList getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort sort) {
        if (log.isTraceEnabled())
            log.trace("엔티티 Projection 조회. clazz=[{}], luceneQuery=[{}], fields=[{}], pageNo=[{}], pageSize=[{}], sort=[{}]",
                      clazz, luceneQuery, StringTool.listToString(fields), pageNo, pageSize, sort);

        FullTextQuery ftq = this.createFullTextQuery(luceneQuery, clazz);
        HibernateTool.setPaging(ftq, (pageNo - 1) * pageSize, pageSize);
        ftq.setProjection(fields);
        if (sort != null)
            ftq.setSort(sort);
        return new SimplePagedList<>(ftq.list(), pageNo, pageSize, count(clazz, luceneQuery));
    }

    public long count(Class<?> clazz) {
        return count(clazz, DetachedCriteria.forClass(clazz));
    }

    public long count(Class<?> clazz, Query luceneQuery) {
        FullTextQuery ftq = this.createFullTextQuery(luceneQuery, clazz);
        return (long) ftq.getResultSize();
    }


    /**
     * 해당 엔티티의 인덱스 정보만을 삭제합니다.
     */
    public void purge(Class<?> clazz, Serializable id) {
        getFullTextSession().purge(clazz, id);
    }

    /**
     * 지정된 수형의 인덱싱 정보를 삭제합니다. (DB의 엔티티 정보는 보존합니다.)
     */
    public void purgeAll(Class<?> clazz) {
        getFullTextSession().purgeAll(clazz);
    }

    /**
     * 엔티티를 인덱싱합니다.
     */
    public <T> void index(T entity) {
        getFullTextSession().index(entity);
    }

    /**
     * 지정된 수형의 모든 엔티티들을 인덱싱 합니다.
     */
    public void indexAll(Class<?> clazz, int batchSize) {
        if (log.isDebugEnabled())
            log.debug("수형[{}]의 모든 엔티티에 대해 인덱싱을 수행합니다...", clazz);

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

    public Future<Void> indexAllAsync(final Class<?> clazz, final int batchSize) {
        if (log.isDebugEnabled())
            log.debug("비동기 방식으로 엔티티에 대해 인덱싱을 수행합니다... clazz=[{}], batchSize=[{}]", clazz, batchSize);
        return AsyncTool.newTask(new Runnable() {
            @Override
            public void run() {
                indexAll(clazz, batchSize);
            }
        });
    }

    public void clearIndex(Class<?> clazz) {
        if (log.isDebugEnabled())
            log.debug("엔티티에 대한 모든 인덱스 정보를 삭제합니다... clazz=[{}]", clazz);

        FullTextSession fts = getFullTextSession();
        fts.purgeAll(clazz);                        // remove obsolete index
        fts.flushToIndexes();                       // apply purge before optimize
        fts.getSearchFactory().optimize(clazz);     // physically clear space
    }
}
