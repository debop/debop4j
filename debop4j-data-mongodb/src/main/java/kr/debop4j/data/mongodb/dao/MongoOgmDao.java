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

package kr.debop4j.data.mongodb.dao;

import kr.debop4j.data.ogm.dao.HibernateOgmDao;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * MongoDB를 저장소로 사용하는 hibernate-ogm Data Access Object 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오전 11:14
 */
@Component
@SuppressWarnings("unchecked")
public class MongoOgmDao extends HibernateOgmDao {

    private static final Logger log = LoggerFactory.getLogger(MongoOgmDao.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    public MongoOgmDao() {}

    public MongoOgmDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public MongoOgmDao(Session session) {
        super(session);
    }

    @Override
    public long count(Class<?> clazz) {
        return count(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    @Override
    public long count(Class<?> clazz, Query luceneQuery) {
        if (isTraceEnabled)
            log.trace("수형[{}]에 대해 갯수를 구합니다. luceneQuery=[{}]", clazz, luceneQuery);

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        return ftq.getResultSize();
    }

    /**
     * 지정된 수형의 엔티티를 모두 삭제합니다.
     */
    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(findAll(clazz));
    }
}