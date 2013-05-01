package kr.debop4j.data.ehcache.ogm.cfg;

import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.ehcache.EhcacheDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.data.ehcache.ogm.cfg.EhcacheGridDatastoreConfigBase
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 29
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public abstract class EhcacheGridDatastoreConfigBase extends GridDatastoreConfigBase {
    @Override
    @Bean
    public DatastoreProvider datastoreProvider() {
        DatastoreProvider provider = (DatastoreProvider) getService(DatastoreProvider.class);

        if (!(EhcacheDatastoreProvider.class.isInstance(provider))) {
            throw new RuntimeException("Ehcache 에서 테스트를 하지 못했습니다.");
        }
        return EhcacheDatastoreProvider.class.cast(provider);
    }

    @Override
    @Bean
    public GridDialect gridDialect() {
        return new EhcacheDialect((EhcacheDatastoreProvider) datastoreProvider());
    }

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        return ((EhcacheDatastoreProvider) datastoreProvider()).getCacheManager();
    }
}
