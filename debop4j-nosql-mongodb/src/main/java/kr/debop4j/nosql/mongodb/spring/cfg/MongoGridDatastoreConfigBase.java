package kr.debop4j.nosql.mongodb.spring.cfg;

import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.TransactionManager;
import java.util.Properties;

/**
 * MongoDB 를 hibernate-ogm 엔티티의 저장소로 사용하도록 하는 Spring 환경설정입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29
 */
@Configuration
@Slf4j
public class MongoGridDatastoreConfigBase extends GridDatastoreConfigBase {
    public static final String MONGODB_DATASTORE_PROVIDER =
            "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider";

    protected Properties getHibernateOgmProperties() {
        Properties props = getHibernateProperties();

        props.put("hibernate.ogm.datastore.provider", MONGODB_DATASTORE_PROVIDER);
        props.put(Environment.MONGODB_DATABASE, getDatabaseName());
        props.put(Environment.MONGODB_TIMEOUT, 200);
        props.put(Environment.MONGODB_ASSOCIATIONS_STORE, getAssociationStorage().name());

        if (log.isDebugEnabled())
            log.debug("hibernate-ogm 환경설정 정보를 지정했습니다. props=\n{}", props.toString());

        return props;
    }

    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.COLLECTION;
    }

    @Bean
    public javax.transaction.TransactionManager transactionManager() {
        TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
        if (log.isDebugEnabled())
            log.debug("TransactionManager 를 생성합니다. transactionManager=[{}]", tm.getClass().getName());
        return tm;
    }
}
