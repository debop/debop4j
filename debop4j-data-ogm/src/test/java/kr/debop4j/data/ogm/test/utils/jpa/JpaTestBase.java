package kr.debop4j.data.ogm.test.utils.jpa;

import kr.debop4j.data.ogm.test.utils.BaseOGMTest;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.jpa.HibernateOgmPersistence;
import org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform;
import org.hibernate.service.jta.platform.spi.JtaPlatform;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.transaction.TransactionManager;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오전 11:22
 */
@Slf4j
public abstract class JpaTestBase extends BaseOGMTest {

    @Getter
    private EntityManagerFactory factory;
    @Getter
    private TransactionManager transactionManager;

    public abstract Class<?>[] getEntities();

    @Before
    public void createFactory() throws MalformedURLException {
        GetterPersistenceUnitInfo info = new GetterPersistenceUnitInfo();
        info.setClassLoader(Thread.currentThread().getContextClassLoader());
        //we explicitly list them to avoid scanning
        info.setExcludeUnlistedClasses(true);
        info.setJtaDataSource(new NoopDatasource());
        List<String> classNames = new ArrayList<String>();
        for (Class<?> clazz : getEntities()) {
            classNames.add(clazz.getName());
        }
        info.setManagedClassNames(classNames);
        info.setNonJtaDataSource(null);
        info.setPersistenceProviderClassName(HibernateOgmPersistence.class.getName());
        info.setPersistenceUnitName("default");
        final URL persistenceUnitRootUrl = new File("").toURL();
        info.setPersistenceUnitRootUrl(persistenceUnitRootUrl);
        info.setPersistenceXMLSchemaVersion("2.0");
        info.setProperties(new Properties());
        info.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        info.setTransactionType(PersistenceUnitTransactionType.JTA);
        info.setValidationMode(ValidationMode.AUTO);
        info.getProperties().setProperty(Environment.JTA_PLATFORM,
                                         JBossStandAloneJtaPlatform.class.getName()
        );

        // MassIndexerFactoryIntegrator is defined at 4.3.0
        // info.getProperties().setProperty(MassIndexerFactoryIntegrator.MASS_INDEXER_FACTORY_CLASSNAME, OgmMassIndexerFactory.class.getName());
        for (Map.Entry<String, String> entry : TestHelper.getEnvironmentProperties().entrySet()) {
            info.getProperties().setProperty(entry.getKey(), entry.getValue());
        }
        refineInfo(info);

        factory = new HibernateOgmPersistence().createContainerEntityManagerFactory(info, Collections.EMPTY_MAP);
        transactionManager = extractJBossTransactionManager(factory);
    }

    // can be overridden by subclasses
    protected void refineInfo(GetterPersistenceUnitInfo info) { }

    /**
     * Get JBoss TM out of Hibernate
     */
    public static TransactionManager extractJBossTransactionManager(EntityManagerFactory factory) {
        if (log.isDebugEnabled())
            log.debug("TransactionManager를 꺼냅니다.");
        SessionFactoryImplementor sessionFactory =
                (SessionFactoryImplementor) ((HibernateEntityManagerFactory) factory).getSessionFactory();
        return sessionFactory.getServiceRegistry().getService(JtaPlatform.class).retrieveTransactionManager();
    }

    protected final void commitOrRollback(boolean operationSuccessful) throws Exception {
        if (operationSuccessful) {
            getTransactionManager().commit();
        } else {
            getTransactionManager().rollback();
        }
    }

    @After
    public void closeFactory() {
        if (factory == null)
            return;

        if (factory.isOpen()) {
            TestHelper.dropSchemaAndDatabase(factory);
            factory.close();
            factory = null;
        } else {
            factory = null;
        }
    }
}
