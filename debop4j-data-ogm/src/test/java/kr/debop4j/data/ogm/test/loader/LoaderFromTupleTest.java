package kr.debop4j.data.ogm.test.loader;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.ogm.datastore.impl.MapTupleSnapshot;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.EntityKeyMetadata;
import org.hibernate.ogm.loader.OgmLoader;
import org.hibernate.ogm.loader.OgmLoadingContext;
import org.hibernate.ogm.persister.OgmEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static kr.debop4j.data.ogm.test.utils.TestHelper.extractEntityTuple;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.loader.LoaderFromTupleTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class LoaderFromTupleTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Feeling.class };
    }

    @Test
    public void loadingFromTuple() throws Exception {

        final Session session = openSession();
        Transaction transaction = session.beginTransaction();

        Feeling feeling = new Feeling();
        feeling.setName("Moody");
        session.persist(feeling);

        transaction.commit();
        session.clear();

        EntityKey key = new EntityKey(new EntityKeyMetadata("Feeling", new String[] { "id" }),
                                      new Object[] { feeling.getId() });
        Map<String, Object> entityTuple = (Map<String, Object>) extractEntityTuple(sessions, key);
        final Tuple tuple = new Tuple(new MapTupleSnapshot(entityTuple));

        EntityPersister persister =
                ((SessionFactoryImplementor) session.getSessionFactory())
                        .getEntityPersister(Feeling.class.getName());

        OgmLoader loader = new OgmLoader(new OgmEntityPersister[] { (OgmEntityPersister) persister });
        OgmLoadingContext ogmLoadingContext = new OgmLoadingContext();
        List<Tuple> tuples = new ArrayList<Tuple>();
        tuples.add(tuple);
        ogmLoadingContext.setTuples(tuples);

        List<Object> entities = loader.loadEntities((SessionImplementor) session, LockOptions.NONE, ogmLoadingContext);
        assertThat(entities.size()).isEqualTo(1);
        assertThat(((Feeling) entities.get(0)).getName()).isEqualTo("Moody");

        session.close();
    }
}
