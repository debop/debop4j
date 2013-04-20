package kr.debop4j.access.test;

import kr.debop4j.access.model.calendar.WorkCalendar;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.organization.CompanyCode;
import kr.debop4j.access.model.product.Product;
import kr.debop4j.data.hibernate.springconfiguration.PostgreSqlConfigBase;
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
 * PostgreSQL DB를 사용하는 Hibernate 환경설정입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 7.
 */
@Configuration
@EnableTransactionManagement
public class UsingPostgreSqlConfiguration extends PostgreSqlConfigBase {

    @Override
    public String getDatabaseName() {
        return "HAccess";
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

    @Override
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        props.put(Environment.USE_QUERY_CACHE, true);
        props.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        props.put(Environment.CACHE_REGION_PREFIX, "");
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:ehcache.xml");

        // Validator
        props.put("javax.persistencexml.validation.group.pre-persist", "javax.validation.groups.Default");
        props.put("javax.persistencexml.validation.group.pre-update", "javax.validation.groups.Default");

        return props;
    }

    @Override
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
}
