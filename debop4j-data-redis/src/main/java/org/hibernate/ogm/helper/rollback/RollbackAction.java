package org.hibernate.ogm.helper.rollback;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.ogm.datastore.redis.impl.RedisDatastoreProvider;
import org.hibernate.ogm.helper.memento.Memento;
import org.hibernate.ogm.helper.memento.Originator;

/**
 * org.hibernate.ogm.helper.rollback.RollbackAction
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:28
 */
public class RollbackAction {

    private final RedisDatastoreProvider provider;

    public RollbackAction(RedisDatastoreProvider provider, Object key) {
        this.provider = provider;
    }

    /** 이전 상태로 롤백합니다. */
    public void rollback() {

    }

    private Object[] setSavePoint(Object key) {
        Object[] objs = new Object[2];
        Originator o = new Originator();
        Memento memento = null;

        throw new NotImplementedException("구현 중");

    }
}
