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

package kr.debop4j.timeperiod.timerange;

import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.timeperiod.TimePart;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 1일 범위 내에서의 시간 범위를 표현합니다. (예 1시~19시, 0시~23시)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오전 11:30
 */
public class HourRangeInDay extends ValueObjectBase implements Comparable<HourRangeInDay>, Serializable {

    private static final long serialVersionUID = -5824233675219678490L;

    @Getter private final TimePart start;
    @Getter private final TimePart end;

    public HourRangeInDay(int hourOfDay) {
        this(hourOfDay, hourOfDay);
    }

    public HourRangeInDay(int startHourOfDay, int endHourOfDay) {
        this(new TimePart(startHourOfDay), new TimePart(endHourOfDay));
    }

    public HourRangeInDay(TimePart start, TimePart end) {
        if (start.compareTo(end) <= 0) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
    }

    public boolean isMoment() {
        return Objects.equals(start, end);
    }

    public boolean hasInside(int hourOfDay) {
        return start.getHourOfDay() <= hourOfDay && hourOfDay <= end.getHourOfDay();
    }

    public boolean hasInside(TimePart time) {
        return time.compareTo(start) >= 0 && time.compareTo(end) <= 0;
    }

    @Override
    public int compareTo(HourRangeInDay o) {
        return start.compareTo(o.getStart());
    }


    @Override
    public int hashCode() {
        return start.getHourOfDay() * 10000 + end.getHourOfDay();
    }

    @Override
    protected com.google.common.base.Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("start", start)
                .add("end", end);
    }
}
