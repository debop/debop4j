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
import kr.debop4j.data.model.EntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Category extends EntityBase<Long> implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 7583780980623927361L;

    protected Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @Getter
    @Setter
    private String name;

    @Getter
    @Type( type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType" )
    private DateTime updateTimestamp;

    public void updateUpdateTimestamp() {
        updateTimestamp = DateTime.now();
    }

    @Getter
    private final List<Event> events = new ArrayList<>();

    public void addEvents(Event... ets) {
        for (Event e : ets) {
            events.add(e);
            e.setCategory(this);
        }
    }

    public void removeEvents(Event... ets) {
        for (Event e : ets) {
            events.remove(e);
            e.setCategory(null);
        }
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return Objects.hashCode(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("updateTimestamp", updateTimestamp);
    }
}
