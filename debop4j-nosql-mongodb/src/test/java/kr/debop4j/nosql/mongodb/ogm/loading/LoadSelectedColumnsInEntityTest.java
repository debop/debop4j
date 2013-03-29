package kr.debop4j.nosql.mongodb.ogm.loading;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.nosql.mongodb.ogm.MongoGridDatastoreConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.nosql.mongodb.ogm.loading.LoadSelectedColumnsInEntityTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
public class LoadSelectedColumnsInEntityTest extends LoadSelectedColumnsCollectionTest {

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(InEntityConfiguration.class);
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

/**
 * TODO: IN_ENTITY 방식은 아직 에러가 있는 것 같다.
 */
@Configuration
class InEntityConfiguration extends MongoGridDatastoreConfiguration {

//    @Override
//    protected AssociationStorage getAssociationStorage() {
//        return AssociationStorage.IN_ENTITY;
//    }
}
