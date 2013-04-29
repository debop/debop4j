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

package kr.debop4j.search.hibernate;

import kr.debop4j.search.AppConfig;
import kr.debop4j.search.dao.HibernateSearchDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.debop4j.search.hibernate.SearchTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 23. 오후 9:25
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public abstract class SearchTestBase {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    protected SessionFactory sessionFactory;

    protected FullTextSession fts;

    @Before
    public void before() {
        fts = Search.getFullTextSession(sessionFactory.openSession());
    }

    @After
    public void after() {
        if (fts != null) {
            fts.flush();
            fts.flushToIndexes();

            fts.close();
            fts = null;
        }
    }

    public HibernateSearchDao getSearchDao() {
        return appContext.getBean(HibernateSearchDao.class);
    }
}
