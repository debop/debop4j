package org.hibernate.ogm.datastore.redis;

/**
 * org.hibernate.ogm.datastore.redis.Environment
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:53
 */
public interface Environment {

    /** Redis 서버 주소 */
    public static final String REDIS_HOST = "hibernate.ogm.redis.host";

    /** Redis 서버 Port */
    public static final String REDIS_PORT = "hibernate.ogm.redis.port";

    /** Redis 연결 Timeout */
    public static final String REDIS_TIMEOUT = "hibernate.ogm.redis.timeout";

    /** Redis 비밀번호 */
    public static final String REDIS_PASSWORD = "hiberante.ogm.redis.password";

    /** Ogm 용 데이터베이스 */
    public static final String REDIS_DATABASE = "hiberante.ogm.redis.database";


    /** Redis Host 기본값 (127.0.0.1) */
    public static final String REDIS_DEFAULT_HOST = "127.0.0.1";

    /** Redis 기본 Port 값 (6379) */
    public static final String REDIS_DEFAULT_PORT = "6379";

    /** Redis connection timeout default value (2000: 2 seconds) */
    public static final String REDIS_DEFAULT_TIMEOUT = "2000"; // 2 seconds

    /** Redis connection password (default: null) */
    public static final String REDIS_DEFAULT_PASSWORD = null;

    /** Redis default database (default: 0) */
    public static final String REDIS_DEFAULT_DATABASE = "0";
}
