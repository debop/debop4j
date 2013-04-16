package kr.debop4j.data.mongodb.dao;

import jodd.props.Props;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.model.Twit;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.mongodb.dao.TimelineTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 2:58
 */
@Slf4j
public class TimelineTest extends MongoGridDatastoreTestBase {

    @Test
    public void loadProps() throws Exception {
        Props props = new Props();
        props.load(new ClassPathResource("twitter.props").getInputStream());

        assertThat(props.countTotalProperties()).isGreaterThan(0);
        assertThat(props.getValue("oauth.consumerKey")).isNotEmpty();
    }

    /**
     * hibernate-ogm 은 아직 hibernate 의 criteria api 를 지원하지 않습니다!!!
     */
    @Test
    public void insertAndLoadDelete() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);

        // 트위터 정보를 받아 저장하기
        Twitter twitter = Twitters.getTwitter();
        List<Status> statuses = twitter.getHomeTimeline(new Paging(1, 100));
        for (Status status : statuses) {
            Twit twit = Twitters.createTwit(status);
            dao.saveOrUpdate(twit);
        }
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        // 저장된 트윗 정보를 전체 로드하여 삭제합니다.
        //
        Transaction tx = UnitOfWorks.getCurrentSession().beginTransaction();
        try {
            List<Twit> twits = dao.findAll(Twit.class);
            TimelineTest.log.debug("로드된 Twit 수=[{}]", twits.size());

            dao.deleteAll(twits);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        }
    }
}
