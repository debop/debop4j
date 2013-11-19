/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibernate.ogm.datastore.redis.impl;

import lombok.Getter;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.hibernate.ogm.datastore.redis.Environment;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.redis.RedisDialect;
import org.hibernate.ogm.helper.JsonHelper;
import org.hibernate.ogm.repository.RedisRepository;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Startable;
import org.hibernate.service.spi.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * Redis를 데이터 저장소로 사용하는 DatastoreProvider
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:15
 */
public class RedisDatastoreProvider implements DatastoreProvider, Startable, Stoppable, Configurable {

    private static final long serialVersionUID = 5522668853021833574L;

    private static final Logger log = LoggerFactory.getLogger(RedisDatastoreProvider.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private Properties props = new Properties();
    private AtomicBoolean isCacheStarted = new AtomicBoolean(false);

    private final JsonHelper jsonHelper = new JsonHelper();
    private Map<String, String> requiredProperties;
    private Pattern setterPattern;

    /**
     * JedisPool
     */
    private JedisPool pool;
    /**
     * Jedis Client
     */
    @Getter
    private RedisRepository redisClient;


    @Override
    public void configure(Map configurationValues) {
        if (configurationValues != null) {
            for (Object key : configurationValues.keySet()) {
                props.put(key, configurationValues.get(key));
                if (log.isTraceEnabled())
                    log.trace("Config key=[{}], value=[{}]", key, props.get(key));
            }
        }
    }

    @Override
    public Class<? extends GridDialect> getDefaultDialect() {
        return RedisDialect.class;
    }

    @Override
    public void start() {
        if (isCacheStarted.get())
            return;

        log.info("RedisDatastoreProvider를 시작합니다...");

        try {
            pool = setupJedis();
            redisClient = new RedisRepository(pool);

            isCacheStarted.set(true);
            log.info("RedisDatastoreProvider를 시작했습니다.");

        } catch (Exception e) {
            log.error("Redis용 DatastoreProvider를 시작하는데 예외가 발생했습니다.", e);
            throw new RuntimeException("Redis용 DatastoreProvider를 시작하는데 예외가 발생했습니다.", e);
        }
    }

    @Override
    public void stop() {
        if (!isCacheStarted.get())
            return;
        log.info("RedisDatastoreProvider를 중지합니다...");

        try {
            redisClient = null;
            if (pool != null)
                pool.destroy();

            isCacheStarted.compareAndSet(true, false);

            log.info("RedisDatastoreProvider를 중지했습니다.");
        } catch (Exception e) {
            log.error("Redis용 DatastoreProvider를 중지하는데 예외가 발생했습니다.", e);
            throw new RuntimeException("Redis용 DatastoreProvider를 중지하는데 예외가 발생했습니다.", e);
        }
    }

    private synchronized JedisPool setupJedis() {

        String host = props.getProperty(Environment.REDIS_HOST, Environment.REDIS_DEFAULT_HOST);
        String port = props.getProperty(Environment.REDIS_PORT, Environment.REDIS_DEFAULT_PORT);
        String timeout = props.getProperty(Environment.REDIS_TIMEOUT, Environment.REDIS_DEFAULT_TIMEOUT);
        String password = props.getProperty(Environment.REDIS_PASSWORD, Environment.REDIS_DEFAULT_PASSWORD);
        String database = props.getProperty(Environment.REDIS_DATABASE, Environment.REDIS_DEFAULT_DATABASE);

        if (log.isDebugEnabled())
            log.debug("Create JedisPool... host=[{}], port=[{}], timeout=[{}], password=[{}], database=[{}]",
                      host, port, timeout, password, database);

        return new JedisPool(getPoolConfig(),
                             host,
                             Integer.decode(port),
                             Integer.decode(timeout),
                             password,
                             Integer.decode(database));
    }

    private GenericObjectPool.Config getPoolConfig() {
        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();

        // TODO: 여기에 환경설정에서 읽어오도록 한다.

        return poolConfig;
    }
}
