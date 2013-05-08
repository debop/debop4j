package kr.debop4j.data.couchbase.spring.cache;

import com.couchbase.client.CouchbaseClient;
import kr.debop4j.data.couchbase.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * com.couchbase.spring.cache.CouchbaseCacheConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 6:14
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = { UserRepository.class })
@Slf4j
public class CouchbaseCacheConfig {

    @Bean
    public List<URI> servers() {
        List<URI> servers = new ArrayList<URI>();
        servers.add(URI.create("http://localhost:8091/pools"));
        return servers;
    }

    @Bean
    @Scope("prototype")
    public CouchbaseClient couchbaseClient() throws IOException {
        return new CouchbaseClient(servers(), "default", "");
    }

    @Bean
    public CouchbaseCacheManager couchbaseCacheManager() throws IOException {
        return new CouchbaseCacheManager(couchbaseClient());
    }
}
