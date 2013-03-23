package kr.debop4j.ogm.spring.cfg;

import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.repository.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.ogm.datastore.impl.DatastoreServices;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.service.Service;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * hibernate-ogm 의 환경설정을 Spring Configuration으로 구현합니다.
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 2:22
 */
@Configuration
@Slf4j
public abstract class DatastoreConfigBase {

    /**
     * DataStoreProvider 를 제공합니다.
     */
    @Bean
    public DatastoreProvider datastoreProvider() {
        return (DatastoreProvider) getService(DatastoreProvider.class);
    }

    /**
     * {@link GridDialect} 를 제공합니다.
     */
    @Bean
    public GridDialect gridDialect() {
        return ((DatastoreServices) getService(DatastoreServices.class)).getGridDialect();
    }

    /**
     * Hibernate SessionFactory 를 제공합니다.
     */
    @Bean
    public SessionFactory sessionFactory() {
        if (log.isInfoEnabled())
            log.info("hiberante-ogm 용 SessionFactory를 생성합니다...");

        OgmConfiguration cfg = new OgmConfiguration();

        for (String pkgName : getMappedPackageNames()) {
            cfg.addPackage(pkgName);
        }

        for (Class annoatatedClass : getMappedEntities()) {
            cfg.addAnnotatedClass(annoatatedClass);
        }

        cfg.setInterceptor(hibernateInterceptor());
        cfg.setProperties(getHibernateOgmProperties());

        if (log.isInfoEnabled())
            log.info("hiberante-ogm 용 SessionFactory를 생성했습니다!!!");

        return cfg.buildSessionFactory();
    }

    @Bean
    public org.hibernate.Interceptor hibernateInterceptor() {
        return new StatefulEntityInterceptor();
    }

    @Bean
    public UnitOfWorkFactory unitOfWorkFactory() {
        if (log.isInfoEnabled())
            log.info("UnitOfWorkFactory를 생성합니다...");

        UnitOfWorkFactory factory = new UnitOfWorkFactory();
        factory.setSessionFactory(sessionFactory());
        return factory;
    }

    @Bean
    public HibernateRepositoryFactory hibernateRepositoryFactory() {
        return new HibernateRepositoryFactory();
    }

    protected String getDatabaseName() {
        return "debop4j_ogm_test";
    }

    protected String[] getMappedPackageNames() {
        return new String[0];
    }

    protected Class[] getMappedEntities() {
        return new Class[0];
    }


    protected Properties getHibernateProperties() {
        Properties props = new Properties();

        props.setProperty(Environment.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        props.setProperty(Environment.HBM2DDL_AUTO, "none");

        return props;
    }

    protected Properties getHibernateOgmProperties() {
        return getHibernateProperties();
    }

    protected org.hibernate.service.Service getService(Class<? extends Service> serviceImpl) {
        SessionFactoryImplementor sessionFactory = sfi();
        ServiceRegistryImplementor serviceRegistry = sessionFactory.getServiceRegistry();
        return serviceRegistry.getService(serviceImpl);
    }

    protected SessionFactoryImplementor sfi() {
        return (SessionFactoryImplementor) sessionFactory();
    }
}
