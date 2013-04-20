package kr.debop4j.data.hibernate.springconfiguration;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * H2 Embedded DB를 사용하는 Hibernate 설정정입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 21.
 */
public abstract class H2ConfigBase extends HibernateConfigBase {

    @Override
    protected String getDatabaseName() {
        return "mem";
    }

    @Override
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("org.h2.Driver",
                               "jdbc:h2:" + getDatabaseName() + ":test;DB_CLOSE_DELAY=-1",
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
