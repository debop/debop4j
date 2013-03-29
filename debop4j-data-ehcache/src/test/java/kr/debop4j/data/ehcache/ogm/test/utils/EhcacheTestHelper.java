package kr.debop4j.data.ehcache.ogm.test.utils;

import kr.debop4j.data.ogm.test.utils.TestableGridDialect;
import org.hibernate.SessionFactory;
import org.hibernate.ogm.grid.EntityKey;

import java.util.Map;

/**
 * kr.debop4j.data.ehcache.ogm.test.utils.EhcacheTestHelper
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29. 오후 10:03
 */
public class EhcacheTestHelper implements TestableGridDialect {
    @Override
    public boolean assertNumberOfEntities(int numberOfEntities, SessionFactory sessionFactory) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean assertNumberOfAssociations(int numberOfAssociations, SessionFactory sessionFactory) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, Object> extractEntityTuple(SessionFactory sessionFactory, EntityKey key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean backendSupportsTransactions() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dropSchemaAndDatabase(SessionFactory sessionFactory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getEnvironmentProperties() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
