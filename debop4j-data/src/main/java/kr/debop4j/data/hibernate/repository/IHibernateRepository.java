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

package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.model.IStatefulEntity;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Hibernate 용 Data Access Object Interface 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 27.
 */
@Transactional
public interface IHibernateRepository<E extends IStatefulEntity> {

    Class<E> getEntityClass();

    /**
     * Current thread context 에서 사용하는 Session을 반환합니다.
     * {@link kr.debop4j.data.hibernate.unitofwork.UnitOfWorks#start} 가 먼저 수행되어 있어야 합니다.
     *
     * @return the session
     */
    Session getSession();

    /**
     * Session의 변경을 DB에 적용합니다.
     */
    void flushSession();

    /**
     * Session의 변경을 Transaction을 이용하여 DB에 적용합니다.
     */
    void transactionalFlush();

    /**
     * 특정 수형의 해당 id 값을 가지는 엔티티를 로드합니다. (실제 로드하는 것이 아니라, proxy로 로드하는 것이다)
     * 주의: 실제 데이터가 없을 때에는 예외가 발생합니다.
     */
    <E> E load(Serializable id);

    /**
     * 특정 수형의 해당 id 값을 가지는 엔티티를 로드합니다. (실제 로드하는 것이 아니라, proxy로 로드하는 것이다)
     * 주의: 실제 데이터가 없을 때에는 예외가 발생합니다.
     */
    <E> E load(Serializable id, LockOptions lockOptions);

    /**
     * 특정 수형의 해당 id 값을 가지는 엔티티를 로드합니다. 없을 때에는 null을 반환합니다.
     *
     * @param id identifier 값
     * @return 엔티티, 없으면 null
     */
    <E> E get(Serializable id);

    /**
     * 특정 수형의 해당 id 값을 가지는 엔티티를 로드합니다. 없을 때에는 null을 반환합니다.
     *
     * @param id identifier 값
     * @return 엔티티, 없으면 null
     */
    <E> E get(Serializable id, LockOptions lockOptions);

    /**
     * 특정 수형의 해당 id 들을 가지는 엔티티들을 로드합니다.
     */
    <E> List<E> getIn(Collection<? extends Serializable> ids);

    /**
     * 특정 수형의 해당 id 들을 가지는 엔티티들을 로드합니다.
     */
    <E> List<E> getIn(Serializable[] ids);

    /**
     * 질의의 결과를 {@link ScrollableResults} 로 반환합니다.
     *
     * @param dc 질의 정보
     * @return {@link  ScrollableResults} 인스턴스
     */
    ScrollableResults getScroll(DetachedCriteria dc);

    /**
     * 질의의 결과를 {@link ScrollableResults} 로 반환합니다.
     *
     * @param dc         질의 정보
     * @param scrollMode the scroll mode
     * @return 검색 결과 Scollable Resutls
     */
    ScrollableResults getScroll(DetachedCriteria dc, ScrollMode scrollMode);

    /**
     * 질의의 결과를 {@link ScrollableResults} 로 반환합니다.
     *
     * @param criteria 질의
     * @return 검색 결과 Scollable Resutls
     */
    ScrollableResults getScroll(Criteria criteria);

    /**
     * 질의의 결과를 {@link ScrollableResults} 로 반환합니다.
     *
     * @param criteria   질의
     * @param scrollMode scroll mode
     * @return 검색 결과 Scollable Resutls
     */
    ScrollableResults getScroll(Criteria criteria, ScrollMode scrollMode);

    /**
     * 질의의 결과를 {@link ScrollableResults} 로 반환합니다.
     *
     * @param query      the query
     * @param parameters the parameters
     * @return the scroll
     */
    ScrollableResults getScroll(Query query, HibernateParameter... parameters);

    /**
     * 질의의 결과를 {@link ScrollableResults} 로 반환합니다.
     *
     * @param query      the query
     * @param scrollMode the scroll mode
     * @param parameters the parameters
     * @return the scroll
     */
    ScrollableResults getScroll(Query query, ScrollMode scrollMode, HibernateParameter... parameters);

    /**
     * 모든 엔티티를 조회합니다.
     *
     * @param orders 정렬
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> findAll(Order... orders);

    /**
     * 모든 엔티티를 필터링 없이 Paging 처리하여 반환합니다.
     *
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param orders      정렬
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> findAll(int firstResult, int maxResults, Order... orders);


    /**
     * 해당 엔티티에 대한 조회를 수행합니다.
     *
     * @param criteria 질의 정보
     * @param orders   정렬
     * @param <E>      엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(Criteria criteria, Order... orders);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param criteria    질의 정보
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param orders      정렬
     * @param <E>         엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(Criteria criteria, int firstResult, int maxResults, Order... orders);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param orders 정렬
     * @param <E>    엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(DetachedCriteria dc, Order... orders);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param dc          질의 정보
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param orders      정렬
     * @param <E>         엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(DetachedCriteria dc, int firstResult, int maxResults, Order... orders);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param query      질의 정보
     * @param parameters hibernate parameters
     * @param <E>        엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(Query query, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param query       질의 정보
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param parameters  hibernate parameters
     * @param <E>         엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(Query query, int firstResult, int maxResults, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param hql        HQL 문자열
     * @param parameters hibernate parameters
     * @param <E>        엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(String hql, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param hql         HQL 문자열
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param parameters  hibernate parameters
     * @param <E>         엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> find(String hql, int firstResult, int maxResults, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param queryName  name of Named Query
     * @param parameters hibernate parameters
     * @param <E>        엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> findByNamedQuery(String queryName, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param queryName   name of Named Query
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param parameters  hibernate parameters
     * @param <E>         엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> findByNamedQuery(String queryName, int firstResult, int maxResults, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param sqlString  SQL String
     * @param parameters hibernate parameters
     * @param <E>        엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> findBySQLString(String sqlString, HibernateParameter... parameters);

    /**
     * 엔티티에 대한 조회를 수행합니다.
     *
     * @param sqlString   SQL String
     * @param firstResult 첫번째 인덱스 (0부터 시작, OFFSET)
     * @param maxResults  최대 갯수 (LIMITS)
     * @param parameters  hibernate parameters
     * @param <E>         엔티티 수형
     * @return 조회한 엔티티의 컬렉션
     */
    <E> List<E> findBySQLString(String sqlString, int firstResult, int maxResults, HibernateParameter... parameters);

    /**
     * {@link Example}로 조회를 수행합니다.
     *
     * @param example {@link Example} 인스턴스
     * @param <E>     엔티티 수형
     * @return 조회된 엔티티 컬렉션
     */
    <E> List<E> findByExample(Example example);

    /**
     * 조회 결과를 페이징 처리해서 가져온다.
     *
     * @param criteria 질의 정보
     * @param pageNo   페이지 번호 (1부터 시작)
     * @param pageSize 페이지 크기 (보통 10)
     * @param orders   정렬 방법
     * @param <E>      엔티티 수형
     * @return 페이징 처리된 조회 결과
     */
    <E> IPagedList<E> getPage(Criteria criteria, int pageNo, int pageSize, Order... orders);

    /**
     * 조회 결과를 페이징 처리해서 가져온다.
     *
     * @param dc       질의 정보
     * @param pageNo   페이지 번호 (1부터 시작)
     * @param pageSize 페이지 크기 (보통 10)
     * @param orders   정렬 방법
     * @param <E>      엔티티 수형
     * @return 페이징 처리된 조회 결과
     */
    <E> IPagedList<E> getPage(DetachedCriteria dc, int pageNo, int pageSize, Order... orders);

    /**
     * 조회 결과를 페이징 처리해서 가져온다.
     *
     * @param query      질의 정보
     * @param pageNo     페이지 번호 (1부터 시작)
     * @param pageSize   페이지 크기 (보통 10)
     * @param parameters 질의용 인자 정보
     * @param <E>        엔티티 수형
     * @return 페이징 처리된 조회 결과
     */
    <E> IPagedList<E> getPage(Query query, int pageNo, int pageSize, HibernateParameter... parameters);

    /**
     * 조회 결과를 페이징 처리해서 가져온다.
     *
     * @param hql        질의 정보
     * @param pageNo     페이지 번호 (1부터 시작)
     * @param pageSize   페이지 크기 (보통 10)
     * @param parameters 질의용 인자 정보
     * @param <E>        엔티티 수형
     * @return 페이징 처리된 조회 결과
     */
    <E> IPagedList<E> getPageByHql(final String hql, int pageNo, int pageSize, HibernateParameter... parameters);

    /**
     * 조회 결과를 페이징 처리해서 가져온다.
     *
     * @param queryName  쿼리 명
     * @param pageNo     페이지 번호 (1부터 시작)
     * @param pageSize   페이지 크기 (보통 10)
     * @param parameters 질의용 인자 정보
     * @param <E>        엔티티 수형
     * @return 페이징 처리된 조회 결과
     */
    <E> IPagedList<E> getPageByNamedQuery(final String queryName, int pageNo, int pageSize, HibernateParameter... parameters);

    /**
     * 조회 결과를 페이징 처리해서 가져온다.
     *
     * @param sqlString  일반 SQL 문자열
     * @param pageNo     페이지 번호 (1부터 시작)
     * @param pageSize   페이지 크기 (보통 10)
     * @param parameters 질의용 인자 정보
     * @param <E>        엔티티 수형
     * @return 페이징 처리된 조회 결과
     */
    <E> IPagedList<E> getPageBySQLString(final String sqlString, int pageNo, int pageSize, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param dc  조회 조건
     * @param <E> 엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findUnique(DetachedCriteria dc);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param criteria 조회 조건
     * @param <E>      엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findUnique(Criteria criteria);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param query 조회 조건
     * @param <E>   엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findUnique(Query query, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param hql 조회 조건
     * @param <E> 엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findUniqueByHql(String hql, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param queryName NamedQuery의 name
     * @param <E>       엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findUniqueByNamedQuery(String queryName, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param sqlString 일반 SQL 문
     * @param <E>       엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findUniqueBySQLString(String sqlString, HibernateParameter... parameters);

    /**
     * 질의 조건에 만족하는 첫번째 엔티티를 반환합니다.
     *
     * @param dc     질의 조건
     * @param orders 정렬 방식
     * @param <E>    엔티티 수형
     * @return 첫번째 엔티티
     */
    <E> E findFirst(DetachedCriteria dc, Order... orders);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param criteria 조회 조건
     * @param <E>      엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findFirst(Criteria criteria, Order... orders);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param query 조회 조건
     * @param <E>   엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findFirst(Query query, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param hql 조회용 HQL 문장
     * @param <E> 엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findFirstByHql(String hql, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param queryName 조회용 쿼리명
     * @param <E>       엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findFirstByNamedQuery(String queryName, HibernateParameter... parameters);

    /**
     * 지정한 엔티티에 대한 유일한 결과를 조회합니다. (결과가 없거나, 복수이면 예외가 발생합니다.
     *
     * @param sqlString 일반 SQL 문자열
     * @param <E>       엔티티 수형
     * @return 조회된 엔티티
     */
    <E> E findFirstBySQLString(String sqlString, HibernateParameter... parameters);

    /**
     * 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @return 엔티티 존재 여부
     */
    boolean exists();

    /**
     * 조회 조건에 해당하는 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @param dc 조회 조건
     * @return 엔티티 존재 여부
     */
    boolean exists(DetachedCriteria dc);

    /**
     * 조회 조건에 해당하는 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @param criteria 조회 조건
     * @return 엔티티 존재 여부
     */
    boolean exists(Criteria criteria);

    /**
     * 조회 조건에 해당하는 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @param query 조회 조건
     * @return 엔티티 존재 여부
     */
    boolean exists(Query query, HibernateParameter... parameters);

    /**
     * 조회 조건에 해당하는 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @param hql 조회 조건
     * @return 엔티티 존재 여부
     */
    boolean existsByHql(final String hql, HibernateParameter... parameters);

    /**
     * 조회 조건에 해당하는 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @param queryName 쿼리명
     * @return 엔티티 존재 여부
     */
    boolean existsByNamedQuery(final String queryName, HibernateParameter... parameters);

    /**
     * 조회 조건에 해당하는 엔티티 정보가 존재하는지를 파악합니다.
     *
     * @param sqlString 일반 SQL 문
     * @return 엔티티 존재 여부
     */
    boolean existsBySQLString(final String sqlString, HibernateParameter... parameters);

    /**
     * 해당 엔티티의 갯수를 구합니다.
     *
     * @return 해당 엔티티의 갯수
     */
    long count();

    /**
     * 질의 조건에 해당하는 엔티티의 갯수를 구합니다.
     *
     * @param criteria 질의 조건
     * @return 해당 엔티티의 갯수
     */
    long count(Criteria criteria);

    /**
     * 질의 조건에 해당하는 엔티티의 갯수를 구합니다.
     *
     * @param dc 질의 조건
     * @return 해당 엔티티의 갯수
     */
    long count(DetachedCriteria dc);

    /**
     * 질의 조건에 해당하는 엔티티의 갯수를 구합니다.
     *
     * @param query      질의 조건
     * @param parameters 질의 인자 값
     * @return 해당 엔티티의 갯수
     */
    long count(Query query, HibernateParameter... parameters);

    /**
     * 엔티티를 Session에 추가합니다. 기존에 있다면 update하고, 없으면 save 합니다.
     *
     * @param entity 엔티티
     * @return update 된 엔티티
     */
    Object merge(Object entity);

    /**
     * 엔티티를 저장합니다.
     *
     * @param entity 저장할 Transient Object
     */
    void persist(Object entity);

    /**
     * 엔티티를 저장합니다.
     *
     * @param entity 저장할 Transient Object
     * @return identifier of entity.
     */
    Serializable save(Object entity);

    /**
     * 엔티티를 저장하거나 Update 합니다.
     *
     * @param entity 엔티티
     */
    void saveOrUpdate(Object entity);

    /**
     * 엔티티를 Update 합니다.
     *
     * @param entity 엔티티
     */
    void update(Object entity);

    /**
     * 해당 엔티티를 삭제합니다. 만약 cascading 이 지정되어 있다면 cascading delete 를 수행합니다.
     *
     * @param entity 삭제할 엔티티
     */
    void delete(Object entity);

    /**
     * 지정한 Id값을 가진 엔티티를 삭제합니다. 만약 cascading 이 지정되어 있다면 cascading delete 를 수행합니다.
     *
     * @param id 삭제할 엔티티의 id 값
     */
    void deleteById(Serializable id);

    /**
     * 해당 수형의 모든 엔티티를 삭제합니다.
     */
    void deleteAll();

    /**
     * 해당 수형의 모든 엔티티를 삭제합니다.
     *
     * @param entities 삭제할 엔티티의 컬렉션
     */
    void deleteAll(Collection<?> entities);

    /**
     * 질의 조건에 해당하는 모든 엔티티를 삭제합니다.
     *
     * @param dc 질의 조건
     */
    void deleteAll(DetachedCriteria dc);

    /**
     * 질의 조건에 해당하는 모든 엔티티를 삭제합니다.
     *
     * @param criteria 질의 조건
     */
    void deleteAll(Criteria criteria);

    /**
     * Cascade 적용 없이 엔티티들을 모두 삭제합니다.
     */
    int deleteAllWithoutCascade();

    /**
     * 지정한 HQL 구문 (insert, update, delete) 을 수행합니다.
     *
     * @param hql        수행할 HQL 구문
     * @param parameters 인자 정보
     * @return 실행에 영향 받은 행의 수
     */
    int executeUpdateByHql(final String hql, HibernateParameter... parameters);

    /**
     * 지정한 name의 NamedQuery (insert, update, delete) 을 수행합니다.
     *
     * @param queryName  수행할 NamedQuery의 Name
     * @param parameters 인자 정보
     * @return 실행에 영향 받은 행의 수
     */
    int executeUpdateByNamedQuery(final String queryName, HibernateParameter... parameters);

    /**
     * 지정한 HQL 구문 (insert, update, delete) 을 수행합니다.
     *
     * @param sqlString  수행할 HQL 구문
     * @param parameters 인자 정보
     * @return 실행에 영향 받은 행의 수
     */
    int executeUpdateBySQLString(final String sqlString, HibernateParameter... parameters);

    /**
     * Projection을 수행하여, 하나의 엔티티를 반환합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param dc             질의 정보
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과
     */
    <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, DetachedCriteria dc);

    /**
     * Projection을 수행하여, 하나의 엔티티를 반환합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param criteria       질의 정보
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과
     */
    <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, Criteria criteria);

    /**
     * Projection을 수행합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param dc             질의 정보
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과 컬렉션
     */
    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         DetachedCriteria dc);

    /**
     * Projection을 수행합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param dc             질의 정보
     * @param firstResult    첫번째 결과 인덱스 (0부터 시작)
     * @param maxResults     최대 결과 갯수
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과 컬렉션
     */
    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         DetachedCriteria dc,
                                         int firstResult,
                                         int maxResults);

    /**
     * Projection을 수행합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param criteria       질의 정보
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과 컬렉션
     */
    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         Criteria criteria);

    /**
     * Projection을 수행합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param criteria       질의 정보
     * @param firstResult    첫번째 결과 인덱스 (0부터 시작)
     * @param maxResults     최대 결과 갯수
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과 컬렉션
     */
    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         Criteria criteria,
                                         int firstResult,
                                         int maxResults);

    /**
     * Projection을 수행합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param dc             질의 정보
     * @param pageNo         page number. (1부터 시작)
     * @param pageSize       page size.
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과 컬렉션
     */
    <TProject> IPagedList<TProject> reportPage(Class<TProject> projectClass,
                                               ProjectionList projectionList,
                                               DetachedCriteria dc,
                                               int pageNo,
                                               int pageSize);

    /**
     * Projection을 수행합니다.
     *
     * @param projectClass   Projection 결과 클래스
     * @param projectionList Projection 정보
     * @param criteria       질의 정보
     * @param pageNo         page number. (1부터 시작)
     * @param pageSize       page size.
     * @param <TProject>     Projection 결과 엔티티의 수형
     * @return Projection 결과 컬렉션
     */
    <TProject> IPagedList<TProject> reportPage(Class<TProject> projectClass,
                                               ProjectionList projectionList,
                                               Criteria criteria,
                                               int pageNo,
                                               int pageSize);
}
