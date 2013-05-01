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

package kr.debop4j.data.model;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 트리 구조를 가지며, 메타정보를 제공하는 엔티티
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 19
 */
public abstract class TreeMetaEntityBase<T extends IEntity<TId> & ITreeEntity<T>, TId extends Serializable>
        extends TreeEntityBase<T, TId> implements IMetaEntity {

    private static final long serialVersionUID = -7475565290976767358L;

    @Getter
    private final Map<String, IMetaValue> metaMap = Maps.newLinkedHashMap();

    @Override
    public IMetaValue getMetaValue(String key) {
        return getMetaMap().get(key);
    }

    @Override
    public Set<String> getMetaKeys() {
        return getMetaMap().keySet();
    }

    @Override
    public void addMetaValue(String metaKey, IMetaValue metaValue) {
        getMetaMap().put(metaKey, metaValue);
    }

    @Override
    public void addMetaValue(String metaKey, Object value) {
        getMetaMap().put(metaKey, new SimpleMetaValue(value));
    }

    @Override
    public void removeMetaValue(String metaKey) {
        getMetaMap().remove(metaKey);
    }
}
