package kr.debop4j.data.hibernate.provider;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import java.util.Properties;

/**
 * JDBC connection provider builder with HSQLDB.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 16.
 */
@Deprecated
@Slf4j
public class ConnectionProviderBuilder {

    public static final String DRIVER = "org.hsqldb.jdbcDriver";
    public static final String URL = "jdbc:hsqldb:mem:%s";
    public static final String USER = "sa";
    public static final String PASS = "";

    public static Properties getConnectionProviderProperties() {
        return getConnectionProviderProperties("hibernate");
    }

    public static Properties getConnectionProviderProperties(String dbName) {

        Properties props = new Properties(null);
        props.put(Environment.DRIVER, DRIVER);
        props.put(Environment.URL, String.format(URL, dbName));
        props.put(Environment.USER, USER);
        props.put(Environment.PASS, PASS);

        return props;
    }

    public static DriverManagerConnectionProviderImpl buildConnectionProvider() {
        return buildConnectionProvider(false);
    }

    public static DriverManagerConnectionProviderImpl buildConnectionProvider(String dbName) {
        return buildConnectionProvider(getConnectionProviderProperties(dbName), false);
    }

    public static DriverManagerConnectionProviderImpl buildConnectionProvider(final boolean allowAggressiveRelease) {
        return buildConnectionProvider(getConnectionProviderProperties(), allowAggressiveRelease);
    }

    public static DriverManagerConnectionProviderImpl buildConnectionProvider(Properties props,
                                                                              final boolean allowAggressiveRelease) {
        if (log.isInfoEnabled())
            log.info("Build ConnectionProvider props=[{}], allowAggressiveRelease=[{}]",
                     props, allowAggressiveRelease);

        DriverManagerConnectionProviderImpl connectionProvider =
                new DriverManagerConnectionProviderImpl() {
                    @Override
                    public boolean supportsAggressiveRelease() {
                        return allowAggressiveRelease;
                    }
                };
        connectionProvider.configure(props);
        return connectionProvider;
    }

    public static Dialect getCorrespondingDialect() {
        return new HSQLDialect();
    }
}
