package kr.debop4j.nosql.mongodb.ogm;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * kr.debop4j.nosql.mongodb.ogm.MongoDbGridDatastoreTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
public abstract class MongoDbGridDatastoreTestBase {

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
