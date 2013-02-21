package kr.debop4j.data.hibernate.forTesting.configurations;

import kr.debop4j.data.hibernate.forTesting.DatabaseEngine;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * kr.debop4j.data.hibernate.forTesting.configurations.HSqlDbConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class HSqlDbConfiguration extends DbConfiguration {

    public DatabaseEngine getDatabaseEngine() {
        return DatabaseEngine.HSql;
    }


    @Override
    public String getDatabaseName() {
        return "mem";
    }

    @Override
    @Bean
    public DataSource dataSource() {
        return buildDataSource("org.hsqldb.jdbcDriver",
                               "jdbc:hsqldb:" + getDatabaseName() + ":test",
                               "sa",
                               "");
    }

    @Override
    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");

        return props;
    }
}
