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
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Embeddable;

/**
 * 기간을 표현하는 컴포넌트
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 22.
 */
@Embeddable
@Getter
@Setter
public class DateTimeRange extends ValueObjectBase {

    public static final long ZeroMillis = 0L;
    public static final long MinMillis = 0L;
    public static final long MillisPerDay = 24 * 60 * 60 * 1000;
    public static final long MaxMillis = 3652059L * MillisPerDay - 1;

    public static final DateTime MinPeriodTime = new DateTime(MinMillis);
    public static final DateTime MaxPeriodTime = new DateTime(MaxMillis);

    private static final long serialVersionUID = 3120213434581635267L;

    private DateTime start;
    private DateTime end;
    //private boolean readonly;

    public DateTimeRange() {
        this(MinPeriodTime, MaxPeriodTime);
    }


    public DateTimeRange(DateTime start, DateTime end) {
        this.start = (start != null) ? start : MinPeriodTime;
        this.end = (end != null) ? end : MaxPeriodTime;
    }

    public DateTimeRange(DateTimeRange source) {
        this.start = source.start;
        this.end = source.end;
    }


    @Override
    public int hashCode() {
        return HashTool.compute(start, end);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("start", start)
                    .add("stop", end);
    }
}
