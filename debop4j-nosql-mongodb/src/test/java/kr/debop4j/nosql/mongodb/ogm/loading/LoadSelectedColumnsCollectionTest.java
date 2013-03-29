package kr.debop4j.nosql.mongodb.ogm.loading;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.nosql.mongodb.ogm.MongoGridDatastoreTestBase;
import kr.debop4j.nosql.mongodb.ogm.model.Module;
import kr.debop4j.nosql.mongodb.ogm.model.Project;
import kr.debop4j.nosql.mongodb.tools.MongoTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.Association;
import org.hibernate.ogm.datastore.spi.AssociationContext;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.mongodb.MongoDBAssociationSnapshot;
import org.hibernate.ogm.dialect.mongodb.MongoDBDialect;
import org.hibernate.ogm.grid.AssociationKey;
import org.hibernate.ogm.grid.AssociationKind;
import org.hibernate.ogm.grid.EntityKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * org.hibernate.ogm.test.mongodb.loading.LoadSelectedColumnsCollectionTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 5:43
 */
@Slf4j
public class LoadSelectedColumnsCollectionTest extends MongoGridDatastoreTestBase {

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

    @Test
    public void testLoadSelectedAssociationColumns() {

        Project loaded = (Project) UnitOfWorks.getCurrentSession().get(Project.class, "projectID");
        if (loaded != null) {
            UnitOfWorks.getCurrentSession().delete(loaded);
            UnitOfWorks.getCurrentSession().flush();
        }

        Module mongodb = new Module();
        mongodb.setName("MongoDB");
        UnitOfWorks.getCurrentSession().persist(mongodb);

        Module infinispan = new Module();
        infinispan.setName("Infinispan");
        UnitOfWorks.getCurrentSession().persist(infinispan);

        List<Module> modules = new ArrayList<Module>();
        modules.add(mongodb);
        modules.add(infinispan);

        Project hibernateOGM = new Project("projectID", "HibernateOGM");
        hibernateOGM.setModules(modules);

        UnitOfWorks.getCurrentSession().persist(hibernateOGM);
        UnitOfWorks.getCurrent().transactionalFlush();

        this.addExtraColumn();
        GridDialect gridDialect = Springs.getBean(GridDialect.class);  //this.getGridDialect();
        AssociationKey associationKey = new AssociationKey(
                "Project_Module",
                new String[]{"Project_id"},
                new Object[]{"projectID"}
        );
        associationKey.setAssociationKind(AssociationKind.ASSOCIATION);
        associationKey.setCollectionRole("modules");
        associationKey.setOwnerEntityKey(new EntityKey("Project", new String[]{"id"}, new String[]{"projectID"}));
        associationKey.setRowKeyColumnNames(new String[]{"Project_id", "module_id"});
        AssociationContext associationContext = new AssociationContext(Arrays.asList(associationKey.getRowKeyColumnNames()));
        final Association association = gridDialect.getAssociation(associationKey, associationContext);
        final MongoDBAssociationSnapshot associationSnapshot = (MongoDBAssociationSnapshot) association.getSnapshot();
        final DBObject assocObject = associationSnapshot.getDBObject();
        this.checkLoading(assocObject);

        UnitOfWorks.getCurrentSession().delete(mongodb);
        UnitOfWorks.getCurrentSession().delete(infinispan);
        UnitOfWorks.getCurrentSession().delete(hibernateOGM);
        UnitOfWorks.getCurrentSession().flush();
    }

    /**
     * To be sure the datastoreProvider retrieves only the columns we want,
     * an extra column is manually added to the association document
     */
    protected void addExtraColumn() {
        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) Springs.getBean(DatastoreProvider.class); //this.getService( DatastoreProvider.class );
        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection("associations_Project_Module");
        BasicDBObject query = new BasicDBObject(1);
        query.put("_id", new BasicDBObject("Project_id", "projectID"));

        BasicDBObject updater = new BasicDBObject(1);
        updater.put("$push", new BasicDBObject("extraColumn", 1));
        collection.update(query, updater);
    }

    protected void checkLoading(DBObject associationObject) {
        /*
        * The only column (except _id) that needs to be retrieved is "rows"
		* So we should have 2 columns
		*/
        final Set<?> retrievedColumns = associationObject.keySet();
        assertThat(retrievedColumns).hasSize(2).containsOnly(MongoDBDialect.ID_FIELDNAME, MongoDBDialect.ROWS_FIELDNAME);
    }
}
