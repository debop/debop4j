package kr.debop4j.data.mongodb;

import kr.debop4j.data.mongodb.dao.Player;
import kr.debop4j.data.mongodb.dao.impl.MongoOgmDaoImpl;
import kr.debop4j.data.mongodb.spring.cfg.MongoGridDatastoreConfigBase;
import kr.debop4j.data.mongodb.test.model.Project;
import kr.debop4j.data.mongodb.tools.MongoTool;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.data.mongodb.ogm.MongoDbConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Configuration
@ComponentScan({ "kr.debop4j.data.mongodb.spring.cfg",
                       "kr.debop4j.data.mongodb.tools" })
@Slf4j
public class MongoGridDatastoreConfiguration extends MongoGridDatastoreConfigBase {

    @Bean
    public MongoTool mongoTool() {
        GridDialect dialect = gridDialect();
        DatastoreProvider provider = datastoreProvider();

        return new MongoTool(dialect, provider);
    }

    @Override
    protected String getDatabaseName() {
        return "debop4j_data";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Project.class.getPackage().getName(),
                Player.class.getPackage().getName()
        };
    }

    @Override
    @Bean
    public HibernateOgmDaoImpl hibernateOgmDao() {
        return new MongoOgmDaoImpl();
    }
}
