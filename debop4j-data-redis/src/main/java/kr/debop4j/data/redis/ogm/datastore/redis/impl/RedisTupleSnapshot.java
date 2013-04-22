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
