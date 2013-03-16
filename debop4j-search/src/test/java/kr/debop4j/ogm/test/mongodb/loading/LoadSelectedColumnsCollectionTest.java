package kr.debop4j.ogm.test.mongodb.loading;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import kr.debop4j.ogm.test.OgmTestBase;
import org.fest.assertions.Assertions;
import org.hibernate.ogm.datastore.impl.DatastoreServices;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.datastore.spi.TupleContext;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.mongodb.MongoDBDialect;
import org.hibernate.ogm.grid.EntityKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * kr.debop4j.ogm.test.mongodb.loading.LoadSelectedColumnsCollectionTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 16.
 */
public class LoadSelectedColumnsCollectionTest extends OgmTestBase {

    @Test
    public void testLoadSelectedColumns() {
        final String collectionName = "Drink";

        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) getService(DatastoreProvider.class);

        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection(collectionName);
        BasicDBObject water = new BasicDBObject();

        water.put("_id", "1234");
        water.put("name", "Water");
        water.put("volume", "1L");
        collection.insert(water);

        List<String> selectedColumns = Lists.newArrayList();
        selectedColumns.add("name");
        Tuple tuple = this.getTuple(collectionName, "1234", selectedColumns);

        Assert.assertNotNull(tuple);
        Set<String> retrievedColumn = tuple.getColumnNames();

        Assert.assertEquals(selectedColumns.size(), retrievedColumn.size() - 1);
        Assert.assertTrue(retrievedColumn.containsAll(selectedColumns));

        collection.remove(water);
    }

    public Tuple getTuple(String collectionName, String id, List<String> selectedColumns) {
        EntityKey key = new EntityKey(collectionName, new String[]{MongoDBDialect.ID_FIELDNAME}, new Object[]{id});
        TupleContext tupleContext = new TupleContext(selectedColumns);
        return this.getGridDialect().getTuple(key, tupleContext);
    }

    protected GridDialect getGridDialect() {
        return ((DatastoreServices) getService(DatastoreServices.class)).getGridDialect();
    }

    protected void addExtraColumn() {
        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) getService(DatastoreProvider.class);
        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection("associations_Project_Module");
        BasicDBObject query = new BasicDBObject(1);
        query.put("_id", new BasicDBObject("Project_id", "projectID"));

        BasicDBObject updater = new BasicDBObject(1);
        updater.put("$push", new BasicDBObject("extraColumn", 1));
        collection.update(query, updater);
    }

    protected void checkLoading(DBObject associationObject) {
        //
        // The only column (expect _id) that needs to be retrieved is "rows
        // So we should have 2 columns
        //
        final Set<?> retrievedColumns = associationObject.keySet();
        Assertions
                .assertThat(retrievedColumns)
                .hasSize(2)
                .containsOnly(MongoDBDialect.ID_FIELDNAME, MongoDBDialect.ROWS_FIELDNAME);
    }
}
