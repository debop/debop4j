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

import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.search.AppConfig;
import kr.debop4j.search.dao.IHibernateSearchDao;
import kr.debop4j.search.hibernate.model.Document;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * kr.debop4j.search.hibernate.SearchTestBase
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
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
        UnitOfWorks.start();
        fts = Search.getFullTextSession(UnitOfWorks.getCurrentSession());
    }

    @After
    public void after() {
        if (fts != null) {
            fts.flush();
            fts.flushToIndexes();
        }
        UnitOfWorks.stop();
    }

    public IHibernateSearchDao getSearchDao() {
        return appContext.getBean(IHibernateSearchDao.class);
    }

    public static final Random rnd = new Random();
    public static final int REPEAT_COUNT = 50;
    public static final int DOCUMENT_COUNT = 20;

    public static final String DefaultBody =
            "1. 동해물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세." +
                    "   (후렴) 무궁화 삼천리 화려 강산, 대한 사람 대한으로 길이 보전하세." +
                    "2. 남산 위에 저 소나무 철갑을 두른 듯, 바람 서리 불변함은 우리 기상일세." +
                    "3. 가을 하늘 공활한데 높고 구름 없이, 밝은 달은 우리 가슴 일편단심일세." +
                    "4. 이 기상과 이 맘으로 충성을 다하여, 괴로우나 즐거우나 나라 사랑하세.";

    public Document createDocument() {
        Document document = new Document("Id-" + String.valueOf(rnd.nextInt()));
        document.setBody(DefaultBody);
        document.addAttr("담당", "송길주");
        document.addAttr("상태", "처리중");
        return document;
    }

    public List<Document> createDocuments(int count) {
        List<Document> documents = new ArrayList<Document>(count);
        for (int i = 0; i < count; i++) {
            Document document = new Document("이름-" + String.valueOf(i));
            document.setBody(DefaultBody);
            document.addAttr("담당", "송길주");
            document.addAttr("상태", "처리중");
            documents.add(document);
        }
        return documents;
    }
}
