package kr.debop4j.data.mongodb.ogm.test.loading;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mongodb.ogm.test.MongoGridDatastoreConfiguration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.data.mongodb.ogm.test.loading.LoadSelectedColumnsInEntityTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
public class LoadSelectedColumnsInEntityTest extends LoadSelectedColumnsCollectionTest {

    @BeforeClass
    public static void beforeClass() {
        Springs.reset();
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

//    @Override
//    protected void addExtraColumn() {
//        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) Springs.getBean(DatastoreProvider.class);// (MongoDBDatastoreProvider) super.getService( DatastoreProvider.class );
//        DB database = provider.getDatabase();
//        DBCollection collection = database.getCollection("Project");
//
//        BasicDBObject query = new BasicDBObject(1);
//        query.put("_id", "projectID");
//
//        BasicDBObject updater = new BasicDBObject(1);
//        updater.put("$push", new BasicDBObject("extraColumn", 1));
//        collection.update(query, updater);
//    }
//
//    protected void checkLoading(DBObject associationObject) {
//        /*
//         * The only column (except _id) that needs to be retrieved is "modules"
//		 * So we should have 2 columns
//		 */
//        final Set<?> retrievedColumns = associationObject.keySet();
//        assertThat(retrievedColumns).hasSize(2).containsOnly(MongoDBDialect.ID_FIELDNAME, "modules");
//    }
}

/**
 * TODO: IN_ENTITY 방식은 아직 에러가 있는 것 같다.
 */
@Configuration
class InEntityConfiguration extends MongoGridDatastoreConfiguration {

    @Override
    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.COLLECTION;
        //return AssociationStorage.IN_ENTITY;
    }
}
