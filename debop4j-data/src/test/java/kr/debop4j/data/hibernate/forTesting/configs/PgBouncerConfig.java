package kr.debop4j.data.hibernate.forTesting.configs;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * PgBouncer에 연결하는 방식에 대한 설정 : BoneCP 는 pgBouncer와 연결이 안됩니다!!! 그래서 dbcp BasicDataSource를 사용합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 25
 */
@Configuration
@EnableTransactionManagement
public class PgBouncerConfig extends PostgreSqlConfig {

    @Bean
    public DataSource dataSource() {
//        return buildDataSource("org.postgresql.Driver",
//                               "jdbc:postgresql://localhost:6432/" + getDatabaseName() + "?prepareThreshold=0",
//                               "root",
//                               "root");

        // NOTE: pgBouncer 로 연결 시 꼭 prepareThreshold=0 를 추가해야 합니다.
        //
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:6432/" + getDatabaseName() + "?prepareThreshold=0");
        ds.setUsername("root");
        ds.setPassword("root");

        return ds;
    }
}
