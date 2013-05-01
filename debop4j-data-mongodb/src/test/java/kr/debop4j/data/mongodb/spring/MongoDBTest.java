package kr.debop4j.data.mongodb.spring;

import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.model.LuckyNumberPojo;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * MongoDBTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 14. 오후 10:16
 */
public class MongoDBTest extends MongoGridDatastoreTestBase {

    @Test
    public void persistAndDeleteTest() {
        LuckyNumberPojo num = new LuckyNumberPojo();
        num.setId("123");
        num.setLuckynumber(123);
        UnitOfWorks.getCurrentSession().persist(num);
        UnitOfWorks.getCurrent().transactionalFlush();

        LuckyNumberPojo loaded = (LuckyNumberPojo) UnitOfWorks.getCurrentSession().get(LuckyNumberPojo.class, num.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getId()).isEqualTo(num.getId());
        assertThat(loaded.getLuckynumber()).isEqualTo(num.getLuckynumber());

        UnitOfWorks.getCurrentSession().delete(loaded);
        UnitOfWorks.getCurrent().transactionalFlush();
    }
}
