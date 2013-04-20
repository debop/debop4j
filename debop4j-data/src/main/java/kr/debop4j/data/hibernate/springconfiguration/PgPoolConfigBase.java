package kr.debop4j.data.hibernate.springconfiguration;

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
