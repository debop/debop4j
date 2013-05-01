package kr.debop4j.core.pool;

/**
 * kr.debop4j.core.pool.PoolConfig
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 8. 오전 10:50
 */
public class PoolConfig extends AbstractPoolConfig {

    public PoolConfig() {
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60_000); // 60 sec
        setTimeBetweenEvictionRunsMillis(30_000);
        setNumTestsPerEvictionRun(-1);
    }
}
