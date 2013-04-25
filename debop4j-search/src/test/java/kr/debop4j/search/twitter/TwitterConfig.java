package kr.debop4j.search.twitter;

import kr.debop4j.search.AppConfig;
import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.search.twitter.TwitterConfig
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오후 4:33
 */
@Configuration
@EnableTransactionManagement
public class TwitterConfig extends AppConfig {

    @Override
    protected Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        String twit = "hibernate.search." + Twit.class.getName();
        props.put(twit + ".sharding_strategy.nbr_of_shards", "5");
        props.put(twit + ".directory_provider", FSDirectoryProvider.class.getName());

        return props;
    }
}
