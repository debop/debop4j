package kr.debop4j.ogm.spring.cfg.mongodb;

import kr.debop4j.ogm.spring.cfg.DatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * kr.debop4j.ogm.spring.cfg.mongodb.MongoDBConfigBase
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 3:53
 */
@Configuration
@Slf4j
public abstract class MongoDBConfigBase extends DatastoreConfigBase {

    public static final String MONGODB_DATASTORE_PROVIDER = "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider";

    protected Properties getHibernateOgmProperties() {
        Properties props = getHibernateProperties();

        props.setProperty("hibernate.ogm.datastore.provider", MONGODB_DATASTORE_PROVIDER);

        props.put(Environment.MONGODB_DATABASE, getDatabaseName());
        props.put(Environment.MONGODB_TIMEOUT, 200);
        props.put(Environment.MONGODB_ASSOCIATIONS_STORE, getAssociationStorage().name());

        return props;
    }

    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.COLLECTION;
    }

    @Bean
    public javax.transaction.TransactionManager transactionManager() {
        return com.arjuna.ats.jta.TransactionManager.transactionManager();
    }
}
