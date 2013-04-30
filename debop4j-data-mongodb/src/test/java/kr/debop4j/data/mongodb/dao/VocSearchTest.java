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

import kr.debop4j.core.Action1;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.model.Voc;
import kr.debop4j.data.ogm.dao.IHibernateOgmDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 대량의 Voc 데이터에 대해 검색 수행 테스트
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 29. 오후 9:39
 */
@Slf4j
public class VocSearchTest extends MongoGridDatastoreTestBase {

    private static final Random rnd = new Random();
    private static final int REPEAT_COUNT = 100;
    private static final int VOC_COUNT = 100;

    public Voc createVoc() {
        Voc voc = new Voc("배성혁-" + rnd.nextInt());
        voc.addAttr("담당", "송길주");
        voc.addAttr("상태", "처리중");
        return voc;
    }

    public List<Voc> createVocs(int count) {
        List<Voc> vocs = new ArrayList<Voc>(count);
        for (int i = 0; i < count; i++) {
            Voc voc = new Voc("이름-" + i);
            voc.addAttr("담당", "송길주");
            voc.addAttr("상태", "처리중");
            vocs.add(voc);
        }
        return vocs;
    }

    @Test
    public void crud() throws Exception {
        IHibernateOgmDao dao = Springs.getBean(IHibernateOgmDao.class);

        Voc voc = new Voc("배성혁");
        voc.addAttr("담당", "송길주");
        voc.addAttr("상태", "처리중");

        dao.saveOrUpdate(voc);
        dao.getSession().flush();
        dao.getSession().clear();

        Voc loaded = dao.get(Voc.class, voc.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getSrtype()).isEqualTo(voc.getSrtype());

        dao.delete(loaded);
        dao.getSession().flush();
        dao.getSession().clear();

        loaded = dao.get(Voc.class, voc.getId());
        assertThat(loaded).isNull();
    }

    @Test
    public void searchQueryTest() throws Exception {
        daoInSerial(new Action1<IHibernateOgmDao>() {
            @Override
            public void perform(IHibernateOgmDao dao) {

                // findAll
                List<Voc> loadedVocs = dao.findAll(Voc.class);
                assertThat(loadedVocs).isNotNull();
                assertThat(loadedVocs.size()).isGreaterThan(0);
                log.debug("findAll seach result = [{}]", loadedVocs.size());

                QueryBuilder builder = dao.getQueryBuilder(Voc.class);
                Query query =
                        builder.bool()
                                .must(builder.keyword().onField("attrs.name").matching("담당").createQuery())
                                .must(builder.keyword().onField("attrs.value").matching("송길주").createQuery())
                                .must(builder.keyword().wildcard().onField("memo").matching("백두*").createQuery())
                                .createQuery();
                loadedVocs = (List<Voc>) dao.find(Voc.class, query);
                assertThat(loadedVocs).isNotNull();
                assertThat(loadedVocs.size()).isGreaterThan(0);

                builder = dao.getQueryBuilder(Voc.class);
                query =
                        builder.bool()
                                .must(builder.keyword().onField("attrs.name").matching("담당").createQuery())
                                .must(builder.keyword().onField("attrs.value").matching("송길").createQuery())
                                .must(builder.keyword().wildcard().onField("memo").matching("나눔*").createQuery())
                                .createQuery();
                loadedVocs = (List<Voc>) dao.find(Voc.class, query);
                assertThat(loadedVocs).isNotNull();
                assertThat(loadedVocs.size()).isEqualTo(0);
            }
        });
    }

    public void daoInSerial(Action1<IHibernateOgmDao> action) throws Exception {

        IHibernateOgmDao dao = Springs.getBean(IHibernateOgmDao.class);
        for (int i = 0; i < REPEAT_COUNT; i++) {
            List<Voc> vocs = createVocs(VOC_COUNT);
            for (Voc voc : vocs) {
                dao.persist(voc);
            }
            dao.getSession().flush();
            Thread.sleep(10);
            dao.flushToIndexes();
            Thread.sleep(10);
            dao.getSession().clear();
        }
        log.debug("Voc [{}]개를 추가했습니다.", REPEAT_COUNT * VOC_COUNT);

        try {
            action.perform(dao);
        } finally {
            log.debug("Voc 엔티티를 삭제합니다...");
            List<Voc> vocs = dao.findAll(Voc.class);
            assertThat(vocs.size()).isGreaterThan(0);
            dao.deleteAll(vocs);
            dao.getFullTextSession().flush();
            dao.flushToIndexes();
            dao.getFullTextSession().close();
        }
        assertThat(dao.count(Voc.class)).isEqualTo(0);
    }
}
