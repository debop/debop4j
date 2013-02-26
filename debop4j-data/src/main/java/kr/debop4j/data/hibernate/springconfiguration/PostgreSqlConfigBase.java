package kr.debop4j.data.hibernate.springconfiguration;

import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * PostgreSQL DB를 사용하는 Hibernate 용 환경설정입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class PostgreSqlConfigBase extends HibernateConfigBase {

    @Override
    public String getDatabaseName() {
        return "hibernate";
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("org.postgresql.Driver",
                               "jdbc:postgresql://localhost/" + getDatabaseName() + "?Set=UTF8",
                               "root",
                               "root");
    }

    @Bean
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();

        props.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL82Dialect");

        return props;
    }
}
