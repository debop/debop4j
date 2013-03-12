package kr.debop4j.data;

import kr.debop4j.data.hibernate.springconfiguration.HSqlConfigBase;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.data.HibernateConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig extends HSqlConfigBase {

    @Override
    protected Properties hibernateProperties() {

        Properties props = super.hibernateProperties();

        props.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        props.put(Environment.USE_QUERY_CACHE, true);
        props.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:ehcache.xml");

        return props;
    }

    private static String[] mappedPackageNames = new String[]{
            "kr.debop4j.data.mapping.model.annotated",
            "kr.debop4j.data.mapping.model.annotated.collection",
            "kr.debop4j.data.mapping.model.annotated.join",
            "kr.debop4j.data.mapping.model.annotated.joinedSubclass",
            "kr.debop4j.data.mapping.model.annotated.onetomany",
            "kr.debop4j.data.mapping.model.annotated.onetoone",
            "kr.debop4j.data.mapping.model.annotated.subclass",
            "kr.debop4j.data.mapping.model.annotated.tree",
            "kr.debop4j.data.mapping.model.annotated.unionSubclass",
            "kr.debop4j.data.mapping.model.annotated.usertypes",
            "kr.debop4j.data.hibernate.search.model"
    };

    @Override
    protected String[] getMappedPackageNames() {
        return mappedPackageNames;
    }
}
