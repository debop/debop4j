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

package kr.debop4j.data.mapping.model.hbm.join;

import com.google.common.base.Objects;
import kr.debop4j.data.model.EntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 고객 정보
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19.
 */
@Getter
@Setter
public class Join_Customer extends EntityBase<Long> implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 9221823986414874215L;

    protected Join_Customer() {
    }

    public Join_Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    private String name;
    private String email;
    private Date created;

    private Address address = new Address();

    @Setter(value = AccessLevel.PRIVATE)
    @Type(type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType")
    private DateTime updateTimestamp;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return Objects.hashCode(name, email);
    }

    @Override
    public Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("email", email)
                .add("created", created);
    }

    @Override
    public void updateUpdateTimestamp() {
        this.updateTimestamp = DateTime.now();
    }
}
