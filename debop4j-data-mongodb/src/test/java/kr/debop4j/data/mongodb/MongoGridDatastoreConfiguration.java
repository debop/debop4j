package kr.debop4j.data.mongodb;

import kr.debop4j.data.mongodb.model.Project;
import kr.debop4j.data.mongodb.spring.cfg.MongoGridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected String getDatabaseName() {
        return "debop4j_data";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Project.class.getPackage().getName(),
        };
    }
}
