package kr.debop4j.data.mongodb.cache;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * kr.debop4j.core.cache.mongodb.SpringMongoConfiguration
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 25 오후 3:36
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = { UserRepository.class })
@Slf4j
public class MongoCacheConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "debop4j_cache";
    }

    @Bean(name = "databaseName")
    protected String databaseName() {
        return getDatabaseName();
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongoClient();
    }

    @Bean
    public MongoClient mongoClient() throws Exception {
        return new MongoClient("localhost");
    }

    @Bean
    public MongoCacheManager mongoCacheManager() throws Exception {
        return new MongoCacheManager(super.mongoTemplate(), 300);
    }
}
