package kr.debop4j.data.hibernate.springconfiguration;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Derby Embedded DB 에 대한 Hibernate 관련 Configuration 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 22.
 */
@Configuration
@EnableTransactionManagement
public abstract class DerbyConfigBase extends HibernateConfigBase {

    protected String getDatabaseName() {
        return "memory";
    }

    @Bean
    public DataSource dataSource() {
        return buildDataSource("org.apache.derby.jdbc.EmbeddedDriver",
                               "jdbc:derby:" + getDatabaseName() + ":test;create=true;",
                               "",
                               "");
    }

    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.DerbyTenSevenDialect");

        return props;
    }
}
