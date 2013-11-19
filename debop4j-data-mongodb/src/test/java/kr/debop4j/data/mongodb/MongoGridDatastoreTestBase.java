package kr.debop4j.data.mongodb;

import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.debop4j.data.mongodb.MongoGridDatastoreTestBase
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 28
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoGridDatastoreConfiguration.class })
public abstract class MongoGridDatastoreTestBase {

    @BeforeClass
    public static void beforeClass() {}

    @AfterClass
    public static void afterClass() {
        // Springs.reset();
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
