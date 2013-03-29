package kr.debop4j.data.mongodb.cache;

import com.mongodb.Mongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * kr.debop4j.core.cache.mongodb.SpringMongoConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 25 오후 3:36
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = {UserRepository.class})
@Slf4j
public class MongoCacheConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "debop4j_nosql";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new Mongo("localhost");
    }

    @Bean
    public MongoCacheManager mongoCacheManager() throws Exception {
        return new MongoCacheManager(super.mongoTemplate(), 300);
    }
}
