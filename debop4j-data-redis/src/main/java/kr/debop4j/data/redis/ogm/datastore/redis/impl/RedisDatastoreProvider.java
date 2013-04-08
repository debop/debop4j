package kr.debop4j.data.redis.ogm.datastore.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.service.spi.Startable;
import org.hibernate.service.spi.Stoppable;

/**
 * hibernate-ogm DatastoreProvider for Redis
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오전 12:06
 */
@Slf4j
public class RedisDatastoreProvider implements DatastoreProvider, Startable, Stoppable {

    private static final long serialVersionUID = 7563680529865220115L;

    @Override
    public Class<? extends GridDialect> getDefaultDialect() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void start() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
