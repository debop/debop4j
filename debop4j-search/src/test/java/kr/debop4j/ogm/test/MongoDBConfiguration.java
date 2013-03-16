package kr.debop4j.ogm.test;

import kr.debop4j.data.hibernate.interceptor.MultiInterceptor;
import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.repository.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * kr.debop4j.ogm.test.MongoDBConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 16.
 */
@Configuration
public class MongoDBConfiguration {

    public String getDatabaseName() {
        return "debop4j_ogm_test";
    }

    public String[] getMappedPackageNames() {
        return new String[]{
                "kr.debop4j.mongodb.model"
        };
    }

    protected Properties hibernateProperties() {
        Properties props = new Properties();

        props.setProperty("hibernate.ogm.datastore.provider", "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider");
        props.setProperty("hibernate.ogm.mongodb.database", getDatabaseName());
        props.setProperty("hibernate.ogm.mongodb.connection_timeout", "200");
        props.setProperty(org.hibernate.cfg.Configuration.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        props.setProperty(Environment.HBM2DDL_AUTO, "none");

        return props;
    }

    @Bean
    public SessionFactory sessionFactory() {
        OgmConfiguration cfg = new OgmConfiguration();

        for (String pkg : getMappedPackageNames())
            cfg.addPackage(pkg);

        cfg.setInterceptor(hibernateInterceptor());
        cfg.setProperties(hibernateProperties());

        return cfg.buildSessionFactory();
    }

    @Bean
    public MultiInterceptor hibernateInterceptor() {
        MultiInterceptor interceptor = new MultiInterceptor();

        interceptor.getInterceptors().add(statuefulEntityInterceptor());
        interceptor.getInterceptors().add(updateTimestampedInterceptor());

        return interceptor;
    }

    @Bean
    public StatefulEntityInterceptor statuefulEntityInterceptor() {
        return new StatefulEntityInterceptor();
    }

    @Bean
    public UpdateTimestampedInterceptor updateTimestampedInterceptor() {
        return new UpdateTimestampedInterceptor();
    }

    @Bean
    public UnitOfWorkFactory unitOfWorkFactory() {
        UnitOfWorkFactory factory = new UnitOfWorkFactory();
        factory.setSessionFactory(sessionFactory());
        return factory;
    }

    @Bean
    public HibernateRepositoryFactory hibernateRepositoryFactory() {
        return new HibernateRepositoryFactory();
    }
}
