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
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Identifier의 수행이 Integer인 엔티티의 기본 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 23.
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class IntAnnotatedEntityBase extends AnnotatedEntityBase implements IEntity<Integer> {
    private static final long serialVersionUID = -8854746449856715827L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return super.hashCode();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id);
    }
}
