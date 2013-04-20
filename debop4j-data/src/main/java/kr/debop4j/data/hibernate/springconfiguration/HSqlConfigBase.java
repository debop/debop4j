package kr.debop4j.data.hibernate.springconfiguration;

import kr.debop4j.data.hibernate.forTesting.DatabaseEngine;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * HSql 을 DB로 사용하는 Hibernate Configuration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 21.
 */
public abstract class HSqlConfigBase extends HibernateConfigBase {

    public DatabaseEngine getDatabaseEngine() {
        return DatabaseEngine.HSql;
    }

    public String getDatabaseName() {
        return "mem";
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("org.hsqldb.jdbcDriver",
                               "jdbc:hsqldb:" + getDatabaseName() + ":test",
                               "sa",
                               "");
    }

    protected Properties hibernateProperties() {
        Properties props = super.hibernateProperties();
        props.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
        return props;
    }
}
