package kr.debop4j.data.mapping.northwind.config;

import kr.debop4j.data.hibernate.springconfiguration.PostgreSqlConfigBase;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * PostgreSQL
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 23.
 */
@Configuration
@EnableTransactionManagement
public class PostgreSqlConfig extends PostgreSqlConfigBase {

    @Override
    public String getDatabaseName() {
        return "Northwind";
    }

    @Override
    public Properties hibernateProperties() {
        Properties props = super.hibernateProperties();
        props.put(Environment.HBM2DDL_AUTO, "none"); // create | spawn | spawn-drop | update | validate
        return props;
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                kr.debop4j.data.mapping.northwind.model.Order.class.getPackage().getName()
        };
    }
}
