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

import jodd.props.Props;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.model.Twit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.mongodb.dao.TimelineTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 15. 오후 2:58
 */
@Slf4j
public class TimelineTest extends MongoGridDatastoreTestBase {

    @Autowired
    MongoOgmDao dao;

    @Test
    public void loadProps() throws Exception {
        Props props = new Props();
        props.load(new ClassPathResource("twitter.props").getInputStream());

        assertThat(props.countTotalProperties()).isGreaterThan(0);
        assertThat(props.getValue("oauth.consumerKey")).isNotEmpty();
    }

    /** hibernate-ogm 은 아직 hibernate 의 criteria api 를 지원하지 않습니다!!! */
    @Test
    public void insertAndLoadDelete() throws Exception {
        try {
            // 트위터 정보를 받아 저장하기
            Twitter twitter = Twitters.getTwitter();
            List<Status> statuses = twitter.getHomeTimeline(new Paging(1, 100));


                log.trace("Timeline의 새로운 글 수 =[{}]", statuses.size());

            for (Status status : statuses) {
                Twit twit = Twitters.createTwit(status);
                dao.saveOrUpdate(twit);


                    log.trace("Twit을 저장했습니다. [{}]", twit);
            }
            dao.getFullTextSession().flush();
            dao.getFullTextSession().clear();

            List<Twit> twits = dao.find(Twit.class);
            assertThat(twits.size()).isGreaterThan(0);
            assertThat(dao.count(Twit.class)).isEqualTo(twits.size());

        } finally {
            dao.deleteAll(Twit.class);
            dao.getFullTextSession().flush();
            dao.clearIndex(Twit.class);
        }
        assertThat(dao.count(Twit.class)).isEqualTo(0);
    }
}
