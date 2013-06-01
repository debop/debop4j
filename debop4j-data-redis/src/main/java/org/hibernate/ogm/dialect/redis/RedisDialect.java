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

package org.hibernate.ogm.dialect.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.hibernate.LockMode;
import org.hibernate.dialect.lock.LockingStrategy;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.ogm.datastore.impl.EmptyTupleSnapshot;
import org.hibernate.ogm.datastore.impl.MapHelpers;
import org.hibernate.ogm.datastore.map.impl.MapAssociationSnapshot;
import org.hibernate.ogm.datastore.redis.impl.RedisDatastoreProvider;
import org.hibernate.ogm.datastore.spi.Association;
import org.hibernate.ogm.datastore.spi.AssociationContext;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.datastore.spi.TupleContext;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.grid.AssociationKey;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.RowKey;
import org.hibernate.ogm.type.ByteStringType;
import org.hibernate.ogm.type.GridType;
import org.hibernate.ogm.type.StringCalendarDateType;
import org.hibernate.persister.entity.Lockable;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link GridDialect} for Redis
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:14
 */
@Slf4j
public class RedisDialect implements GridDialect {

    private static final long serialVersionUID = 3980191064775489616L;

    private final RedisDatastoreProvider provider;

    private static final String ENTITY_HSET = "OGM-Entity";
    private static final String ASSOCIATION_HSET = "OGM-Association";
    private static final String SEQUENCE_HSET = "OGM-Sequence";

    private final ConcurrentMap<String, EntityKey> entityKeys = new ConcurrentHashMap<String, EntityKey>();
    private final ConcurrentMap<String, AssociationKey> associationKeys = new ConcurrentHashMap<String, AssociationKey>();


    public RedisDialect(RedisDatastoreProvider provider) {
        log.info("RedisDialect를 생성합니다...");
        this.provider = provider;
    }

    @Override
    public LockingStrategy getLockingStrategy(Lockable lockable, LockMode lockMode) {
        // TODO: Redis 의 Locking 기능을 조사해보자.
        throw new RuntimeException("the lock is not supported yet.");
    }

    @Override
    public Tuple getTuple(EntityKey key, TupleContext tupleContext) {
        if (log.isTraceEnabled())
            log.trace("get tuple... key=[{}]", key);

        throw new NotImplementedException("구현 중");
    }

    @Override
    public Tuple createTuple(EntityKey key) {
        Map<String, Object> tuple = new HashMap<String, Object>();
        return new Tuple(new RedisTupleSnapshot(tuple));
    }

    @Override
    public void updateTuple(Tuple tuple, EntityKey key) {
        Map<String, Object> entityRecord = ((RedisTupleSnapshot) tuple.getSnapshot()).getMap();
        MapHelpers.applyTupleOpsOnMap(tuple, entityRecord);

        throw new NotImplementedException("구현 중");
    }

    @Override
    public void removeTuple(EntityKey key) {
        if (log.isTraceEnabled()) log.trace("remove tuple... key=[{}]", key);

        throw new NotImplementedException("구현 중");
    }

    @Override
    public Association getAssociation(AssociationKey key, AssociationContext associationContext) {
        // TODO: 구현 필요
        throw new NotImplementedException("구현 중");
    }

    @Override
    public Association createAssociation(AssociationKey key) {
        Map<RowKey, Map<String, Object>> associationMap = new HashMap<RowKey, Map<String, Object>>();
        return new Association(new MapAssociationSnapshot(associationMap));
    }

    @Override
    public void updateAssociation(Association association, AssociationKey key) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public void removeAssociation(AssociationKey key) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public Tuple createTupleAssociation(AssociationKey associationKey, RowKey rowKey) {
        return new Tuple(EmptyTupleSnapshot.SINGLETON);
    }

    @Override
    public void nextValue(RowKey key, IntegralDataTypeHolder value, int increment, int initialValue) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public GridType overrideType(Type type) {
        // Override handling of calendar types
        if (type == StandardBasicTypes.CALENDAR || type == StandardBasicTypes.CALENDAR_DATE) {
            return StringCalendarDateType.INSTANCE;
        } else if (type == StandardBasicTypes.BYTE) {
            return ByteStringType.INSTANCE;
        }
        return null; // all other types handled as in hibernate-ogm-core
    }

//    @Override
//    public void forEachTuple(Consumer consumer, EntityKeyMetadata... entityKeyMetadatas) {
//        throw new NotImplementedException("구현 중");
//    }
}
