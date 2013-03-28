package kr.debop4j.nosql.mongodb.ogm;

import kr.debop4j.nosql.mongodb.ogm.model.Module;
import kr.debop4j.nosql.mongodb.ogm.model.Project;
import kr.debop4j.ogm.spring.cfg.mongodb.MongoDBConfigBase;
import kr.debop4j.ogm.tools.mongodb.MongoTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.nosql.mongodb.ogm.MongoDbConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Configuration
@ComponentScan({"kr.debop4j.ogm.spring.cfg",
                       "kr.debop4j.ogm.tools.mongodb"})
@Slf4j
public class OgmMongoDbConfiguration extends MongoDBConfigBase {

    @Bean
    public MongoTool mongoTool() {
        return new MongoTool(gridDialect(), datastoreProvider());
    }

    @Override
    protected String getDatabaseName() {
        return "debop4j_nosql_ogm";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Project.class.getPackage().getName(),
        };
    }

    @Override
    protected Class[] getMappedEntities() {
        return new Class[]{
                Module.class,
                Project.class
        };
    }
}
