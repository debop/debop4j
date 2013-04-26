package kr.debop4j.data.mongodb.test.queries;

import kr.debop4j.data.mongodb.dao.MongoOgmDao;
import kr.debop4j.data.ogm.test.queries.Helicopter;
import kr.debop4j.data.ogm.test.queries.Hypothesis;
import kr.debop4j.data.ogm.test.queries.SimpleQueriesTest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.AfterClass;

/**
 * kr.debop4j.data.mongodb.test.queries.MongodbSimpleQueriesTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 5:56
 */
@Slf4j
public class MongodbSimpleQueriesTest extends SimpleQueriesTest {

    @AfterClass
    public static void afterClass() throws Exception {

        final Session session = sessions.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            MongoOgmDao dao = new MongoOgmDao(session);
            dao.deleteAll(Helicopter.class);
            dao.deleteAll(Hypothesis.class);
            transaction.commit();
        } catch (Exception ignored) {
            log.warn("삭제에 실패했습니다. mongodb의 경우 database를 수동으로 삭제해 주세요.", ignored);
        }
        session.close();
    }

}
