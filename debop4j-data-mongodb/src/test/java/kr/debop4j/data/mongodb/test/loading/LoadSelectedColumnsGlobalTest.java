package kr.debop4j.data.mongodb.test.loading;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;

/**
 * kr.debop4j.data.mongodb.test.loading.LoadSelectedColumnsGlobalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Slf4j
public class LoadSelectedColumnsGlobalTest extends LoadSelectedColumnsCollectionTest {

    @Override
    protected void configure(org.hibernate.cfg.Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(
                Environment.MONGODB_ASSOCIATIONS_STORE,
                AssociationStorage.GLOBAL_COLLECTION.toString().toLowerCase()
        );
    }

    /**
     * To be sure the datastoreProvider retrieves only the columns we want,
     * an extra column is manually added to the association document
     */
    @Override
    protected void addExtraColumn() {
        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) super.getService(DatastoreProvider.class);
        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection("Associations");

        final BasicDBObject idObject = new BasicDBObject(2);
        idObject.append("Project_id", "projectID");
        idObject.append("table", "Project_Module");

        BasicDBObject query = new BasicDBObject(1);
        query.put("_id", idObject);

        BasicDBObject updater = new BasicDBObject(1);
        updater.put("$push", new BasicDBObject("extraColumn", 1));
        collection.update(query, updater);
    }
}
