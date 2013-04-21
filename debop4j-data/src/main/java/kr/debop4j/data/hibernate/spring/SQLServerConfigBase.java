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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * kr.debop4j.data.hibernate.spring.SQLServerConfigBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 22.
 */
@Configuration
@EnableTransactionManagement
public abstract class SQLServerConfigBase extends HibernateConfigBase {

    protected String getDatabaseName() {
        return "hibernate";
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                               "jdbc:sqlserver://localhost/" + getDatabaseName() + ";integratedSecurity=true;",
                               "sa",
                               "sa");
    }
}