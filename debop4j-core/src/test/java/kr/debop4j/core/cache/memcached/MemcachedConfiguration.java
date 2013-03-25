package kr.debop4j.core.cache.memcached;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.MemcachedClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * kr.debop4j.core.cache.memcached.MemcachedConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 25 오후 1:26
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = {UserRepository.class})
@Slf4j
public class MemcachedConfiguration {

    /**
     * MemcachedClient 를 제공해야 합니다
     */
    @Bean
    public MemcachedClient memcachedClient() {
        try {
            return new MemcachedClient(new InetSocketAddress("localhost", 11211));
        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }
    }

    @Bean
    public MemcachedCacheManager memcachedCacheManager() {
        return new MemcachedCacheManager(memcachedClient());
    }


    @PostConstruct
    public void postConstruct() {
        log.info("Cache Created");
    }
}
