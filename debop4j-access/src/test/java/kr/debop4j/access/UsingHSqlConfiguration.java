package kr.debop4j.access;

import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.product.Product;
import kr.debop4j.access.model.workcalendar.WorkCalendar;
import kr.debop4j.data.hibernate.springconfiguration.HSqlConfigBase;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.access.UsingHSqlConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 7.
 */
@Configuration
@EnableTransactionManagement
public class UsingHSqlConfiguration extends HSqlConfigBase {

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
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
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:ehcache.xml");

        return props;
    }
}
