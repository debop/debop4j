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
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;

/**
 * kr.debop4j.search.dao.IHibernateSearchDao
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 26. 오전 9:55
 */
public interface IHibernateSearchDao extends IHibernateDao {

    Session getSession();

    FullTextSession getFullTextSession();

    /** 지정한 형식에 대한 질의 빌더를 생성합니다. */
    QueryBuilder getQueryBuilder(Class<?> clazz);

    FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... classes);

    <T> List<T> find(Class<T> clazz, Query luceneQuery, Sort sort);

    <T> List<T> find(Class<T> clazz, Query luceneQuery, int firstResult, int maxResults, Sort sort);

    List<Object[]> find(Class<?> clazz, Query luceneQuery, int firstResult, int maxResults, Criteria criteria, Sort sort);

    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort);

    <T> IPagedList<T> getPage(Class<T> clazz, Query luceneQuery, int pageNo, int pageSize, Criteria criteria, Sort sort);

    /** 지정한 쿼리를 수행하여 해당 엔티티의 ID 값만 가져옵니다. */
    IPagedList<Serializable> getIds(Class<?> clazz, Query luceneQuery, int pageNo, int pageSize, Sort sort);

    /** 지정한 쿼리를 수행하여 해당 필드의 값들만 뽑아온다. */
    IPagedList getProjectionPage(Class<?> clazz, Query luceneQuery, String[] fields, int pageNo, int pageSize, Sort sort);

    long count(Class<?> clazz);

    long count(Class<?> clazz, Query luceneQuery);

    /** 해당 엔티티의 인덱스 정보만을 삭제합니다. */
    void purge(Class<?> clazz, Serializable id);

    /** 지정된 수형의 인덱싱 정보를 삭제합니다. (DB의 엔티티 정보는 보존합니다.) */
    void purgeAll(Class<?> clazz);

    /** 엔티티를 인덱싱합니다. */
    <T> void index(T entity);

    /** 지정된 수형의 모든 엔티티들을 인덱싱 합니다. */
    void indexAll(Class<?> clazz, int batchSize);

    Future<Void> indexAllAsync(Class<?> clazz, int batchSize);

    void flushIndexes();

    void clearIndex(Class<?> clazz);

    /** 해당 수형의 인덱스를 최적화합니다. */
    void optimize(Class<?> clazz);

    void optimizeAll();
}
