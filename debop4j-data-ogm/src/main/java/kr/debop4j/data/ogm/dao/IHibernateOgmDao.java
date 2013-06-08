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

import kr.debop4j.core.collection.IPagedList;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * kr.debop4j.data.ogm.dao.IHibernateOgmDao
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 31. 오후 10:31
 */
public interface IHibernateOgmDao extends AutoCloseable {

    /** IHibernateOgmDao에서 사용할 hibernate Session 을 thread local 에 저장할 때의 키 값 */
    String SESSION_KEY = IHibernateOgmDao.class.getName() + ".Session";

    /** IHibernateOgmDao에서 사용할 hibernate-search FullTextSession 을 thread local 에 저장할 때의 키 값 */
    String FULL_TEXT_SESSION_KEY = IHibernateOgmDao.class.getName() + ".FullTextSession";

    /** 기본 Batch size (1000) */
    int DEFAUALT_BATCH_SIZE = 1000;

    /** 현 Thread-context 에서 사용할 Session 를 빈환합니다. */
    Session getSession();

    /** 현 Thread-context 에서 사용할 hibernate-search 의 {@link  org.hibernate.search.FullTextSession} 을 반환합니다. */
    FullTextSession getFullTextSession();

    /**
     * 지정한 형식에 대한 질의 빌더를 생성합니다.
     *
     * @param clazz 엔티티 수형
     * @return 쿼리 빌더
     */
    QueryBuilder getQueryBuilder(Class<?> clazz);

    /**
     * lucene 을 이용한 full text search 를 위한 {@link org.hibernate.search.FullTextQuery}를 반환합니다.
     *
     * @param luceneQuery 조회용 루씬 쿼리
     * @param entities    대상 엔티티 수형들
     * @return FullTextQuery 인스턴스
     */
    FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entities);

    /**
     * 해당 Id 값을 가진 엔티티를 조회합니다. 없으면 null을 반환합니다.
     *
     * @param clazz 조회할 엔티티 수형
     * @param id    조회할 엔티티의 Id 값
     * @return 해당 엔티티, 없으면 null을 반환
     */

    <T> T get(Class<T> clazz, Serializable id);

    /**
     * 지정한 엔티티에 조회를 수행합니다.
     *
     * @param clazz 조회할 엔티티 수형
     * @return 전체 엔티티
     */
    <T> List<T> find(Class<T> clazz);

    /**
     * 지정한 엔티티에 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @return 조회한 결과 엔티티 컬렉션
     */
    <T> List<T> find(Class<T> clazz, Query luceneQuery);

    /**
     * 지정한 엔티티에 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param luceneSort  정렬 방식
     * @return 조회한 결과 엔티티 컬렉션
     */
    <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort luceneSort);

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
    <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort luceneSort);

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

    <T> List<T> find(Class<T> clazz, Query luceneQuery,
                     int firstResult, int maxResults, Sort luceneSort, Criteria criteria);

    /**
     * 검색할 엔티티의 ID 값만 조회합니다 (많은 경우 ID만 필요한 경우가 많습니다)
     *
     * @param clazz       조회할 ID
     * @param luceneQuery 루씬 쿼리
     * @return 조회한 엔티티의 ID의 컬렉션
     */
    List<Serializable> findIds(Class<?> clazz, Query luceneQuery);

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
    List<Serializable> findIds(Class<?> clazz, Query luceneQuery,
                               int firstResult, int maxResults, Sort luceneSort);

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
    List<Serializable> findIds(Class<?> clazz, Query luceneQuery,
                               int firstResult, int maxResults, Sort luceneSort, Criteria criteria);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      Projection 필드들 ({@link  org.hibernate.search.ProjectionConstants})
     * @return Projection 값
     */
    List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      Projection 필드들 ({@link  org.hibernate.search.ProjectionConstants})
     * @param luceneSort  루씬 정렬 방식
     * @return Projection 값
     */
    List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort luceneSort);

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
    List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort luceneSort, Criteria criteria);

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
    List<Object[]> findProjections(Class<?> clazz, Query luceneQuery, String[] fields,
                                   int firstResult, int maxResults, Sort luceneSort, Criteria criteria);

    /**
     * 페이징 조회를 수행합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 조회 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10)
     * @return 검색 결과를 페이징 결과로 반환합니다.
     */
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize);

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
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort luceneSort);

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
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery,
                              int pageNo, int pageSize, Sort luceneSort, Criteria criteria);

    /**
     * 엔티티의 ID 값만 조회하여 Paging 처리해서 반환합니다.
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @return 엔테티의 Id 값을 Paging 처리한 인스턴스
     */
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize);

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
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery,
                                       int pageNo, int pageSize, Sort luceneSort);

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
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery,
                                       int pageNo, int pageSize, Sort luceneSort, Criteria criteria);

    /**
     * 루씬 부가 정보등을 페이징 조회합니다.
     * <p/>
     * 참고 : {@link org.hibernate.search.ProjectionConstants#ID},
     * {@link org.hibernate.search.ProjectionConstants#SCORE} 등
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      조회할 필드
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @return Projection 정보
     */
    IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize);

    /**
     * 루씬 부가 정보등을 페이징 조회합니다.
     * <p/>
     * 참고 : {@link org.hibernate.search.ProjectionConstants#ID},
     * {@link org.hibernate.search.ProjectionConstants#SCORE} 등
     *
     * @param clazz       조회할 엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param fields      조회할 필드
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기 (보통 10 이상)
     * @param luceneSort  루씬 정렬 방식
     * @return Projection 정보
     */
    IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                           int pageNo, int pageSize, Sort luceneSort);

    /**
     * 루씬 부가 정보등을 페이징 조회합니다.
     * <p/>
     * 참고 : {@link org.hibernate.search.ProjectionConstants#ID},
     * {@link org.hibernate.search.ProjectionConstants#SCORE} 등
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
    IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                           int pageNo, int pageSize, Sort luceneSort, Criteria criteria);

    /**
     * 조회 조건에 맞는 엔티티의 갯수를 반환합니다.
     *
     * @param clazz 엔티티 수형
     * @return 조건에 맞는 엔티티의 갯수
     */
    long count(Class<?> clazz);

    /**
     * 조회 조건에 맞는 엔티티의 갯수를 반환합니다.
     *
     * @param clazz       엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @return 조건에 맞는 엔티티의 갯수
     */
    long count(Class<?> clazz, Query luceneQuery);

    /**
     * 조회 조건에 맞는 엔티티의 갯수를 반환합니다.
     *
     * @param clazz       엔티티 수형
     * @param luceneQuery 루씬 쿼리
     * @param criteria    fetching 방식
     * @return 조건에 맞는 엔티티의 갯수
     */
    long count(Class<?> clazz, Query luceneQuery, Criteria criteria);

    /**
     * 엔티티가 있는지 확인합니다.
     *
     * @param clazz 엔티티의 수형
     * @return 존재 여부
     */
    boolean exists(Class<?> clazz);

    /**
     * 조회 조건에 해당하는 엔티티가 있는지 확인합니다.
     *
     * @param clazz       엔티티의 수형
     * @param luceneQuery 루씬 쿼리
     * @return 존재 여부
     */
    boolean exists(Class<?> clazz, Query luceneQuery);

    /**
     * transient Object를 저장합니다. cascading은 cascade="persist" 인 경우에 적용됩니다.
     *
     * @param entity 저장할 transient object
     */
    void persist(Object entity);

    /**
     * 엔티티를 session에 추가합니다. 저장된 엔티티라면, 기존 엔티티의 값을 해당 엔티티의 속성 값으로 update 합니다.
     *
     * @param entity 엔티티
     * @return update된 엔티티
     */
    Object merge(Object entity);

    /**
     * transient object 를 저장합니다.  cascading은 cascade="save-update" 인 경우에 적용됩니다.
     *
     * @param entity 저장할 transient object
     * @return 저장한 object의 ideneifier 값
     */
    Serializable save(Object entity);

    /**
     * transient object 라면 save하고, persistent object 라면 update 합니다.
     *
     * @param entity 저장할 엔티티
     */
    void saveOrUpdate(Object entity);

    /**
     * 엔티티의 정보를 갱신합니다.
     *
     * @param entity persistent object
     */
    void update(Object entity);

    /**
     * 엔티티를 삭제합니다.
     *
     * @param entity 삭제할 엔티티
     */
    void delete(Object entity);

    /**
     * 해당 Id 값을 가지는 엔티티를 삭제합니다.
     *
     * @param clazz 삭제할 엔티티의 수형
     * @param id    Identifier 값
     */
    void deleteById(Class<?> clazz, Serializable id);

    /**
     * Id 컬렉션에 해당하는 엔티티를 모두 삭제합니다.
     *
     * @param clazz 삭제할 엔티티의 수형
     * @param ids   삭제할 엔티티의 Identifier 값들
     */
    void deleteByIds(Class<?> clazz, Collection<? extends Serializable> ids);

    /**
     * 해당 수형의 모든 엔티티 정보를 삭제합니다.
     *
     * @param clazz 삭제할 엔티티
     */
    void deleteAll(Class<?> clazz);

    /**
     * 조회 조건에 해당하는 엔티티들을 모두 삭제합니다.
     *
     * @param clazz       삭제할 엔티티
     * @param luceneQuery 루씬 쿼리
     */
    void deleteAll(Class<?> clazz, Query luceneQuery);

    /**
     * 모든 엔티티들을 삭제합니다.
     *
     * @param entities 삭제할 엔티티 컬렉션
     */
    void deleteAll(Collection<?> entities);

    /**
     * 해당 엔티티의 인덱스 정보를 제거합니다.
     *
     * @param clazz 엔티티 수형
     * @param id    엔티티의 id 값
     */
    void purge(Class<?> clazz, Serializable id);

    /**
     * 지정된 수형의 모든 엔티티들의 인덱스 정보를 제거합니다.
     *
     * @param clazz 해당 엔티티의 수형
     */
    void purgeAll(Class<?> clazz);

    /** Session에 남아있는 인덱싱 작업을 강제로 수행하도록 합니다. */
    void flushToIndexes();

    /**
     * 엔티티를 수동으로 재 인덱싱합니다.<br/>
     * Force the (re)indexing of a given <b>managed</b> object.
     *
     * @param entity 재인덱싱할 엔티티
     */
    <T> void index(T entity);

    /**
     * 지정된 수형의 모든 엔티티들을 인덱싱 합니다.
     *
     * @param clazz     인덱싱할 엔티티
     * @param batchSize 배치 크기
     */
    void indexAll(Class<?> clazz, int batchSize);

    /**
     * 해당 수형의 모든 인덱스를 비동기 방식으로 구성합니다.
     *
     * @param clazz     엔티티 수형
     * @param batchSize Batch 크기 (작은 크기로 나눠서 인덱싱을 수행하여, 메모리를 절약합니다.)
     * @return {@link Future}
     */
    Future<Void> indexAllAsync(Class<?> clazz, int batchSize);

    /**
     * 해당 수형의 모든 인덱스 정보를 삭제합니다.
     *
     * @param clazz 엔티티 수형
     */
    void clearIndex(Class<?> clazz);

    /** 모든 인덱스를 삭제합니다. */

    void clearIndexAll();

    /**
     * 해당 엔티티의 인덱스를 최적화합니다.
     *
     * @param clazz 최적화할 엔티티의 수형
     */
    void optimize(Class<?> clazz);

    /** 모든 엔티티의 인덱스를 최적화합니다. */
    void optimizeAll();

    /** 세션의 모든 변경을 저장소에 적용한다. */
    void flush();

    /** 세션의 모든 인덱스 변경 정보를 저장합니다. */
    void flushIndexes();
}
