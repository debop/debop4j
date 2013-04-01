package kr.debop4j.data.ogm.test.loader;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.loader.OgmLoader;
import org.hibernate.ogm.persister.OgmEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * kr.debop4j.data.ogm.test.loader.LoaderFromTupleTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
public class LoaderFromTupleTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Feeling.class };
    }

    @Test
    public void testLoadingFromTuple() throws Exception {
        final Session session = openSession();

        Transaction transaction = session.beginTransaction();
        Feeling feeling = new Feeling();
        feeling.setName("Moody");
        session.persist(feeling);
        transaction.commit();

        session.clear();

        EntityKey key = new EntityKey("Feeling", new String[] { "UUID" }, new Object[] { feeling.getId() });

        EntityPersister persister = ((SessionFactoryImplementor) session.getSessionFactory()).getEntityPersister(Feeling.class.getName());
        OgmLoader loader = new OgmLoader(new OgmEntityPersister[] { (OgmEntityPersister) persister });

        List<Tuple> tuples = new ArrayList<Tuple>();

        session.close();
    }
}
