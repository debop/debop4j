package org.hibernate.ogm.test.mongodb;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * org.hibernate.ogm.test.mongodb.MongoDBTestBase
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 2:20
 */
@Slf4j
public abstract class MongoDBTestBase {

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(AppConfiguration.class);
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
