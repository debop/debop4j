package kr.debop4j.ogm.spring.cfg.ehcache;

import kr.debop4j.ogm.spring.cfg.DatastoreConfigBase;
import net.sf.ehcache.CacheManager;
import org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.ehcache.EhcacheDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ehcache 를 저장소로 사용하는 hibernate-ogm 의 환경설정
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 10:35
 */
@Configuration
@EnableTransactionManagement
public abstract class EhcacheConfigBase extends DatastoreConfigBase {

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
