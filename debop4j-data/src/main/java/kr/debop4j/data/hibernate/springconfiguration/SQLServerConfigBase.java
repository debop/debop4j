package kr.debop4j.data.hibernate.springconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * kr.debop4j.data.hibernate.springconfiguration.SQLServerConfigBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 22.
 */
@Configuration
@EnableTransactionManagement
public abstract class SQLServerConfigBase extends HibernateConfigBase {
    @Override
    protected String getDatabaseName() {
        return "hibernate";
    }

    @Override
    public DataSource dataSource() {
        return buildDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                               "jdbc:sqlserver://localhost/" + getDatabaseName() + ";integratedSecurity=true;",
                               "sa",
                               "sa");
    }
}
