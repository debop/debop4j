package kr.debop4j.access.test;

import kr.debop4j.access.model.calendar.WorkCalendar;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.organization.CompanyCode;
import kr.debop4j.access.model.product.Product;
import kr.debop4j.data.hibernate.springconfiguration.MySqlConfigBase;
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
 * com.kt.vital.domain.UsingMySqlConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Configuration
@EnableTransactionManagement
public class UsingMySqlConfiguration extends MySqlConfigBase {

    @Override
    public String getDatabaseName() {
        return "Vital";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                CompanyCode.class.getPackage().getName(),
                Company.class.getPackage().getName(),
                Product.class.getPackage().getName(),
                WorkCalendar.class.getPackage().getName(),
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

        props.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        props.put(Environment.USE_QUERY_CACHE, true);
        props.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        props.put(Environment.CACHE_REGION_PREFIX, "");
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:ehcache.xml");

        // Validator
        props.put("javax.persistence.validation.group.pre-persist", "javax.validation.groups.Default");
        props.put("javax.persistence.validation.group.pre-update", "javax.validation.groups.Default");

        return props;
    }
}
