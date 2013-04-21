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
 * PostgreSQL 용 ConnectionPool과 Replication을 제공하는 PgPool 로 Connection을 만듭니다. (포트 9999를 사용합니다)
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 26.
 */
@Configuration
@EnableTransactionManagement
public abstract class PgPoolConfigBase extends PostgreSqlConfigBase {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("org.postgresql.Driver",
                               "jdbc:postgresql://localhost:9999/" + getDatabaseName() + "?Set=UTF8",
                               "root",
                               "root");
    }
}
