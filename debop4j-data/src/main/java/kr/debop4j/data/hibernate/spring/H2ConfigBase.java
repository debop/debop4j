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

package kr.debop4j.data.hibernate.spring;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * H2 Embedded DB를 사용하는 Hibernate 설정정입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 21.
 */
public abstract class H2ConfigBase extends HibernateConfigBase {

    @Override
    protected String getDatabaseName() {
        return "mem";
    }

    @Override
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("org.h2.Driver",
                               "jdbc:h2:" + getDatabaseName() + ":test;DB_CLOSE_DELAY=-1",
                               "sa",
                               "");
    }

    @Override
    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");

        return props;
    }
}
