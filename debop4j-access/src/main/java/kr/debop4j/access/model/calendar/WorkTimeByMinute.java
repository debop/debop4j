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

package kr.debop4j.access.model.calendar;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * kr.debop4j.access.model.calendar.WorkTimeByMinute
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 11.
 */
@Entity
@Table(name = "WorkTimeByMinute")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class WorkTimeByMinute extends WorkTimeByTimeBase {
    private static final long serialVersionUID = 5542870547481028168L;

    protected WorkTimeByMinute() {}

    public WorkTimeByMinute(WorkCalendar workCalendar, Date workMinute) {
        super(workCalendar, workMinute);
    }

    @Id
    @GeneratedValue
    @Column(name = "WorkTimeId")
    private Long id;

    @Transient
    public Date getWorkMinute() {
        return super.getWorkTime();
    }

    public void setWorkMinute(Date workMinute) {
        super.setWorkTime(workMinute);
    }


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(getWorkCalendar(), getWorkMinute());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("workHour", getWorkMinute());
    }
}
