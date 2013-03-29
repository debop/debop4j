package kr.debop4j.data.couchbase.cache;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * kr.debop4j.core.cache.couchbase.CouchbaseCacheConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25 오후 5:36
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = {UserRepository.class})
@Slf4j
public class CouchbaseCacheConfiguration {

    @Bean
    public List<URI> couchbaseList() {
        List<URI> uris = Lists.newArrayList(URI.create("http://localhost:8091/pools"));
        return uris;
    }

    @Bean
    public CouchbaseClient couchbaseClient() throws IOException {
        return new CouchbaseClient(couchbaseList(), "default", "");
    }

    @Bean
    public CouchbaseCacheManager couchbaseCacheManager() throws IOException {
        return new CouchbaseCacheManager(couchbaseClient());
    }
}
