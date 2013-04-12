package kr.debop4j.data.mongodb.ogm.test.loading;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import kr.debop4j.data.mongodb.ogm.test.model.Module;
import kr.debop4j.data.mongodb.ogm.test.model.Project;
import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.datastore.impl.DatastoreServices;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.*;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.mongodb.MongoDBAssociationSnapshot;
import org.hibernate.ogm.dialect.mongodb.MongoDBDialect;
import org.hibernate.ogm.grid.*;
import org.hibernate.service.Service;
import org.hibernate.service.spi.ServiceRegistryImplementor;
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
public class LoadSelectedColumnsCollectionTest extends OgmTestBase {

    @Test
    public void testLoadSelectedColumns() {
        final String collectionName = "Drink";

        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) this.getService(DatastoreProvider.class);

        DB database = provider.getDatabase();
        DBCollection collection = database.getCollection(collectionName);
        BasicDBObject water = new BasicDBObject();
        water.put("_id", "1234");
        water.put("name", "Water");
        water.put("volume", "1L");
        collection.insert(water);

        List<String> selectedColumns = new ArrayList<String>();
        selectedColumns.add("name");
        Tuple tuple = this.getTuple(collectionName, "1234", selectedColumns);

        Assert.assertNotNull(tuple);
        Set<String> retrievedColumn = tuple.getColumnNames();

		/*
          *The dialect will return all columns (which include _id field) so we have to substract 1 to check if
		  *the right number of columns has been loaded.
		 */
        Assert.assertEquals(selectedColumns.size(), retrievedColumn.size() - 1);
        Assert.assertTrue(retrievedColumn.containsAll(selectedColumns));

        collection.remove(water);
    }

    @Test
    public void testLoadSelectedAssociationColumns() {
        Session session = openSession();
        final Transaction transaction = session.getTransaction();
        transaction.begin();

        Module mongodb = new Module();
        mongodb.setName("MongoDB");
        session.persist(mongodb);

        Module infinispan = new Module();
        infinispan.setName("Infinispan");
        session.persist(infinispan);

        List<Module> modules = new ArrayList<Module>();
        modules.add(mongodb);
        modules.add(infinispan);

        Project hibernateOGM = new Project();
        hibernateOGM.setId("projectID");
        hibernateOGM.setName("HibernateOGM");
        hibernateOGM.setModules(modules);

        session.persist(hibernateOGM);
        transaction.commit();

        this.addExtraColumn();
        GridDialect gridDialect = this.getGridDialect();
        AssociationKeyMetadata metadata = new AssociationKeyMetadata("Project_Module", new String[]{ "Project_id" });
        metadata.setRowKeyColumnNames(new String[]{ "Project_id", "module_id" });
        AssociationKey associationKey = new AssociationKey(
                metadata,
                new Object[]{ "projectID" }
        );
        associationKey.setAssociationKind(AssociationKind.ASSOCIATION);
        associationKey.setCollectionRole("modules");
        associationKey.setOwnerEntityKey(new EntityKey(new EntityKeyMetadata("Project", new String[]{ "id" }), new String[]{ "projectID" }));
        AssociationContext associationContext = new AssociationContext(Arrays.asList(associationKey.getRowKeyColumnNames()));
        final Association association = gridDialect.getAssociation(associationKey, associationContext);
        final MongoDBAssociationSnapshot associationSnapshot = (MongoDBAssociationSnapshot) association.getSnapshot();
        final DBObject assocObject = associationSnapshot.getDBObject();
        this.checkLoading(assocObject);

        session.delete(mongodb);
        session.delete(infinispan);
        session.delete(hibernateOGM);
        session.close();
    }

    public Tuple getTuple(String collectionName, String id, List<String> selectedColumns) {
        EntityKey key = new EntityKey(new EntityKeyMetadata(collectionName, new String[]{ MongoDBDialect.ID_FIELDNAME }), new Object[]{ id });
        TupleContext tupleContext = new TupleContext(selectedColumns);
        return this.getGridDialect().getTuple(key, tupleContext);
    }

    protected Service getService(Class<? extends Service> serviceImpl) {
        SessionFactoryImplementor factory = super.sfi();
        ServiceRegistryImplementor serviceRegistry = factory.getServiceRegistry();
        return serviceRegistry.getService(serviceImpl);
    }

    protected GridDialect getGridDialect() {
        return ((DatastoreServices) this.getService(DatastoreServices.class)).getGridDialect();
    }

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(
                Environment.MONGODB_ASSOCIATIONS_STORE,
                AssociationStorage.COLLECTION.name()
        );
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                Project.class,
                Module.class
        };
    }

    /**
     * To be sure the datastoreProvider retrieves only the columns we want,
     * an extra column is manually added to the association document
     */
    protected void addExtraColumn() {
        MongoDBDatastoreProvider provider = (MongoDBDatastoreProvider) this.getService(DatastoreProvider.class);
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
