/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.ehcache.ogm.cfg;

import kr.debop4j.data.ogm.spring.GridDatastoreConfigBase;
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
