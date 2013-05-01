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

package kr.debop4j.search.hibernate.sharding;

import kr.debop4j.search.AppConfig;
import kr.debop4j.search.hibernate.model.Dvd;
import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.search.hibernate.sharding.ShardingConfig
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 25. 오전 10:48
 */
@Configuration
@EnableTransactionManagement
public class ShardingConfig extends AppConfig {

    @Override
    protected Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        // sharding 관련 설정 추가
        String dvd = "hibernate.search." + Dvd.class.getName();
        props.put(dvd + ".sharding_strategy.nbr_of_shards", "2");
        props.put(dvd + ".directory_provider", FSDirectoryProvider.class.getName());

        return props;
    }
}
