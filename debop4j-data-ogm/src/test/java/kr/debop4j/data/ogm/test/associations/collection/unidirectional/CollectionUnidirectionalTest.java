package kr.debop4j.data.ogm.test.associations.collection.unidirectional;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static kr.debop4j.data.ogm.test.utils.TestHelper.assertNumberOfAssociations;
import static kr.debop4j.data.ogm.test.utils.TestHelper.assertNumberOfEntities;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * kr.debop4j.data.ogm.test.associations.collection.unidirectional.CollectionUnidirectionalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 12:58
 */
@Slf4j
public class CollectionUnidirectionalTest extends OgmTestBase {

    @Getter
    private Session session;

    @Override
    public void doBefore() throws Exception {
        super.doBefore();
        session = openSession();
    }

    @Override
    public void doAfter() throws Exception {
        if (session != null)
            session.close();
        super.doAfter();
    }

    @Test
    public void unidirectionalCollection() throws Exception {

        Transaction transaction = session.beginTransaction();

        SnowFlake sf = new SnowFlake();
        sf.setDescription("Snowflake 1");
        session.save(sf);

        SnowFlake sf2 = new SnowFlake();
        sf2.setDescription("Snowflake 2");
        session.save(sf2);

        Cloud cloud = new Cloud();
        cloud.setLength(23);
        cloud.getProducedSnowFlakes().add(sf);
        cloud.getProducedSnowFlakes().add(sf2);
        session.persist(cloud);
        session.flush();

        assertThat(assertNumberOfEntities(3, sessions)).isTrue();
        assertThat(assertNumberOfAssociations(1, sessions)).isTrue();
        transaction.commit();

        assertThat(assertNumberOfEntities(3, sessions)).isTrue();
        assertThat(assertNumberOfAssociations(1, sessions)).isTrue();
        session.clear();

        transaction = session.beginTransaction();

        cloud = (Cloud) session.get(Cloud.class, cloud.getId());
        assertNotNull(cloud.getProducedSnowFlakes());
        assertEquals(2, cloud.getProducedSnowFlakes().size());

        final SnowFlake removedSf = cloud.getProducedSnowFlakes().iterator().next();
        SnowFlake sf3 = new SnowFlake();
        sf3.setDescription("Snowflake 3");
        session.persist(sf3);

        cloud.getProducedSnowFlakes().remove(removedSf);
        cloud.getProducedSnowFlakes().add(sf3);

        transaction.commit();

        assertThat(assertNumberOfEntities(4, sessions)).isTrue();
        assertThat(assertNumberOfAssociations(1, sessions)).isTrue();
        session.clear();

        transaction = session.beginTransaction();
        cloud = (Cloud) session.get(Cloud.class, cloud.getId());
        assertNotNull(cloud.getProducedSnowFlakes());
        assertEquals(2, cloud.getProducedSnowFlakes().size());
        boolean present = false;
        for (SnowFlake current : cloud.getProducedSnowFlakes()) {
            if (current.getDescription().equals(removedSf.getDescription())) {
                present = true;
            }
        }
        assertFalse("flake not removed", present);
        for (SnowFlake current : cloud.getProducedSnowFlakes()) {
            session.delete(current);
        }
        session.delete(session.load(SnowFlake.class, removedSf.getId()));
        cloud.getProducedSnowFlakes().clear();
        transaction.commit();

        assertThat(assertNumberOfEntities(1, sessions)).isTrue();
        assertThat(assertNumberOfAssociations(0, sessions)).isTrue();

        session.clear();

        transaction = session.beginTransaction();
        cloud = (Cloud) session.get(Cloud.class, cloud.getId());
        assertNotNull(cloud.getProducedSnowFlakes());
        assertEquals(0, cloud.getProducedSnowFlakes().size());
        session.delete(cloud);
        session.flush();
        transaction.commit();

        assertThat(assertNumberOfEntities(0, sessions)).isTrue();
        assertThat(assertNumberOfAssociations(0, sessions)).isTrue();

        checkCleanCache();
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                Cloud.class,
                SnowFlake.class
        };
    }
}
