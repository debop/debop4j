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

package kr.debop4j.access.test;

import kr.debop4j.access.model.calendar.WorkCalendar;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.product.Product;
import kr.debop4j.data.hibernate.spring.HSqlConfigBase;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.beanvalidation.BeanValidationEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * HSql 메모리 DB를 사용하는 Hibernate 환경 설정입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 7.
 */
@Configuration
@EnableTransactionManagement
public class UsingHSqlConfiguration extends HSqlConfigBase {

    @Override
    protected String[] getMappedPackageNames() {
        return new String[] {
                Company.class.getPackage().getName(),
                Product.class.getPackage().getName(),
                WorkCalendar.class.getPackage().getName(),
        };
    }

    @Override
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.HBM2DDL_AUTO, "create-drop"); // create | spawn | spawn-drop | update | validate | none

        props.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        props.put(Environment.USE_QUERY_CACHE, true);
        props.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
        props.put(Environment.CACHE_REGION_PREFIX, "");
        props.put(Environment.CACHE_PROVIDER_CONFIG, "classpath:redis.properties");

        // Validator
        props.put("javax.persistencexml.validation.group.pre-persist", "javax.validation.groups.Default");
        props.put("javax.persistencexml.validation.group.pre-update", "javax.validation.groups.Default");

        return props;
    }

    @Bean
    public BeanValidationEventListener beanValidationEventListener() {
        return new BeanValidationEventListener();
    }
}
