package kr.debop4j.data.mongodb.ogm.test;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * kr.debop4j.data.mongodb.ogm.test.MongoGridDatastoreTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Slf4j
public abstract class MongoGridDatastoreTestBase {

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(MongoGridDatastoreConfiguration.class);
    }

    @AfterClass
    public static void afterClass() {
        Springs.reset();
    }

    @Before
    public void before() {
        UnitOfWorks.start();
    }

    @After
    public void after() {
        UnitOfWorks.stop();
    }
}
