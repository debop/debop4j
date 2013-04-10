package kr.debop4j.data.redis.ogm.dialect;

import kr.debop4j.data.redis.ogm.datastore.redis.impl.RedisDatastoreProvider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LockMode;
import org.hibernate.dialect.lock.LockingStrategy;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.ogm.datastore.spi.Association;
import org.hibernate.ogm.datastore.spi.AssociationContext;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.datastore.spi.TupleContext;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.grid.AssociationKey;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.EntityKeyMetadata;
import org.hibernate.ogm.grid.RowKey;
import org.hibernate.ogm.massindex.batchindexing.Consumer;
import org.hibernate.ogm.type.GridType;
import org.hibernate.persister.entity.Lockable;
import org.hibernate.type.Type;


/**
 * RedisDialect
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 10. 오후 10:08
 */
@Slf4j
public class RedisDialect implements GridDialect {

    private static final long serialVersionUID = -5386185596480321243L;

    private final RedisDatastoreProvider provider;

    public RedisDialect(RedisDatastoreProvider provider) {
        this.provider = provider;
    }

    @Override
    public LockingStrategy getLockingStrategy(Lockable lockable, LockMode lockMode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tuple getTuple(EntityKey key, TupleContext tupleContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tuple createTuple(EntityKey key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTuple(Tuple tuple, EntityKey key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeTuple(EntityKey key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Association getAssociation(AssociationKey key, AssociationContext associationContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Association createAssociation(AssociationKey key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateAssociation(Association association, AssociationKey key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeAssociation(AssociationKey key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tuple createTupleAssociation(AssociationKey associationKey, RowKey rowKey) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nextValue(RowKey key, IntegralDataTypeHolder value, int increment, int initialValue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GridType overrideType(Type type) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void forEachTuple(Consumer consumer, EntityKeyMetadata... entityKeyMetadatas) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
