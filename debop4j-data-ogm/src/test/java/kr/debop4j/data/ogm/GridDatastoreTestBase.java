package kr.debop4j.data.ogm;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * kr.debop4j.data.ogm.GridDatastoreTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 12:05
 */
public class GridDatastoreTestBase {

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(GridDatastoreConfiguration.class);
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
