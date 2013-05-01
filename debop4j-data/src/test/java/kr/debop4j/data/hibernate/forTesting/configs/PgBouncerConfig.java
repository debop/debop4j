package kr.debop4j.data.hibernate.forTesting.configs;

import kr.debop4j.data.jdbc.JdbcTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * PgBouncer에 연결하는 방식에 대한 설정 : BoneCP 는 pgBouncer와 연결이 안됩니다!!! 그래서 dbcp BasicDataSource를 사용합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 25
 */
@Configuration
@EnableTransactionManagement
public class PgBouncerConfig extends PostgreSqlConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {

        // NOTE: pgBouncer 로 연결 시 꼭 prepareThreshold=0 를 추가해야 합니다.
        //
        return
                JdbcTool.getTomcatDataSource("org.postgresql.Driver",
                                             "jdbc:postgresql://localhost:6432/" + getDatabaseName() + "?prepareThreshold=0",
                                             "root",
                                             "root");

//        BasicDataSource ds = new BasicDataSource();
//        ds.setDriverClassName("org.postgresql.Driver");
//        ds.setUrl("jdbc:postgresql://localhost:6432/" + getDatabaseName() + "?prepareThreshold=0");
//        ds.setUsername("root");
//        ds.setPassword("root");
//
//        return ds;
    }
}
