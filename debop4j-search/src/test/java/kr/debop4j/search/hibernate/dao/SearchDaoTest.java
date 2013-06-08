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

package kr.debop4j.search.hibernate.dao;

import kr.debop4j.core.Action1;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkNestingOptions;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.search.dao.SearchDao;
import kr.debop4j.search.hibernate.SearchTestBase;
import kr.debop4j.search.hibernate.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * com.kthcorp.daisy.search.dao.SearchDaoTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 5. 오전 1:27
 */
@Slf4j
public class SearchDaoTest extends SearchTestBase {

    @Autowired
    SearchDao searchDao;


    @After
    public void after() {
        searchDao.deleteAll(Document.class);
        searchDao.flush();
        searchDao.clearIndex(Document.class);
    }

    @Test
    public void createDao() throws Exception {
        assertThat(searchDao).isNotNull();
    }

    @Test
    public void crud() throws Exception {

        Document document = createDocument();
        searchDao.saveOrUpdate(document);
        searchDao.getSession().flush();
        searchDao.getSession().clear();

        Document loaded = searchDao.get(Document.class, document.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getRowId()).isEqualTo(document.getRowId());

        searchDao.delete(loaded);
        searchDao.flush();
        searchDao.getSession().clear();

        loaded = searchDao.get(Document.class, document.getId());
        assertThat(loaded).isNull();
    }

    @Test
    public void countTest() throws Exception {
        Document document = createDocument();
        searchDao.saveOrUpdate(document);
        searchDao.getSession().flush();
        searchDao.getSession().clear();

        QueryBuilder builder = searchDao.getQueryBuilder(Document.class);
        Query query =
                builder.bool()
                        .must(builder.keyword().onField("attrs.name").matching("담당").createQuery())
                        .must(builder.keyword().onField("attrs.value").matching("송길주").createQuery())
                        .must(builder.keyword().wildcard().onField("body").matching("백두*").createQuery())
                        .createQuery();

        int count = searchDao.count(Document.class, query);
        assertThat(count).isGreaterThan(0);

        IPagedList<Document> documents = searchDao.getPage(Document.class, query, 1, 10);
        assertThat(documents.getList().size()).isGreaterThan(0);
    }

    @Test
    public void searchQueryTest() throws Exception {
        Action1<SearchDao> searchQueryAction =
                new Action1<SearchDao>() {
                    @Override
                    public void perform(SearchDao dao) {

                        // findAll
                        List<Document> loadedDocuments = dao.findAll(Document.class);
                        assertThat(loadedDocuments).isNotNull();
                        assertThat(loadedDocuments.size()).isGreaterThan(0);
                        log.debug("findAll seach result = [{}]", loadedDocuments.size());

                        QueryBuilder builder = dao.getQueryBuilder(Document.class);
                        Query query =
                                builder.bool()
                                        .must(builder.keyword().onField("attrs.name").matching("담당").createQuery())
                                        .must(builder.keyword().onField("attrs.value").matching("송길주").createQuery())
                                        .must(builder.keyword().wildcard().onField("body").matching("백두*").createQuery())
                                        .createQuery();
                        loadedDocuments = (List<Document>) dao.findAll(Document.class, query, null, null);
                        assertThat(loadedDocuments).isNotNull();
                        assertThat(loadedDocuments.size()).isGreaterThan(0);

                        IPagedList<Document> documents = dao.getPage(Document.class, query, 2, 10);
                        assertThat(documents.getList().size()).isEqualTo(10);

                        builder = dao.getQueryBuilder(Document.class);
                        query =
                                builder.bool()
                                        .must(builder.keyword().onField("attrs.name").matching("담당").createQuery())
                                        .must(builder.keyword().onField("attrs.value").matching("송길").createQuery())
                                        .must(builder.keyword().wildcard().onField("body").matching("나눔*").createQuery())
                                        .createQuery();
                        loadedDocuments = (List<Document>) dao.findAll(Document.class, query, null, null);
                        assertThat(loadedDocuments).isNotNull();
                        assertThat(loadedDocuments.size()).isEqualTo(0);
                    }
                };

        log.info("순차방식으로 엔티티를 추가합니다...");
        daoInSerial(searchQueryAction);

        log.info("병렬방식으로 엔티티를 추가합니다...");
        daoInParallel(searchQueryAction);
    }

    public void daoInSerial(Action1<SearchDao> action) throws Exception {

        for (int i = 0; i < REPEAT_COUNT; i++) {
            List<Document> documents = createDocuments(DOCUMENT_COUNT);
            for (Document document : documents) {
                searchDao.persist(document);
            }
            searchDao.getSession().flush();
            Thread.sleep(10);
            searchDao.flushToIndexes();
            Thread.sleep(10);
            searchDao.getSession().clear();
        }
        log.debug("Document [{}]개를 추가했습니다.", REPEAT_COUNT * DOCUMENT_COUNT);

        try {
            action.perform(searchDao);
        } finally {
            log.debug("Document를 삭제합니다...");
            List<Document> documents = searchDao.findAll(Document.class);
            assertThat(documents.size()).isGreaterThan(0);
            searchDao.deleteAll(documents);
            searchDao.getFullTextSession().flush();
            searchDao.flushToIndexes();
            searchDao.getFullTextSession().close();
        }
        assertThat(searchDao.count(Document.class)).isEqualTo(0);
    }

    public void daoInParallel(Action1<SearchDao> action) throws Exception {

        Parallels.run(REPEAT_COUNT, new Action1<Integer>() {
            @Override
            public void perform(Integer arg) {
                try (IUnitOfWork unitOfWork = UnitOfWorks.start(UnitOfWorkNestingOptions.CreateNewOrNestUnitOfWork)) {
                    List<Document> documents = createDocuments(DOCUMENT_COUNT);

                    for (Document document : documents) {
                        searchDao.persist(document);
                    }
                    searchDao.getSession().flush();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {}
                    /**
                     * 병렬 작업 시에는 flushToIndexes() 메소드를 호출하여,
                     * session이 닫히거나 스레드가 중단되기 전에 인덱싱을 마무리하도록 한다.
                     */
                    searchDao.flushToIndexes();
                    searchDao.getSession().close();

                    log.debug("Document [{}]명을 추가했습니다.", documents.size());
                } catch (Exception e) {
                    log.error("예외 발생", e);
                }
            }
        });

        searchDao.flushToIndexes();

        try {
            action.perform(searchDao);
        } finally {
            log.debug("Document 엔티티를 삭제합니다...");
            List<Document> documents = searchDao.findAll(Document.class);
            assertThat(documents.size()).isGreaterThan(0);
            searchDao.deleteAll(documents);
            searchDao.getFullTextSession().flush();
            searchDao.flushToIndexes();
            searchDao.getFullTextSession().close();
        }
        assertThat(searchDao.count(Document.class)).isEqualTo(0);
    }
}
