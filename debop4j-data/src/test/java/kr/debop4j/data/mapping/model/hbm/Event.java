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
import com.google.common.base.Strings;
import kr.debop4j.data.model.EntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.Date;

@Getter
@Setter
public class Event extends EntityBase<Long> implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 930773110476290116L;

    public Event() {
    }

    public Event(String title, Date date) {

        assert !Strings.isNullOrEmpty(title);

        this.title = title;
        this.date = date;
    }

    private Date date;
    private String title;
    private Category category;

    @Setter(value = AccessLevel.PROTECTED)
    @Type(type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType")
    private DateTime updateTimestamp;

    public void updateUpdateTimestamp() {
        updateTimestamp = DateTime.now();
    }


    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return Objects.hashCode(title, date);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("title", title)
                .add("moment", date)
                .add("updateTimestamp", updateTimestamp)
                .add("categoryId", category.getId());
    }
}

