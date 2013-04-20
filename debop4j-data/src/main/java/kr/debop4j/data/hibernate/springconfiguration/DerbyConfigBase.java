package kr.debop4j.data.hibernate.springconfiguration;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Derby Embedded DB 에 대한 Hibernate 관련 Configuration 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 22.
 */
public abstract class DerbyConfigBase extends HibernateConfigBase {

    protected String getDatabaseName() {
        return "memory";
    }

    @Bean(destroyMethod = "close")
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
