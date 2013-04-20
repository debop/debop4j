package kr.debop4j.data.hibernate.springconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * kr.debop4j.data.hibernate.springconfiguration.SQLServerConfigBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 22.
 */
@Configuration
@EnableTransactionManagement
public abstract class SQLServerConfigBase extends HibernateConfigBase {

    protected String getDatabaseName() {
        return "hibernate";
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return buildDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                               "jdbc:sqlserver://localhost/" + getDatabaseName() + ";integratedSecurity=true;",
                               "sa",
                               "sa");
    }
}
