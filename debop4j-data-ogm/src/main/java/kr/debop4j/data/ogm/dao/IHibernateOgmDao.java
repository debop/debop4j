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
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 26. 오전 9:50
 */
public interface IHibernateOgmDao {
    /** hibernate session을 반환합니다. */
    Session getSession();

    /** hibernate-search의 {@link org.hibernate.search.FullTextSession} 을 반환합니다. */
    FullTextSession getFullTextSession();

    /** 지정한 형식에 대한 질의 빌더를 생성합니다. */
    QueryBuilder getQueryBuilder(Class<?> clazz);

    FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entities);

    <T> T get(Class<T> clazz, Serializable id);

    /** 엔티티 수형에 해당하는 모든 엔티티를 조회합니다. */
    <T> List<T> findAll(Class<T> clazz);

    /** 엔티티 수형에 해당하는 모든 엔티티를 조회합니다. */
    <T> List<T> findAll(Class<T> clazz, Sort luceneSort);

    /** 조회 */
    <T> List<T> find(Class<T> clazz, Query luceneQuery);

    /** 조회 */
    <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort sort);

    /** 조회 */
    <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort);

    /** 조회 */
    <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort, Criteria criteria);

    /** 페이징 조회 */
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort);

    /** 페이징 조회 */
    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria);

    /** 엔티티의 Id를 페이징 조회 */
    IPagedList<Serializable> getIdPage(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort, Criteria criteria);

    /** 루씬 부가 정보를 페이징 조회합니다. (FullTextQuery.ID, FullTextQuery.DOCUMENT_ID, FullTextQuery.SCORE 등) */
    IPagedList<Object[]> getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort sort, Criteria criteria);

    long count(Class<?> clazz);

    long count(Class<?> clazz, Query luceneQuery);

    void persist(Object entity);

    Object merge(Object entity);

    Serializable save(Object entity);

    void saveOrUpdate(Object entity);

    void update(Object entity);

    void delete(Object entity);

    void deleteById(Class<?> clazz, Serializable id);

    void deleteAll(Class<?> clazz);

    void deleteAll(Collection<?> entities);

    /** 해당 엔티티의 인덱스 정보를 제거합니다. */
    void purge(Class<?> clazz, Serializable id);

    /** 지정된 수형의 모든 엔티티들의 인덱스 정보를 제거합니다. */
    void purgeAll(Class<?> clazz);

    /** Session에 남아있는 인덱싱 작업을 강제로 수행하도록 합니다. */
    void flushToIndexes();

    /**
     * 엔티티를 수동으로 재 인덱싱합니다.<br/>
     * Force the (re)indexing of a given <b>managed</b> object.
     */
    <T> void index(T entity);

    /** 지정된 수형의 모든 엔티티들을 인덱싱 합니다. */
    void indexAll(Class<?> clazz, int batchSize);

    /** 해당 수형의 모든 인덱스를 비동기 방식으로 구성합니다. */
    Future<Void> indexAllAsync(Class<?> clazz, int batchSize);

    /** 해당 수형의 모든 인덱스 정보를 삭제합니다. */
    void clearIndex(Class<?> clazz);

    /** 모든 인덱스를 삭제합니다. */
    void clearIndexAll();

    /** 해당 수형의 인덱스를 최적화합니다. */
    void optimize(Class<?> clazz);

    /** 모든 엔티티의 인덱스를 최적화합니다. */
    void optimizeAll();

    /** 세션의 모든 변경을 저장소에 적용한다. */
    void flush();

    /** 세션의 모든 인덱스 변경 정보를 저장합니다. */
    void flushIndexes();
}
