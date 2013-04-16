package kr.debop4j.data.ogm.spring.cfg;

import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.ogm.datastore.impl.DatastoreServices;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Entity;
import java.util.Properties;
import java.util.Set;

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
     * hibernate-ogm용 configuration을 제공합니다.
     */
    @Bean
    public OgmConfiguration ogmConfiguration() {

        if (log.isInfoEnabled())
            log.info("hibernate-ogm용 configuration을 생성합니다...");

        OgmConfiguration cfg = new OgmConfiguration();

        for (String pkgName : getMappedPackageNames()) {
            if (log.isDebugEnabled())
                log.debug("Package를 추가합니다. package=[{}]", pkgName);

            final Reflections reflections = new Reflections(pkgName);
            final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
            for (final Class<?> clazz : classes) {
                if (log.isDebugEnabled())
                    log.debug("Entity 를 등록합니다. Entity=[{}]", clazz);
                cfg.addAnnotatedClass(clazz);
            }
            // cfg.addPackage(pkgName);
        }

        for (Class annoatatedClass : getMappedEntities()) {
            cfg.addAnnotatedClass(annoatatedClass);
            if (log.isDebugEnabled())
                log.debug("AnnotatedEntity를 추가합니다. annotatedClass=[{}]", annoatatedClass.getName());
        }

        log.trace("hibernate용 interceptor릉록합니다., hibernateInterceptor=[{}]", hibernateInterceptor());
        cfg.setInterceptor(hibernateInterceptor());
        cfg.setProperties(getHibernateOgmProperties());

        if (log.isInfoEnabled())
            log.info("hibernate-ogm용 configuration을 생성했습니다!!!");

        return cfg;
    }

    /**
     * Hibernate SessionFactory 를 제공합니다.
     */
    @Bean
    public SessionFactory sessionFactory() {
        if (log.isInfoEnabled())
            log.info("hiberante-ogm 용 SessionFactory를 생성합니다...");

        OgmConfiguration cfg = ogmConfiguration();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        if (log.isInfoEnabled())
            log.info("hibernate-ogm용 SessionFactory를 생성했습니다!!!");

        return sessionFactory;
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
    public HibernateOgmDaoImpl hibernateOgmDao() {
        return new HibernateOgmDaoImpl();
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
        if (log.isTraceEnabled())
            log.trace("Hibernate properties 를 설정합니다...");

        Properties props = new Properties();

        props.put(Environment.USE_NEW_ID_GENERATOR_MAPPINGS, true);
        props.put(Environment.HBM2DDL_AUTO, "none");

        return props;
    }

    /**
     * hibernate-ogm 용 속성을 반환합니다.
     */
    protected Properties getHibernateOgmProperties() {

        if (log.isTraceEnabled())
            log.trace("hibernate-ogm 용 property를 설정합니다...");

        Properties props = getHibernateProperties();

        // hibernate-search 설정 (hibernate-ogm에서는 hibernate criteria를 지원하지 않습니다. hibernate-search를 통해서 criteria를 지원합니다)
        // see Pro Hibernate and MongoDB pp. 246

        // hibernate-search 환경설정
        props.put("hibernate.search.default.indexmanager", "near-real-time");
        props.put("hibernate.search.default.directory_provider", "filesystem");
        props.put("hibernate.search.default.indexBase", "lucene/indexes");
        props.put("hibernate.search.default.locking_strategy", "single");
        // hibernate-search performance settings
        props.put("hibernate.search.default.indexwriter.max_buffered_doc", "true");
        props.put("hibernate.search.default.indexwriter.max_merge_docs", "100");
        props.put("hibernate.search.default.indexwriter.merge_factor", "20");
        props.put("hibernate.search.default.indexwriter.term_index_interval", "default");
        props.put("hibernate.search.default.indexwriter.ram_buffer_size", "1024");
        props.put("hibernate.search.default.exclusive_index_use", "true");

        // transaction factory
        // org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory
        // org.hibernate.engine.transaction.internal.jdbc.JdbcTransactionFactory
        props.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.JtaTransactionFactory");
        props.put("hibernate.current_session_context_class", "thread");

        return props;
    }

    /**
     * hibernate {@link ServiceRegistryImplementor}로부터 해당 서비스를 로드합니다.
     *
     * @param serviceImpl 서비스 구현체의 타입
     * @return {@link ServiceRegistryImplementor}에 등록된 서비스
     */
    protected final org.hibernate.service.Service getService(Class<? extends org.hibernate.service.Service> serviceImpl) {
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
    protected final SessionFactoryImplementor getSessionFactoryImplementor() {
        return (SessionFactoryImplementor) sessionFactory();
    }
}
