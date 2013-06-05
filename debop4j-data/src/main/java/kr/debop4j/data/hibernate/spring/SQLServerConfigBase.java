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
 * SQLServer 접속을 위한 Spring 환경설정 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 22.
 */
@Configuration
@EnableTransactionManagement
public abstract class SQLServerConfigBase extends HibernateConfigBase {

    @Override
    public String getDatabaseName() {
        return "hibernate";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlserver://localhost/" + getDatabaseName() + ";integratedSecurity=true;";
    }

    @Override
    public String getUsername() {
        return "sa";
    }

    @Override
    public String getPassword() {
        return "sa";
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                               getJdbcUrl(),
                               getUsername(),
                               getPassword());
    }
}
