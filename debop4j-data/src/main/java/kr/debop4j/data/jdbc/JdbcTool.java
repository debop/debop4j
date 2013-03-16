package kr.debop4j.data.jdbc;

import com.jolbox.bonecp.BoneCPDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * Jdbc 관련 Utility class
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 26
 */
@Slf4j
public class JdbcTool {

    private JdbcTool() {}

    public static javax.sql.DataSource getDataSource(String driverClass, String url, String username, String passwd) {
        return getTomcatDataSource(driverClass, url, username, passwd);
    }

    /**
     * DataSource 를 빌드합니다.
     */
    public static javax.sql.DataSource getBoneCpDataSource(String driverClass, String url, String username, String passwd) {
        if (log.isDebugEnabled())
            log.debug("build BoneCP DataSource... driverClass=[{}], url=[{}], username=[{}], passwd=[{}]",
                      driverClass, url, username, passwd);

        BoneCPDataSource ds = new BoneCPDataSource();

        ds.setDriverClass(driverClass);
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(passwd);

        ds.setPartitionCount(3);
        ds.setMaxConnectionsPerPartition(200);
        ds.setMinConnectionsPerPartition(10);
        ds.setReleaseHelperThreads(6);
        ds.setAcquireIncrement(5);

        ds.setIdleConnectionTestPeriodInMinutes(1);

        return ds;
    }

    /**
     * DataSource 를 빌드합니다.
     */
    public static javax.sql.DataSource getTomcatDataSource(String driverClass, String url, String username, String passwd) {
        if (log.isDebugEnabled())
            log.debug("build Tomcat pool DataSource... driverClass=[{}], url=[{}], username=[{}], passwd=[{}]",
                      driverClass, url, username, passwd);

        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(driverClass);
        p.setUsername(username);
        p.setPassword(passwd);

        p.setJmxEnabled(true);
        p.setTestWhileIdle(true);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(200);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);

        DataSource ds = new DataSource(p);
        return ds;
    }

    /**
     * 테스트에 사용하기 위해 메모리를 사용하는 HSql DB 에 대한 DataSource 를 반환합니다.
     */
    public static javax.sql.DataSource getEmbeddedHsqlDataSource() {
        return getDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:test", "sa", "");
    }
}
