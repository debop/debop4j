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

package kr.debop4j.data.mapping.model.hbm;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;


/**
 * JPA 기본 엔티티 예
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19
 */
@Slf4j
@Getter
@Setter
@Entity
@Table(name = "STATE_ENTITY")
public class StatefulEntityImpl extends JpaEntityBase {

    private static final long serialVersionUID = 6927281191366376283L;

    protected StatefulEntityImpl() {
    }

    public StatefulEntityImpl(final String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "ENTITY_ID")
    private Long id;


    @Column(name = "STATE_NAME", nullable = false, length = 128)
    @org.hibernate.annotations.Index(name = "IX_STATE_ENTITY_NAME")
    //@Access(value=AccessType.FIELD)
    private String name;

    @Getter
    @Type( type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType" )
    private DateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void updateLastUpdated() {

        if (StatefulEntityImpl.log.isDebugEnabled())
            StatefulEntityImpl.log.debug("PrePersist, PreUpdate event 발생...");

        lastUpdated = DateTime.now();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return Objects.hashCode(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("lastUpdated", lastUpdated);
    }
}
