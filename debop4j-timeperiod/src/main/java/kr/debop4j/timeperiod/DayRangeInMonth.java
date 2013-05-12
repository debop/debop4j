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

package kr.debop4j.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;

/**
 * 동일 월 내에서의 일단위 범위를 나타냅니다. (예: 3일 ~ 5일)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 4:59
 */
public class DayRangeInMonth extends ValueObjectBase implements Comparable<DayRangeInMonth> {
    private static final long serialVersionUID = 1287074885972003104L;

    @Getter private final int startDayOfMonth;
    @Getter private int endDayOfMonth;

    public DayRangeInMonth(int startDayOfMonth, int endDayOfMonth) {
        assertValidDayRange(startDayOfMonth);
        assertValidDayRange(endDayOfMonth);
        if (startDayOfMonth <= endDayOfMonth) {
            this.startDayOfMonth = startDayOfMonth;
            this.endDayOfMonth = endDayOfMonth;
        } else {
            this.startDayOfMonth = endDayOfMonth;
            this.endDayOfMonth = startDayOfMonth;
        }
    }

    public boolean isSingleDay() {
        return startDayOfMonth == endDayOfMonth;
    }

    public boolean hasInside(int dayOfMonth) {
        return startDayOfMonth <= dayOfMonth && dayOfMonth <= endDayOfMonth;
    }

    void assertValidDayRange(int dayOfMonth) {
        assert dayOfMonth > 0 && dayOfMonth <= TimeSpec.MaxDaysPerMonth
                : "dayOfMonth는 1~31 사이의 값이여야 합니다. dayOfMonth=" + dayOfMonth;
    }

    @Override
    public int compareTo(DayRangeInMonth o) {
        return startDayOfMonth - o.getStartDayOfMonth();
    }

    @Override
    public int hashCode() {
        return HashTool.compute(startDayOfMonth, endDayOfMonth);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("startDayOfMonth", startDayOfMonth)
                .add("endDayOfMonth", endDayOfMonth);
    }
}
