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

import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.PaginatedList;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWorkFactory;
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
 * hibernate-ogm을 기반으로 하는 Data Access Object의 Interface입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 5. 오전 1:16
 */
public interface SearchDao extends AutoCloseable {

    /** 현 Thread-context에서 사용할 {@link org.hibernate.Session} 의 저장소 키 (참고: {@link kr.debop4j.core.Local}) */
    public String SESSION_KEY = IUnitOfWorkFactory.CURRENT_HIBERNATE_SESSION;

    /** 현 Thread-context에서 사용할 {@link org.hibernate.search.FullTextSession}의 저장소 키 (참고: {@link kr.debop4j.core.Local}) */
    public String FULL_TEXT_SESSION_KEY = SearchDao.class.getName() + ".FullTextSession";

    /** 현 Thread-context 에서 사용할 Session 를 빈환합니다. */
    Session getSession();

    /** 현 Thread-context 에서 사용할 hibernate-search 의 {@link  FullTextSession} 을 반환합니다. */
    FullTextSession getFullTextSession();

    /**
     * lucene을 이용한 Full text search를 위한 {@link FullTextQuery}를 반환합니다.
     *
     * @param luceneQuery 루씬 쿼리 인스턴스
     * @param entities    대상 엔티티들
     * @return {@link FullTextQuery} 인스턴스
     */
    FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entities);

    /**
     * {@link Query}를 빌드해주는 {@link QueryBuilder}를 반환합니다.
     *
     * @param clazz the clazz 질의 대상 쿼리
     * @return the query builder
     */
    QueryBuilder getQueryBuilder(Class<?> clazz);

    /**
     * 지정한 Id 값을 가지는 엔티티를 로드합니다. 없으면 null 을 반환합니다.
     *
     * @param clazz the clazz
     * @param id    the id
     * @return the t
     */
    <T> T get(Class<T> clazz, Serializable id);

    /**
     * 지정한 수형의 모든 엔티티를 조회합니다.
     *
     * @param clazz the clazz 대상 엔티티
     * @return 검색 결과
     */
    <T> List<T> findAll(Class<T> clazz);

    /**
     * 지정한 수형의 모든 엔티티를 조회합니다.
     *
     * @param clazz      the clazz
     * @param luceneSort the lucene sort
     * @return the list
     */
    <T> List<T> findAll(Class<T> clazz, Sort luceneSort);

    /**
     * 지정한 엔티티를 조회합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param sort        정렬 방법
     * @param criteria    Association 방식 등을 정의
     * @return 검색된 엔티티의 컬렉션
     */
    <T> List<T> findAll(Class<T> clazz, Query luceneQuery, Sort sort, Criteria criteria);

    /**
     * 지정한 엔티티를 조회합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param firstResult 첫번째 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param sort        정렬 방법
     * @return 검색된 엔티티의 컬렉션
     */
    <T> List<T> findAll(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort);

    /**
     * 지정한 엔티티를 조회합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param firstResult 첫번째 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param sort        정렬 방법
     * @param criteria    Association 방식 등을 정의
     * @return 검색된 엔티티의 컬렉션
     */
    <T> List<T> findAll(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria);

    /**
     * Page 단위로 엔티티를 조회합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    the page size
     * @return 검색 결과 Page
     */
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize);

    /**
     * Page 단위로 엔티티를 조회합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    the page size
     * @param sort        정렬 방식
     * @return 검색 결과 Page
     */
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort);

    /**
     * Page 단위로 엔티티를 조회합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    the page size
     * @param sort        정렬 방식
     * @param criteria    조인 조건 등
     * @return 검색 결과 Page
     */
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria);

    /**
     * 특정 엔티티에 대해 검색하여 Id만 반환한다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @return 검색된 엔티티의 Id의 컬렉션
     */
    List<Serializable> getAllIds(Class<?> clazz, Query luceneQuery);

    /**
     * 특정 엔티티에 대해 검색하여 Id만 반환한다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param firstResult 첫번째 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param sort        정렬 방법
     * @return 검색된 엔티티의 Id의 컬렉션
     */
    List<Serializable> getAllIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort);

    /**
     * 특정 엔티티에 대해 검색하여 Id만 반환한다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param firstResult 첫번째 인덱스 (0부터 시작)
     * @param maxResults  최대 결과 갯수
     * @param sort        정렬 방법
     * @param criteria    Association 방식 등을 정의
     * @return 검색된 엔티티의 Id의 컬렉션
     */
    List<Serializable> getAllIds(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria);

    /**
     * 페이지 단위로 엔티티를 조회하여, Id 값만 반환합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    the page size
     * @return 검색 결과 Page
     */
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize);

    /**
     * 페이지 단위로 엔티티를 조회하여, Id 값만 반환합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기
     * @param sort        정렬 방식
     * @return 검색 결과 Page
     */
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort);

    /**
     * 페이지 단위로 엔티티를 조회하여, Id 값만 반환합니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기
     * @param sort        정렬 방식
     * @param criteria    조인 조건 등
     * @return 검색 결과 Page
     */
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param fields      {@link org.hibernate.search.ProjectionConstants} 값의 배열
     * @return field에 해당하는 값의 배열
     */
    List<Object[]> getProjections(Class<?> clazz, Query luceneQuery, String[] fields);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param fields      {@link org.hibernate.search.ProjectionConstants} 값의 배열
     * @param sort        정렬 방식
     * @return field에 해당하는 값의 배열
     */
    List<Object[]> getProjections(Class<?> clazz, Query luceneQuery, String[] fields, Sort sort);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param fields      {@link org.hibernate.search.ProjectionConstants} 값의 배열
     * @param sort        정렬 방식
     * @param criteria    조인 조건 등
     * @return field에 해당하는 값의 배열
     */
    List<Object[]> getProjections(Class<?> clazz, Query luceneQuery, String[] fields,
                                  int firstResult, int maxResults, Sort sort, Criteria criteria);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param fields      {@link org.hibernate.search.ProjectionConstants} 값의 배열
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기
     * @return the projection page
     */
    PaginatedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                              int pageNo, int pageSize);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param fields      {@link org.hibernate.search.ProjectionConstants} 값의 배열
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기
     * @param sort        정렬 방식
     * @return the projection page
     */
    PaginatedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                              int pageNo, int pageSize, Sort sort);

    /**
     * 엔티티 조회 시에 특정 정보만을 가져온다. fields 값에는 {@link org.hibernate.search.ProjectionConstants} 를 쓸 수 있습니다.
     *
     * @param clazz       대상 엔티티
     * @param luceneQuery 조회 방법
     * @param fields      {@link org.hibernate.search.ProjectionConstants} 값의 배열
     * @param pageNo      페이지 번호 (1부터 시작)
     * @param pageSize    페이지 크기
     * @param sort        정렬 방식
     * @param criteria    조인 조건 등
     * @return the projection page
     */
    PaginatedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields,
                                              int pageNo, int pageSize, Sort sort, Criteria criteria);

    /**
     * 해당 수형의 엔티티의 수를 가져온다.
     *
     * @param clazz 대상 엔티티의 수형
     * @return 엔티티의 갯수
     */
    int count(Class<?> clazz);

    /**
     * 특정 엔티티에 대해 조건에 맞는 엔티티 수를 가져온다.
     *
     * @param clazz       대상 엔티티의 수형
     * @param luceneQuery 질의 조건
     * @return 조건애 맞는 엔티티 수
     */
    int count(Class<?> clazz, Query luceneQuery);

    /**
     * 특정 엔티티에 대해 조건에 맞는 엔티티 수를 가져온다.
     *
     * @param clazz       대상 엔티티의 수형
     * @param luceneQuery 질의 조건
     * @return 조건애 맞는 엔티티 수
     */
    int count(Class<?> clazz, Query luceneQuery, Criteria criteria);

    /**
     * 엔티티를 삽입합니다.
     *
     * @param entity 엔티티
     */
    void persist(Object entity);

    /**
     * 엔티티를 갱신하거나 삽입합니다.
     *
     * @param entity 엔티티
     * @return 갱신된 엔티티
     */
    Object merge(Object entity);

    /**
     * 엔티티를 삽입합니다.
     *
     * @param entity 저장할 엔티티
     * @return 저장한 엔티티의 Id 값
     */
    Serializable save(Object entity);

    /**
     * 엔티티를 삽입하거나 갱신합니다.
     *
     * @param entity 대상 엔티티
     */
    void saveOrUpdate(Object entity);

    /**
     * 엔티티를 갱신합니다.
     *
     * @param entity 갱신할 엔티티
     */
    void update(Object entity);

    /**
     * 엔티티를 삭제합니다.
     *
     * @param entity 삭제할 엔티티
     */
    void delete(Object entity);

    /**
     * 지정한 Id 값을 가진 엔티티를 삭제합니다.
     *
     * @param clazz 엔티티의 수형
     * @param id    삭제할 엔티티의 Id 값
     */
    void deleteById(Class<?> clazz, Serializable id);

    /**
     * 지정한 Id 값을 가진 엔티티들을 삭제합니다.
     *
     * @param clazz 엔티티의 수형
     * @param ids   삭제할 엔티티의 Id 값의 컬렉션
     */
    void deleteByIds(Class<?> clazz, Collection<? extends Serializable> ids);

    /**
     * 해당 수형의 모든 엔티티를 삭제합니다.
     *
     * @param clazz 삭제할 엔티티의 수형
     */
    void deleteAll(Class<?> clazz);

    /**
     * 쿼리 결과에 해당하는 엔티티들을 모두 삭제합니다.
     *
     * @param clazz       삭제할 엔티티의 수형
     * @param luceneQuery 조회할 쿼리
     */
    void deleteAll(Class<?> clazz, Query luceneQuery);

    /**
     * 엔티티 컬렉션의 모든 엔티티를 삭제합니다.
     *
     * @param entities 삭제할 엔티티들
     */
    void deleteAll(Collection<?> entities);

    /**
     * 지정한 Id를 가진 엔티티의 인덱스 정보를 삭제합니다.
     *
     * @param clazz 대상 엔티티의 수형
     * @param id    엔티티의 Id
     */
    void purge(Class<?> clazz, Serializable id);

    /**
     * 지정한 수형의 인덱스 정보를 삭제합니다.
     *
     * @param clazz 엔티티 수형
     */
    void purgeAll(Class<?> clazz);

    /**
     * 엔티티를 수동으로 재 인덱싱합니다.<br/>
     * Force the (re)indexing of a given <b>managed</b> object.
     *
     * @param entity 인덱싱할 엔티티
     */
    <T> void index(T entity);

    /**
     * 지정된 수형의 모든 엔티티들을 인덱싱 합니다.
     *
     * @param clazz     대상 엔티티
     * @param batchSize 한번에 인덱싱할 크기
     */
    void indexAll(Class<?> clazz, int batchSize);

    /**
     * 해당 수형의 모든 인덱스를 비동기 방식으로 구성합니다.
     *
     * @param clazz     대상 인덱스
     * @param batchSize 한번에 인덱싱할 크기
     * @return {@link Future} 인스턴스
     */
    Future<Void> indexAllAsync(Class<?> clazz, int batchSize);

    /**
     * 해당 수형의 모든 인덱스 정보를 삭제합니다.
     *
     * @param clazz 대상 엔티티
     */
    void clearIndex(Class<?> clazz);

    /** 모든 인덱스를 삭제합니다. */
    void clearIndexAll();

    /**
     * 해당 수형의 인덱스를 최적화합니다.
     *
     * @param clazz 대상 엔티티 수형
     */
    void optimize(Class<?> clazz);

    /** 모든 엔티티의 인덱스를 최적화합니다. */
    void optimizeAll();

    /** 세션의 모든 변경을 저장소에 적용한다. */
    void flush();

    /** 세션의 모든 인덱스 변경 정보를 저장합니다. */
    void flushToIndexes();
}
