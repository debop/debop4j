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

import com.google.common.base.Defaults;
import com.google.common.base.Objects;
import kr.debop4j.core.tools.ReflectTool;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Hibernate 기본 도메인 엔티티의 추상화 클래스입니다.
 * Jpa@author sunghyouk.bae@gmail.com
 *
 * @since 12. 11. 19
 */
@Slf4j
@MappedSuperclass
public abstract class EntityBase<TId extends Serializable> extends StatefulEntityBase implements IEntity<TId> {

    private static final long serialVersionUID = 4766509654284022534L;

    @Id
    @GeneratedValue
    private TId id;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <TId> TId getId() {
        return (TId) this.id;
    }

    protected void setId(TId id) {
        this.id = id;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        boolean sameType = (obj != null) && (getClass() == obj.getClass());

        if (sameType) {
            EntityBase<TId> entity = (EntityBase<TId>) obj;
            return hasSameNonDefaultIdAs(entity) ||
                    ((!isPersisted() || entity.isPersisted()) && hashSameBusinessSignature(entity));
        }
        return false;
    }

    @PostPersist
    public final void postPersist() {
        onPersist();
    }

    @PostLoad
    public final void postLoad() {
        onLoad();
    }

    /**
     * Entity의 HashCode 를 제공합니다. 저장소에 저장된 엔티티의 경우는 Identifier 의 HashCode를 제공합니다.
     *
     * @return hash code
     */
    public int hashCode() {
        if (id == null || !isPersisted())
            System.identityHashCode(this);

        return Objects.hashCode(id);
    }

    private boolean hasSameNonDefaultIdAs(IEntity<TId> entity) {

        try {
            Class<TId> idClass = ReflectTool.getGenericParameterType(this);

            TId defaultValue = Defaults.defaultValue(idClass); //Activators.createInstance(idClass);

            boolean idHasValue = !java.util.Objects.equals(id, defaultValue);
            if (idHasValue) {
                boolean entityIdHasValue = !java.util.Objects.equals(entity.getId(), defaultValue);

                if (entityIdHasValue)
                    return java.util.Objects.equals(id, entity.getId());
            }
        } catch (Exception ex) {
            log.error("Identifier 값 비교 시 예외 발생. entity=" + entity, ex);
        }
        return false;
    }

    private boolean hashSameBusinessSignature(IEntity<TId> other) {
        return (other != null) && (hashCode() == other.hashCode());
    }
}
