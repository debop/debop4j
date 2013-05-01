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

package kr.debop4j.search.twitter;

import jodd.props.Props;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkNestingOptions;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.search.dao.HibernateSearchDao;
import kr.debop4j.search.dao.IHibernateSearchDao;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import twitter4j.*;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.search.twitter.TimelineTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 23. 오후 10:53
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterConfig.class })
public class TimelineTest {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void before() {
        if (Springs.isNotInitialized())
            Springs.init(appContext);

        UnitOfWorks.start();
    }

    @After
    public void after() {
        UnitOfWorks.stop();
    }

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
        HibernateSearchDao dao = appContext.getBean(HibernateSearchDao.class);

        try {
            // 트위터 정보를 받아 저장하기
            Twitter twitter = Twitters.getTwitter();
            List<Status> statuses = twitter.getHomeTimeline(new Paging(1, 50));

            if (log.isTraceEnabled())
                log.trace("Timeline의 새로운 글 수 =[{}]", statuses.size());

            for (Status status : statuses) {
                Twit twit = Twitters.createTwit(status);
                dao.saveOrUpdate(twit);

                if (log.isTraceEnabled())
                    log.trace("Twit을 저장했습니다. [{}]", twit);
            }
            dao.getFullTextSession().flush();
            dao.getFullTextSession().flushToIndexes();
            dao.getFullTextSession().clear();

            List<Twit> twits = dao.findAll(Twit.class);
            assertThat(twits.size()).isGreaterThan(0);
            Assertions.assertThat(dao.count(Twit.class)).isEqualTo(twits.size());

        } finally {
            dao.deleteAll(Twit.class);
            dao.flushSession();
            dao.getFullTextSession().flushToIndexes();
            dao.optimize(Twit.class);
            assertThat(dao.count(Twit.class)).isEqualTo(0);
        }
    }

    @Test
    public void twitStream() throws Exception {

        final IHibernateSearchDao dao = appContext.getBean(IHibernateSearchDao.class);

        try {
            TwitterStream twitterStream = Twitters.getTwitterStream();
            twitterStream.addListener(getStatusListener());

            for (int i = 0; i < 2; i++) {
                twitterStream.sample();
                Thread.sleep(1000);
            }
        } finally {
            dao.deleteAll(Twit.class);
            dao.flushSession();
            dao.flushIndexes();
            dao.clearIndex(Twit.class);
        }
    }

    private StatusListener getStatusListener() {
        return new StatusListener() {
            @Override
            public void onStatus(Status status) {
                Twit twit = Twitters.createTwit(status);
                try (IUnitOfWork unitOfWork = UnitOfWorks.start(UnitOfWorkNestingOptions.CreateNewOrNestUnitOfWork)) {
                    final IHibernateSearchDao dao = appContext.getBean(IHibernateSearchDao.class);
                    dao.saveOrUpdate(twit);
                    dao.flushSession();
                }

                if (log.isTraceEnabled())
                    log.trace("Twit을 저장했습니다. [{}]", twit);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                log.debug("삭제된 정보. id=[{}]", statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                log.debug("회수 제한 경고:[{}]", numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                // nothing to do.
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                // nothing to do.
            }

            @Override
            public void onException(Exception ex) {
                log.error("예외가 발생했습니다.", ex);
            }
        };
    }
}
