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

import com.google.common.base.Objects;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 엔티티의 Identifier generator가 assigned 인 경우에 사용합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 27.
 */
@MappedSuperclass
public class AssignedIdEntityBase<TId extends Serializable> extends EntityBase<TId> implements IAssignedEntity<TId> {
    private static final long serialVersionUID = 2441448634046269391L;

    protected AssignedIdEntityBase() {}

    protected AssignedIdEntityBase(TId assignedId) {
        super.setId(assignedId);
    }

    /**
     * 엔티티의 Id 를 설정합니다.
     *
     * @param newId 새로운 Id 값
     */
    @Override
    public void setId(TId newId) {
        super.setId(newId);
    }

    /**
     * Entity의 HashCode 를 제공합니다. 저장소에 저장된 엔티티의 경우는 Identifier 의 HashCode를 제공합니다.
     *
     * @return hash code
     */
    public int hashCode() {
        return (getId() != null) ? Objects.hashCode(getId()) : System.identityHashCode(this);
    }
}
