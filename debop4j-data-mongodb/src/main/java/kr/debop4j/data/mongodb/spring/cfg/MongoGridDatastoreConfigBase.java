package kr.debop4j.data.mongodb.spring.cfg;

import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MongoDB 를 hibernate-ogm 엔티티의 저장소로 사용하도록 하는 Spring 환경설정입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 29
 */
@Configuration
@Slf4j
public abstract class MongoGridDatastoreConfigBase extends GridDatastoreConfigBase {

    public static final String MONGODB_DATASTORE_PROVIDER =
            "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider";

    @Override
    protected Properties getHibernateOgmProperties() {
        Properties props = getHibernateProperties();

        props.put("hibernate.ogm.datastore.provider", MONGODB_DATASTORE_PROVIDER);
        props.put(Environment.MONGODB_DATABASE, getDatabaseName());
        props.put(Environment.MONGODB_TIMEOUT, 200);

        props.put(Environment.MONGODB_HOST, "localhost");
        props.put(Environment.MONGODB_PORT, 27017);

        // 엔티티 저장 방식
        props.put(Environment.MONGODB_ASSOCIATIONS_STORE, getAssociationStorage().name());

        if (log.isDebugEnabled())
            log.debug("hibernate-ogm 환경설정 정보를 지정했습니다. props=\n{}", props.toString());

        return props;
    }

    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.IN_ENTITY;
    }
}
