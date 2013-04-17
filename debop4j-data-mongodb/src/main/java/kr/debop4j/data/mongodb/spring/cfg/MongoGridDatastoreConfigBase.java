package kr.debop4j.data.mongodb.spring.cfg;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import kr.debop4j.data.mongodb.dao.impl.MongoOgmDaoImpl;
import kr.debop4j.data.mongodb.tools.MongoTool;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.net.UnknownHostException;
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

    @Bean
    public ServerAddress serverAddress() {
        try {
            return new ServerAddress("localhost");
        } catch (UnknownHostException e) {
            log.error("서버를 찾지 못했습니다.", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    protected Properties getHibernateOgmProperties() {

        Properties props = super.getHibernateOgmProperties();

        props.put("hibernate.ogm.datastore.provider", MONGODB_DATASTORE_PROVIDER);
        props.put(Environment.MONGODB_DATABASE, getDatabaseName());
        props.put(Environment.MONGODB_TIMEOUT, 200);

        props.put(Environment.MONGODB_HOST, serverAddress().getHost());
        props.put(Environment.MONGODB_PORT, serverAddress().getPort());

        // 엔티티 저장 방식
        props.put(Environment.MONGODB_ASSOCIATIONS_STORE, getAssociationStorage().name());

        if (log.isDebugEnabled())
            log.debug("hibernate-ogm 환경설정 정보를 지정했습니다. props=\n{}", props.toString());

        return props;
    }

    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.IN_ENTITY;
    }

    @Override
    @Bean
    @Scope("prototype")
    public HibernateOgmDaoImpl hibernateOgmDao() {
        return new MongoOgmDaoImpl();
    }

    @Bean
    @Scope("prototype")
    public MongoClient mongoClient() {
        try {
            return new MongoClient(serverAddress());
        } catch (Exception e) {
            log.error("MongoDB 접속에 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Scope("prototype")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Bean
    public MongoTool mongoTool() {
        GridDialect dialect = gridDialect();
        DatastoreProvider provider = datastoreProvider();

        return new MongoTool(dialect, provider);
    }
}
