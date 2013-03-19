package com.kt.vital.domain;

import com.kt.vital.domain.model.VoiceOfCustomer;
import kr.debop4j.data.hibernate.springconfiguration.HSqlConfigBase;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * HSql 메모리 DB를 사용하는 Hibernate 환경 설정입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 7.
 */
@Configuration
@EnableTransactionManagement
public class UsingHSqlConfiguration extends HSqlConfigBase {

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                VoiceOfCustomer.class.getPackage().getName()
        };
    }

    @Override
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        props.put(Environment.USE_QUERY_CACHE, true);
        props.put(Environment.CACHE_REGION_PREFIX, "debop4j-access");
        props.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:ehcache.xml");

        return props;
    }
}
