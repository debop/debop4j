package kr.debop4j.data.mongodb.test.loading;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.mongodb.MongoDBDialect;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.mongodb.test.loading.LoadSelectedColumnsInEntityTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 28
 */
public class LoadSelectedColumnsInEntityTest extends LoadSelectedColumnsCollectionTest {

    @Override
    protected void configure(org.hibernate.cfg.Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(
                Environment.MONGODB_ASSOCIATIONS_STORE,
                AssociationStorage.IN_ENTITY.toString().toLowerCase()
        );
    }

    @Override
    protected void addExtraColumn() {
        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) super.getService(DatastoreProvider.class);
        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection("Project");

        BasicDBObject query = new BasicDBObject(1);
        query.put("_id", "projectID");

        BasicDBObject updater = new BasicDBObject(1);
        updater.put("$push", new BasicDBObject("extraColumn", 1));
        collection.update(query, updater);
    }

    protected void checkLoading(DBObject associationObject) {
        /*
         * The only column (except _id) that needs to be retrieved is "modules"
		 * So we should have 2 columns
		 */
        final Set<?> retrievedColumns = associationObject.keySet();
        assertThat(retrievedColumns).hasSize(2).containsOnly(MongoDBDialect.ID_FIELDNAME, "modules");
    }
}
