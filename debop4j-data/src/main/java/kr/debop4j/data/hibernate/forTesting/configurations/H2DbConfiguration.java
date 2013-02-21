package kr.debop4j.data.hibernate.forTesting.configurations;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * kr.debop4j.data.hibernate.forTesting.configurations.H2DbConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class H2DbConfiguration extends DbConfiguration {

    @Override
    protected String getDatabaseName() {
        return "mem";
    }

    @Override
    @Bean
    public DataSource dataSource() {
        return buildDataSource("org.h2.Driver",
                               "jdbc:h2:" + getDatabaseName() + ":test",
                               "sa",
                               "");
    }

    @Override
    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");

        return props;
    }
}
