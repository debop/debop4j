package org.hibernate.ogm.test.mongodb.loading;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.ogm.tools.mongodb.MongoTool;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.test.mongodb.MongoDBTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * org.hibernate.ogm.test.mongodb.loading.LoadSelectedColumnsCollectionTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 5:43
 */
public class LoadSelectedColumnsCollectionTest extends MongoDBTestBase {

    @Test
    public void loadSelectedColumns() {
        final String collectionName = "Drink";

        MongoTool mongoTool = Springs.getBean(MongoTool.class);
        MongoDBDatastoreProvider provider = mongoTool.getProvider();


        Assert.assertNotNull(provider);

        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection(collectionName);

        BasicDBObject water = new BasicDBObject();

        water.put("_id", "1234");
        water.put("name", "Water");
        water.put("volumn", "1L");

        collection.insert(water);

        List<String> selectedColumns = Lists.newArrayList();
        selectedColumns.add("name");


        Tuple tuple = mongoTool.getTuple(collectionName, "1234", selectedColumns);

        Assert.assertNotNull(tuple);

        Set<String> retrievedColumn = tuple.getColumnNames();
        Assert.assertEquals(selectedColumns.size(), retrievedColumn.size() - 1);
        Assert.assertTrue(retrievedColumn.containsAll(selectedColumns));

        collection.remove(water);
    }
}
