package com.kt.vital.domain;

import com.kt.vital.domain.model.Voc;
import com.kt.vital.domain.model.admin.TopicBase;
import kr.debop4j.data.hibernate.springconfiguration.HSqlConfigBase;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import org.hibernate.SessionFactory;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.beanvalidation.BeanValidationEventListener;
import org.hibernate.event.spi.EventType;
import org.springframework.context.annotation.Bean;
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
                Voc.class.getPackage().getName(),
                TopicBase.class.getPackage().getName(),
        };
    }

    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sessionFactory = super.sessionFactory();

        // validator용 listener 추가
        HibernateTool.registerEventListener(sessionFactory,
                                            beanValidationEventListener(),
                                            EventType.PRE_INSERT, EventType.PRE_UPDATE, EventType.PRE_DELETE);

        return sessionFactory;
    }

    @Bean
    public BeanValidationEventListener beanValidationEventListener() {
        return new BeanValidationEventListener();
    }

    @Override
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        // secondary cache
        props.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        props.put(Environment.USE_QUERY_CACHE, true);
        props.put(Environment.CACHE_REGION_PREFIX, "debop4j-access");
        props.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:ehcache.xml");

        // Validator
        props.put("javax.persistence.validation.group.pre-persist", "javax.validation.groups.Default");
        props.put("javax.persistence.validation.group.pre-update", "javax.validation.groups.Default");

        return props;
    }
}
