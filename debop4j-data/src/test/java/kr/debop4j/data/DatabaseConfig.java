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

package kr.debop4j.data;

import kr.debop4j.data.hibernate.spring.HSqlConfigBase;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * kr.debop4j.data.HibernateConfig
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 12.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig extends HSqlConfigBase {

    @Override
    protected Properties hibernateProperties() {

        Properties props = super.hibernateProperties();

        props.put(Environment.HBM2DDL_AUTO, "create-drop"); // create | spawn | spawn-drop | update | validate | none

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
