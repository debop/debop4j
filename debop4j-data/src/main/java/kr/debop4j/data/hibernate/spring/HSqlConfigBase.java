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

import kr.debop4j.data.hibernate.forTesting.DatabaseEngine;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * HSql 을 DB로 사용하는 Hibernate Configuration
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class HSqlConfigBase extends HibernateConfigBase {

    /**
     * Gets database engine.
     *
     * @return the database engine
     */
    public DatabaseEngine getDatabaseEngine() {
        return DatabaseEngine.HSql;
    }

    @Override
    public String getDatabaseName() {
        return "test";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:hsqldb:mem:" + getDatabaseName() + ";MVCC=TRUE;";
    }

    @Override
    public String getUsername() {
        return "sa";
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("org.hsqldb.jdbcDriver", getJdbcUrl(), getUsername(), getPassword());
    }

    protected Properties hibernateProperties() {
        Properties props = super.hibernateProperties();
        props.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
        return props;
    }
}
