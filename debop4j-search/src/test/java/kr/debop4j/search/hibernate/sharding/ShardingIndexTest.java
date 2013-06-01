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

package kr.debop4j.search.hibernate.sharding;

import kr.debop4j.search.hibernate.model.Dvd;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.debop4j.search.hibernate.sharding.ShardingIndexTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 25. 오전 10:48
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ShardingConfig.class })
public class ShardingIndexTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    SessionFactory sessionFactory;
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

    @Test
    public void shardingTest() {
        Transaction tx = fts.beginTransaction();

        Dvd dvd = new Dvd();
        dvd.setId(1);
        dvd.setTitle("헬보이");
        dvd.setDescription("헬보이와 아쿠아맨");
        fts.persist(dvd);

        dvd = new Dvd();
        dvd.setId(2);
        dvd.setTitle("프레데터");
        dvd.setDescription("프레데터 2");
        fts.persist(dvd);

        tx.commit();
        fts.clear();

        for (Object element : fts.createQuery("from " + Dvd.class.getName()).list()) {
            fts.delete(element);
        }
    }
}
