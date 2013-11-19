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

package kr.debop4j.data.jpa.repository;

import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.jpa.domain.JpaEntityBase;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * JPA 용 Repository 입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 6. 20. 오전 11:10
 */
public interface JpaRepository<E extends JpaEntityBase> {

    /**
     * {@link EntityManager} 인스턴스
     */
    EntityManager getEntityManager();

    /**
     * 엔티티 수형
     */
    Class<E> getEntityClass();

    /**
     * EntityManager의 변경을 DB에 적용합니다.
     */
    void flush();

    /**
     * EntityManager의 변경을 Transaction을 이용하여 DB에 적용합니다.
     */
    void transactionalFlush();

    /**
     * 특정 수형의 해당 id 값을 가지는 엔티티를 로드합니다. (실제 로드하는 것이 아니라, proxy로 로드하는 것이다)
     * 주의: 실제 데이터가 없을 때에는 예외가 발생합니다.
     */
    <E> E load(Serializable id);

    /**
     * 특정 수형의 해당 id 값을 가지는 엔티티를 로드합니다. 없을 때에는 null을 반환합니다.
     *
     * @param id identifier 값
     * @return 엔티티, 없으면 null
     */
    <E> E get(Serializable id);

    /**
     * 특정 수형의 해당 id 들을 가지는 엔티티들을 로드합니다.
     */
    <E> List<E> getIn(Collection<? extends Serializable> ids);

    /**
     * 특정 수형의 해당 id 들을 가지는 엔티티들을 로드합니다.
     */
    <E> List<E> getIn(Serializable[] ids);


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
     * @param criteria 질의 조건
     */
    void deleteAll(Query criteria);

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
}
