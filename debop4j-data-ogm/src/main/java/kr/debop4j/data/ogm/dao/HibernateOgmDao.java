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
import kr.debop4j.core.collection.SimplePagedList;
import kr.debop4j.core.parallelism.AsyncTool;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * hibernate-ogm 용 DAO 입니다.<br />
 * hibernate의 Criteria 기능을 제공할 수 없어, Criteria 부분은 hibernate-search를 사용합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 15. 오후 5:42
 */
@Component
@SuppressWarnings("unchecked")
public class HibernateOgmDao implements IHibernateOgmDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateOgmDao.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private SessionFactory sessionFactory;

    /**
     * Hibernate OGM 을 사용하는 Data Access Object를 생성합니다.
     *
     * @param sessionFactory SessionFactory
     */
    @Autowired
    public HibernateOgmDao(SessionFactory sessionFactory) {
        shouldNotBeNull(sessionFactory, "sessionFactory");
        this.sessionFactory = sessionFactory;
    }

    /** 현 Thread-context 에서 사용할 Session 를 빈환합니다. */

    @Override
    public synchronized final Session getSession() {
        Session session = Local.get(SESSION_KEY, Session.class);

        if (session == null || !session.isOpen()) {
            log.debug("새로운 Session을 생성합니다...");
            session = sessionFactory.openSession();
            Local.put(SESSION_KEY, session);
        }
        return session;
    }

    /** 현 Thread-context 에서 사용할 hibernate-search 의 {@link  FullTextSession} 을 반환합니다. */

    @Override
    public synchronized final FullTextSession getFullTextSession() {
        FullTextSession fts = Local.get(FULL_TEXT_SESSION_KEY, FullTextSession.class);

        if (fts == null || !fts.isOpen()) {
            log.debug("현 ThreadContext에 새로운 FullTextSession을 생성합니다...");
            fts = Search.getFullTextSession(getSession());
            Local.put(FULL_TEXT_SESSION_KEY, fts);
        }
        return fts;
    }

    /** 지정한 형식에 대한 질의 빌더를 생성합니다. */

    @Override
    public final QueryBuilder getQueryBuilder(Class<?> clazz) {
        return getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
    }

    /**
     * lucene 을 이용한 full text search 를 위한 {@link FullTextQuery}를 반환합니다.
     *
     * @param luceneQuery 조회용 루씬 쿼리
     * @param entities    대상 엔티티 수형들
     * @return FullTextQuery 인스턴스
     */

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

    /**
     * 해당 Id 값을 가진 엔티티를 조회합니다. 없으면 null을 반환합니다.
     *
     * @param clazz 조회할 엔티티 수형
     * @param id    조회할 엔티티의 Id 값
     * @return 해당 엔티티, 없으면 null을 반환
     */

    @Override
    public <T> T get(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    @Override
    public <T> List<T> find(Class<T> clazz) {
        return find(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    /**
     * 지정한 엔티티에 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @return 조회한 결과 엔티티 컬렉션
     */

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery) {
        return find(clazz, luceneQuery, -1, -1, null, null);
    }

    /**
     * 지정한 엔티티에 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param luceneSort  정렬 방식
     * @return 조회한 결과 엔티티 컬렉션
     */

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort luceneSort) {
        return find(clazz, luceneQuery, -1, -1, luceneSort, null);
    }

    /**
     * 엔티티 조회를 수행합니다.
     *
     * @param clazz       엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param firstResult 첫번재 결과 (0부터 시작)
     * @param maxResults  최대 결과 수
     * @param luceneSort  정렬 방식
     * @return 조회 결과
     */
    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort) {
        return find(clazz, luceneQuery, firstResult, maxResults, luceneSort, null);
    }

    /**
     * 엔티티 조회를 수행합니다.
     *
     * @param clazz       엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param firstResult 첫번재 결과 (0부터 시작)
     * @param maxResults  최대 결과 수
     * @param luceneSort  정렬 방식
     * @param criteria    association이 있는 경우 join 방식을 변경할 수 있다.
     * @return 조회 결과
     */
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

    /**
     * 검색할 엔티티의 ID 값만 조회합니다 (많은 경우 ID만 필요한 경우가 많습니다)
     *
     * @param clazz       조회할 ID
     * @param luceneQuery 루씬 쿼리
     * @return 조회한 엔티티의 ID의 컬렉션
     */
    @Override
    public List<Serializable> findIds(Class<?> clazz, Query luceneQuery) {
        return findIds(clazz, luceneQuery, -1, -1, null, null);
    }

    /**
     * 검색할 엔티티의 ID 값만 조회합니다 (많은 경우 ID만 필요한 경우가 많습니다)
     *
     * @param clazz       조회할 ID
     * @param luceneQuery 루씬 쿼리
     * @param firstResult 첫번째 결과 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param luceneSort  루씬 정렬 방식
     * @return 조회한 엔티티의 ID의 컬렉션
     */
    @Override
    public List<Serializable> findIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort) {
        return findIds(clazz, luceneQuery, firstResult, maxResults, luceneSort, null);
    }

    /**
     * 검색할 엔티티의 ID 값만 조회합니다 (많은 경우 ID만 필요한 경우가 많습니다)
     *
     * @param clazz       조회할 ID
     * @param luceneQuery 루씬 쿼리
     * @param firstResult 첫번째 결과 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param luceneSort  루씬 정렬 방식
     * @param criteria    fetching 방식 등
     * @return 조회한 엔티티의 ID의 컬렉션
     */
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

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      Projection 필드들 ({@link  org.hibernate.search.ProjectionConstants})
     * @return Projection 값
     */
    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields) {
        return findProjections(clazz, luceneQuery, fields, -1, -1, null, null);
    }

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      Projection 필드들 ({@link  org.hibernate.search.ProjectionConstants})
     * @param luceneSort  루씬 정렬 방식
     * @return Projection 값
     */
    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort luceneSort) {
        return findProjections(clazz, luceneQuery, fields, -1, -1, luceneSort, null);
    }

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      Projection 필드들 ({@link  org.hibernate.search.ProjectionConstants})
     * @param luceneSort  루씬 정렬 방식
     * @param criteria    Fetching 등 기존 association외의 fetching을 정의할 수 있다.
     * @return Projection 값
     */
    @Override
    public List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort luceneSort, Criteria criteria) {
        return findProjections(clazz, luceneQuery, fields, -1, -1, luceneSort, criteria);
    }

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      Projection 필드들 ({@link  org.hibernate.search.ProjectionConstants})
     * @param firstResult 첫번째 결과 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param luceneSort  루씬 정렬 방식
     * @param criteria    Fetching 등 기존 association외의 fetching을 정의할 수 있다.
     * @return Projection 값
     */
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


    /**
     * 페이징 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 조회 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10)
     * @return 검색 결과를 페이징 결과로 반환합니다.
     */
    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, null, null);
    }

    /**
     * 페이징 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 조회 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10)
     * @param luceneSort  정렬 방식
     * @return 검색 결과를 페이징 결과로 반환합니다.
     */

    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort) {
        return getPage(clazz, luceneQuery, pageNo, pageSize, luceneSort, null);
    }

    /**
     * 페이징 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 조회 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10)
     * @param luceneSort  정렬 방식
     * @param criteria    association이 있는 경우 fetching 방식을 변경할 수 있습니다.
     * @return 검색 결과를 페이징 결과로 반환합니다.
     */

    @Override
    public <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort, Criteria criteria) {
        if (log.isTraceEnabled())
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

    /**
     * 엔티티의 ID 값만 조회하여 Paging 처리해서 반환합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @return 엔테티의 Id 값을 Paging 처리한 인스턴스
     */
    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize) {
        return getIdPage(clazz, luceneQuery, pageNo, pageSize, null, null);
    }

    /**
     * 엔티티의 ID 값만 조회하여 Paging 처리해서 반환합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @param luceneSort  정렬 방식
     * @return 엔테티의 Id 값을 Paging 처리한 인스턴스
     */
    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort) {
        return getIdPage(clazz, luceneQuery, pageNo, pageSize, luceneSort, null);
    }

    /**
     * 엔티티의 ID 값만 조회하여 Paging 처리해서 반환합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @param luceneSort  정렬 방식
     * @param criteria    fetching 등 방식을 제공
     * @return 엔테티의 Id 값을 Paging 처리한 인스턴스
     */

    @Override
    public IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort, Criteria criteria) {
        IPagedList<Object[]> list = getProjectionPage(clazz, luceneQuery, new String[] { FullTextQuery.ID }, pageNo, pageSize, luceneSort, criteria);
        List<Serializable> ids = Lists.newArrayList();
        for (Object[] fields : list.getList()) {
            ids.add((Serializable) fields[0]);
        }
        return new SimplePagedList(ids, pageNo, pageSize, list.getItemCount());
    }

    /**
     * 루씬 부가 정보등을 페이징 조회합니다.
     * <p/>
     * 참고 : {@link ProjectionConstants#ID}, {@link ProjectionConstants#SCORE} 등
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      조회할 필드
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @return Projection 정보
     */
    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize) {
        return getProjectionPage(clazz, luceneQuery, fields, pageNo, pageSize, null, null);
    }

    /**
     * 루씬 부가 정보등을 페이징 조회합니다.
     * <p/>
     * 참고 : {@link ProjectionConstants#ID}, {@link ProjectionConstants#SCORE} 등
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      조회할 필드
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @param luceneSort  루씬 정렬 방식
     * @return Projection 정보
     */
    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort luceneSort) {
        return getProjectionPage(clazz, luceneQuery, fields, pageNo, pageSize, luceneSort, null);
    }


    /**
     * 루씬 부가 정보등을 페이징 조회합니다.
     * <p/>
     * 참고 : {@link ProjectionConstants#ID}, {@link ProjectionConstants#SCORE} 등
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      조회할 필드
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @param luceneSort  루씬 정렬 방식
     * @param criteria    fetching 방식
     * @return Projection 정보
     */

    @Override
    public IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort luceneSort, Criteria criteria) {
        long count = this.count(clazz, luceneQuery);
        List<Object[]> projections = this.findProjections(clazz, luceneQuery, fields, (pageNo - 1) * pageSize, pageSize, luceneSort, criteria);

        return new PaginatedList<>(projections, pageNo, pageSize, count);
    }

    /**
     * 조회 조건에 맞는 엔티티의 갯수를 반환합니다.
     *
     * @param clazz 엔티티 수형
     * @return 조건에 맞는 엔티티의 갯수
     */

    @Override
    public long count(Class<?> clazz) {
        return count(clazz, getQueryBuilder(clazz).all().createQuery(), null);
    }

    /**
     * 조회 조건에 맞는 엔티티의 갯수를 반환합니다.
     *
     * @param clazz       엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @return 조건에 맞는 엔티티의 갯수
     */

    @Override
    public long count(Class<?> clazz, Query luceneQuery) {
        return count(clazz, luceneQuery, null);
    }

    /**
     * 조회 조건에 맞는 엔티티의 갯수를 반환합니다.
     *
     * @param clazz       엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param criteria    fetching 방식
     * @return 조건에 맞는 엔티티의 갯수
     */
    @Override
    public long count(Class<?> clazz, Query luceneQuery, Criteria criteria) {
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);

        if (criteria != null)
            ftq.setCriteriaQuery(criteria);

        int count = ftq.getResultSize();

        if (isTraceEnabled)
            log.trace("entity counting. entity=[{}], query=[{}], count=[{}]", clazz, luceneQuery, count);

        return count;
    }

    /**
     * 엔티티가 있는지 확인합니다.
     *
     * @param clazz 엔티티의 수형
     * @return 존재 여부
     */
    @Override
    public boolean exists(Class<?> clazz) {
        return exists(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    /**
     * 조회 조건에 해당하는 엔티티가 있는지 확인합니다.
     *
     * @param clazz       엔티티의 수형
     * @param luceneQuery 루씬 쿼리
     * @return 존재 여부
     */
    @Override
    public boolean exists(Class<?> clazz, Query luceneQuery) {
        List<Serializable> ids = findIds(clazz, luceneQuery, 0, 1, null);
        return (ids != null && ids.size() > 0);
    }

    /**
     * transient Object를 저장합니다. cascading은 cascade="persist" 인 경우에 적용됩니다.
     *
     * @param entity 저장할 transient object
     */

    @Override
    public void persist(Object entity) {
        getFullTextSession().persist(entity);
    }

    /**
     * 엔티티를 session에 추가합니다. 저장된 엔티티라면, 기존 엔티티의 값을 해당 엔티티의 속성 값으로 update 합니다.
     *
     * @param entity 엔티티
     * @return update된 엔티티
     */

    @Override
    public Object merge(Object entity) {
        return getFullTextSession().merge(entity);
    }

    /**
     * transient object 를 저장합니다.  cascading은 cascade="save-update" 인 경우에 적용됩니다.
     *
     * @param entity 저장할 transient object
     * @return 저장한 object의 ideneifier 값
     */

    @Override
    public Serializable save(Object entity) {
        return getFullTextSession().save(entity);
    }

    /**
     * transient object 라면 save하고, persistent object 라면 update 합니다.
     *
     * @param entity 저장할 엔티티
     */

    @Override
    public void saveOrUpdate(Object entity) {
        getFullTextSession().saveOrUpdate(entity);
    }

    /**
     * 엔티티의 정보를 갱신합니다.
     *
     * @param entity persistent object
     */

    @Override
    public void update(Object entity) {
        getFullTextSession().update(entity);
    }

    /**
     * 엔티티를 삭제합니다.
     *
     * @param entity 삭제할 엔티티
     */

    @Override
    public void delete(Object entity) {
        if (entity == null) return;
        getFullTextSession().delete(entity);
    }

    /**
     * 해당 Id 값을 가지는 엔티티를 삭제합니다.
     *
     * @param clazz 삭제할 엔티티의 수형
     * @param id    Identifier 값
     */

    @Override
    public void deleteById(Class<?> clazz, Serializable id) {
        Object entity = getFullTextSession().load(clazz, id);
        delete(entity);
    }

    /**
     * Id 컬렉션에 해당하는 엔티티를 모두 삭제합니다.
     *
     * @param clazz 삭제할 엔티티의 수형
     * @param ids   삭제할 엔티티의 Identifier 값들
     */
    @Override
    public void deleteByIds(Class<?> clazz, Collection<? extends Serializable> ids) {
        if (isTraceEnabled)
            log.trace("Id 컬렉션에 해당하는 엔티티들을 삭제합니다. clazz=[{}], ids=[{}]", clazz, StringTool.listToString(ids));

        FullTextSession fts = getFullTextSession();
        for (Serializable id : ids)
            delete(fts.load(clazz, id));
    }

    /**
     * 해당 수형의 모든 엔티티 정보를 삭제합니다.
     *
     * @param clazz 삭제할 엔티티
     */

    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    /**
     * 조회 조건에 해당하는 엔티티들을 모두 삭제합니다.
     *
     * @param clazz       삭제할 엔티티
     * @param luceneQuery 루씬 쿼리
     */
    @Override
    public void deleteAll(Class<?> clazz, Query luceneQuery) {
        deleteByIds(clazz, findIds(clazz, luceneQuery));
    }

    /**
     * 모든 엔티티들을 삭제합니다.
     *
     * @param entities 삭제할 엔티티 컬렉션
     */

    @Override
    public void deleteAll(Collection<?> entities) {
        if (ArrayTool.isEmpty(entities))
            return;

        if (isTraceEnabled)
            log.trace("엔티티 컬렉션을 모두 삭제합니다... entity count=[{}]", entities.size());

        FullTextSession fts = getFullTextSession();
        for (Object entity : entities) {
            fts.delete(entity);
        }
    }

    /**
     * 해당 엔티티의 인덱스 정보를 제거합니다.
     *
     * @param clazz 엔티티 수형
     * @param id    엔티티의 id 값
     */

    @Override
    public void purge(Class<?> clazz, Serializable id) {
        if (isTraceEnabled) log.trace("인덱스를 제거합니다. clazz=[{}], id=[{}]", clazz, id);
        getFullTextSession().purge(clazz, id);
    }

    /**
     * 지정된 수형의 모든 엔티티들의 인덱스 정보를 제거합니다.
     *
     * @param clazz 해당 엔티티의 수형
     */

    @Override
    public void purgeAll(Class<?> clazz) {
        log.info("지정된 수형의 모든 엔티티들의 인덱스를 제거합니다. clazz=[{}]", clazz);
        getFullTextSession().purgeAll(clazz);
    }

    /** Session에 남아있는 인덱싱 작업을 강제로 수행하도록 합니다. */

    @Override
    public void flushToIndexes() {
        getFullTextSession().flushToIndexes();
    }

    /**
     * 엔티티를 수동으로 재 인덱싱합니다.<br/>
     * Force the (re)indexing of a given <b>managed</b> object.
     *
     * @param entity 재인덱싱할 엔티티
     */

    @Override
    public <T> void index(T entity) {
        shouldNotBeNull(entity, "entity");
        if (isTraceEnabled) log.trace("수동으로 재 인덱스를 수행합니다. entity=[{}]", entity);

        getFullTextSession().index(entity);
    }

    /**
     * 지정된 수형의 모든 엔티티들을 인덱싱 합니다.
     *
     * @param clazz     인덱싱할 엔티티
     * @param batchSize 배치 크기
     */

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
                index++;
                fts.index(results.get(0));
                if (index % batchSize == 0) {
                    fts.flushToIndexes();
                    fts.clear();
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

    /** 해당 수형의 모든 인덱스를 비동기 방식으로 구성합니다. */

    @Override
    public Future<Void> indexAllAsync(final Class<?> clazz, final int batchSize) {
        if (isDebugEnabled)
            log.debug("비동기 방식으로 엔티티에 대해 인덱싱을 수행합니다... clazz=[{}], batchSize=[{}]", clazz, batchSize);

        return AsyncTool.startNew(new Callable<Void>() {

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
     *
     * @param clazz 엔티티 수형
     */

    @Override
    public void clearIndex(Class<?> clazz) {
        if (isDebugEnabled) log.debug("엔티티에 대한 모든 인덱스 정보를 삭제합니다... clazz=[{}]", clazz);

        getFullTextSession().purgeAll(clazz);       // remove obsolete index
        getFullTextSession().flushToIndexes();      // apply purge before optimize
        optimize(clazz);                            // physically clear space
    }

    /** 모든 인덱스를 삭제합니다. */

    @Override
    public void clearIndexAll() {
        log.info("모든 엔티티에 대해 모든 인덱스 정보를 삭제합니다...");

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
        log.info("지정된 수형의 인덱스를 최적화합니다. clazz=[{}]", clazz);
        getFullTextSession().getSearchFactory().optimize(clazz);
    }

    /** 모든 엔티티의 인덱스를 최적화합니다. */

    @Override
    public void optimizeAll() {
        log.info("모든 수형의 인덱스를 최적화합니다.");
        getFullTextSession().getSearchFactory().optimize();
    }

    /** 세션의 모든 변경을 저장소에 적용한다. */

    @Override
    public void flush() {
        if (isDebugEnabled) log.debug("세션의 모든 변경 정보를 저장소에 적용합니다...");
        getFullTextSession().flush();
    }

    /** 세션의 모든 인덱스 변경 정보를 저장합니다. */

    @Override
    public void flushIndexes() {
        if (isDebugEnabled) log.debug("세션의 모든 인덱스 변경 정보를 저장합니다...");
        getFullTextSession().flushToIndexes();
    }
}
