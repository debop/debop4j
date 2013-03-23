package org.hibernate.ogm.test.mongodb;

import kr.debop4j.ogm.spring.cfg.mongodb.MongoDBConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.test.mongodb.model.Project;
import org.springframework.context.annotation.Configuration;

/**
 * MongoDB 를 DataStore로 사용하는 Configuration
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 2:31
 */
@Configuration
@Slf4j
public class MongoDBConfiguration extends MongoDBConfigBase {

    @Override
    protected String getDatabaseName() {
        return "debop4j_ogm_test";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Project.class.getPackage().getName(),
        };
    }
}
