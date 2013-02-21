package kr.debop4j.data.hibernate.forTesting.configurations;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * kr.debop4j.data.hibernate.forTesting.configurations.PostgreSqlDbConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
public abstract class PostgreSqlDbConfiguration extends DbConfiguration {

    @Override
    public String getDatabaseName() {
        return "hibernate";
    }

    @Bean
    public DataSource dataSource() {

        return buildDataSource("org.postgresql.Driver",
                               "jdbc:postgresql://localhost/" + getDatabaseName() + "?Set=UTF8",
                               "root",
                               "root");
    }

    @Override
    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL82Dialect");

        return props;
    }
}
