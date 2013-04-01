package kr.debop4j.data.ogm.spring.cfg;

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
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * hibernate-ogm 의 환경설정을 Spring Configuration으로 구현합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29
 */
@Configuration
@Slf4j
public abstract class GridDatastoreConfigBase {
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
            if (log.isDebugEnabled())
                log.debug("Package를 추가합니다. package=[{}]", pkgName);
        }

        for (Class annoatatedClass : getMappedEntities()) {
            cfg.addAnnotatedClass(annoatatedClass);
            if (log.isDebugEnabled())
                log.debug("AnnotatedEntity를 추가합니다. annotatedClass=[{}]", annoatatedClass.getName());
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

    abstract protected String getDatabaseName();

    protected String[] getMappedPackageNames() {
        return new String[0];
    }

    protected Class<?>[] getMappedEntities() {
        return new Class<?>[0];
    }

    /**
     * hibernate 용 속성을 설정합니다.
     */
    protected Properties getHibernateProperties() {
        Properties props = new Properties();

        props.put(Environment.USE_NEW_ID_GENERATOR_MAPPINGS, true);
        props.put(Environment.HBM2DDL_AUTO, "none");

        return props;
    }

    /**
     * hibernate-ogm 용 속성을 반환합니다.
     */
    protected Properties getHibernateOgmProperties() {
        return getHibernateProperties();
    }

    /**
     * hibernate {@link ServiceRegistryImplementor}로부터 해당 서비스를 로드합니다.
     *
     * @param serviceImpl 서비스 구현체의 타입
     * @return {@link ServiceRegistryImplementor}에 등록된 서비스
     */
    protected org.hibernate.service.Service getService(Class<? extends org.hibernate.service.Service> serviceImpl) {
        if (log.isDebugEnabled())
            log.debug("Service 를 로드합니다. serviceImpl=[{}]", serviceImpl.getName());

        SessionFactoryImplementor sessionFactory = getSessionFactoryImplementor();
        ServiceRegistryImplementor serviceRegistry = sessionFactory.getServiceRegistry();
        return serviceRegistry.getService(serviceImpl);
    }

    /**
     * SessionFactoryImplementor를 제공합니다.
     *
     * @return {@link SessionFactoryImplementor} 인스턴스
     */
    protected SessionFactoryImplementor getSessionFactoryImplementor() {
        return (SessionFactoryImplementor) sessionFactory();
    }
}
