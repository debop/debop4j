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

package kr.debop4j.data.infinispan.ogm.cfg;

import kr.debop4j.core.NotImplementedException;
import kr.debop4j.data.ogm.spring.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.infinispan.impl.InfinispanDatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.infinispan.InfinispanDialect;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Infinispan을 저장소로 사용하는 hibernate-ogm용  Spring 환경설정 정보입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 6. 7. 오후 12:03
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class InfinispanGridDatastoreConfigBase extends GridDatastoreConfigBase {

    @Override
    protected String getDatabaseName() {
        throw new NotImplementedException("구현해야 합니다.");
    }

    @Override
    @Bean
    public GridDialect gridDialect() {
        return new InfinispanDialect((InfinispanDatastoreProvider) datastoreProvider());
    }

    @Bean
    public EmbeddedCacheManager embeddedCacheManager() {

        return ((InfinispanDatastoreProvider) datastoreProvider()).getEmbeddedCacheManager();
    }
}
