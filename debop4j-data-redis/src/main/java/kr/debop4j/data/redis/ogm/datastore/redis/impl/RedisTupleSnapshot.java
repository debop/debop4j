package kr.debop4j.data.redis.ogm.datastore.redis.impl;

import org.hibernate.ogm.datastore.spi.TupleSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * RedisTupleSnapshot
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 10. 오후 10:08
 */
public class RedisTupleSnapshot implements TupleSnapshot {

    private Map<String, Object> map;

    public RedisTupleSnapshot(Map<String, Object> map) {
        this.map = (map != null) ? map : new HashMap<String, Object>();
    }

    @Override
    public Object get(String column) {
        return map.get(column);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<String> getColumnNames() {
        return map.keySet();
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
