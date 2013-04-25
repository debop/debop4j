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
import kr.debop4j.search.hibernate.model.Item;
import kr.debop4j.search.sharding.DistributorShardingStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.search.hibernate.sharding.ShardingConfig
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오전 10:48
 */
@Configuration
@EnableTransactionManagement
public class ShardingConfig extends AppConfig {

    @Override
    protected Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        // sharding 관련 설정 추가
        String itemName = "hibernate.search." + Item.class.getName();

        props.put(itemName + ".sharding_strategy_nbr_of_shards", "3");
        props.put(itemName + ".0.indexName", "Item-Universal");
        props.put(itemName + ".1.indexName", "Item-Sony");
        props.put(itemName + ".2.indexName", "Item-Warner");

        props.put(itemName + ".sharding_strategy", DistributorShardingStrategy.class.getName());
        props.put(itemName + ".sharding_strategy.distributor.1", "0");
        props.put(itemName + ".sharding_strategy.distributor.2", "1");
        props.put(itemName + ".sharding_strategy.distributor.3", "2");

        return props;
    }
}
