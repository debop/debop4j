package kr.debop4j.data.mongodb.ogm.loading;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mongodb.ogm.MongoGridDatastoreConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.data.mongodb.ogm.loading.LoadSelectedColumnsGlobalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Slf4j
public class LoadSelectedColumnsGlobalTest extends LoadSelectedColumnsCollectionTest {

    @BeforeClass
    public static void beforeClass() {
        Springs.reset();
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

    /**
     * To be sure the datastoreProvider retrieves only the columns we want,
     * an extra column is manually added to the association document
     */
//    @Override
//    protected void addExtraColumn() {
//        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) Springs.getBean(DatastoreProvider.class); //super.getService( DatastoreProvider.class );
//        DB database = provider.getDatabase();
//        DBCollection collection = database.getCollection("Associations");
//
//        final BasicDBObject idObject = new BasicDBObject(2);
//        idObject.append("Project_id", "projectID");
//        idObject.append("table", "Project_Module");
//
//        BasicDBObject query = new BasicDBObject(1);
//        query.put("_id", idObject);
//
//        BasicDBObject updater = new BasicDBObject(1);
//        updater.put("$push", new BasicDBObject("extraColumn", 1));
//        collection.update(query, updater);
//    }
}

@Configuration
@Slf4j
class GlobalCollectionConfiguration extends MongoGridDatastoreConfiguration {

    @Override
    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.COLLECTION;
        //return AssociationStorage.GLOBAL_COLLECTION;
    }
}
