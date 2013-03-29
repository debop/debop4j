package kr.debop4j.nosql.mongodb.ogm.loading;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.nosql.mongodb.ogm.MongoGridDatastoreConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.nosql.mongodb.ogm.loading.LoadSelectedColumnsGlobalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Slf4j
public class LoadSelectedColumnsGlobalTest extends LoadSelectedColumnsCollectionTest {

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(GlobalCollectionConfiguration.class);
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

@Configuration
class GlobalCollectionConfiguration extends MongoGridDatastoreConfiguration {

    @Override
    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.GLOBAL_COLLECTION;
    }
}
