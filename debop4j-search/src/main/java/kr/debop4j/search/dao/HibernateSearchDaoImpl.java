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
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import java.util.List;

/**
 * hibernate-search 를 이용하여 엔티티를 관리하는 Data Access Object 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 20. 오후 10:03
 */
@Slf4j
@SuppressWarnings("unchecked")
public class HibernateSearchDaoImpl {

    private static final String FULL_TEXT_SESSION_KEY = "kr.debop4j.search.dao.HibernateSearchDao.FullTextSession";

    public synchronized FullTextSession getFullTextSession() {
        FullTextSession fts = (FullTextSession) Local.get(FULL_TEXT_SESSION_KEY);
        if (fts == null) {
            fts = Search.getFullTextSession(UnitOfWorks.getCurrentSession());
            Local.put(FULL_TEXT_SESSION_KEY, fts);
            if (log.isDebugEnabled())
                log.debug("Current Thread Context에 새로운 FullTextSession을 생성했습니다. threadName=[{}]",
                          Thread.currentThread().getName());
        }
        return fts;
    }

    public <T> List<T> find(Query luceneQuery, Class<T> clazz) {
        FullTextQuery ftq = getFullTextSession().createFullTextQuery(luceneQuery, clazz);

        return (List<T>) ftq.list();
    }
}
