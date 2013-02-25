package kr.debop4j.data.hibernate.springconfiguration;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * kr.debop4j.data.hibernate.springconfiguration.MySqlDbConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class MySqlConfigBase extends HibernateConfigBase {

    @Override
    public String getDatabaseName() {
        return "hibernate";
    }

    @Override
    @Bean
    public DataSource dataSource() {
        return buildDataSource("com.mysql.jdbc.Driver",
                               "jdbc:mysql://localhost/" + getDatabaseName(),
                               "root",
                               "root");
    }

    @Override
    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");

        return props;
    }
}
