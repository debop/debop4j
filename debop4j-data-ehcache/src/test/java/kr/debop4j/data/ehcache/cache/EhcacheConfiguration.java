package kr.debop4j.data.ehcache.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;

/**
 * kr.debop4j.core.cache.ehcache.EhcacheConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 8:59
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = {UserRepository.class})
@Slf4j
public class EhcacheConfiguration {

    @Bean(name = "ehcache")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
        bean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return bean;
    }

    @Bean(name = "cacheManager")
    public EhCacheCacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Cache Created");
    }
}
