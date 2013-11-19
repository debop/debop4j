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
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * PostgreSQL DB를 사용하는 Hibernate 용 환경설정입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class PostgreSqlConfigBase extends HibernateConfigBase {

    /**
     * The constant DRIVER_CLASS.
     */
    public static final String DRIVER_CLASS = "org.postgresql.Driver";
    /**
     * The constant DIALECT.
     */
    public static final String DIALECT = "org.hibernate.dialect.PostgreSQL82Dialect";

    @Override
    public String getDatabaseName() {
        return "hibernate";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:postgresql://localhost/" + getDatabaseName() + "?Set=UTF8";
    }

    @Override
    public String getUsername() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "root";
    }


    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource(DRIVER_CLASS, getJdbcUrl(), getUsername(), getPassword());
    }

    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, DIALECT);

        return props;
    }
}
