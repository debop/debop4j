package kr.debop4j.core.cache.memcached;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import net.spy.memcached.transcoders.SerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * kr.debop4j.core.cache.memcached.MemcachedConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 25 오후 1:26
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = {UserRepository.class})
@Slf4j
public class MemcachedCacheConfiguration {

    @Bean
    public Transcoder<Object> transcoder() {
        SerializingTranscoder transcoder = new SerializingTranscoder(Integer.MAX_VALUE);
        transcoder.setCompressionThreshold(1024);
        return transcoder;
    }

    /**
     * MemcachedClient 를 제공해야 합니다
     */
    @Bean
    public MemcachedClient memcachedClient() {
        try {
            // 설정항목 : https://code.google.com/p/spymemcached/wiki/SpringIntegration

            MemcachedClientFactoryBean bean = new MemcachedClientFactoryBean();
            bean.setServers("localhost:11211"); // servers="host1:11211,host2:11211";
            bean.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
            bean.setTranscoder(transcoder());
            bean.setOpTimeout(1000);  // 1000 msec

            bean.setHashAlg(DefaultHashAlgorithm.KETAMA_HASH);
            bean.setLocatorType(ConnectionFactoryBuilder.Locator.CONSISTENT);

            return (MemcachedClient) bean.getObject();

        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }
    }

    @Bean
    public MemcachedCacheManager memcachedCacheManager() {
        int timeoutInSeconds = 300;
        return new MemcachedCacheManager(memcachedClient(), timeoutInSeconds);
    }


    @PostConstruct
    public void postConstruct() {
        log.info("Cache Created");
    }
}
