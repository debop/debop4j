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

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 지역화 정보를 가지고, 메타 정보도 제공하는 엔티티의 추상 클래스
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 19
 */
@MappedSuperclass
public abstract class LocaleMetaEntityBase<TId extends Serializable, TLocaleValue extends ILocaleValue>
        extends LocaleEntityBase<TId, TLocaleValue> implements IMetaEntity {

    // TODO: metaMap 에 대한 Annotation을 설정해야 하기 때문에 상속 관계를 가지게 하려면 override 를 해야 합니다!!!
    //

    @Getter
    private Map<String, IMetaValue> metaMap = Maps.newLinkedHashMap();

    @Override
    public IMetaValue getMetaValue(String key) {
        return metaMap.get(key);
    }

    @Override
    public Set<String> getMetaKeys() {
        return metaMap.keySet();
    }

    @Override
    public void addMetaValue(String metaKey, IMetaValue metaValue) {
        metaMap.put(metaKey, metaValue);
    }

    @Override
    public void addMetaValue(String metaKey, Object value) {
        metaMap.put(metaKey, new SimpleMetaValue(value));
    }

    @Override
    public void removeMetaValue(String metaKey) {
        metaMap.remove(metaKey);
    }

    private static final long serialVersionUID = -3409892017189966160L;
}
