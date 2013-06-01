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
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;

/**
 * 특정 요일의 시간의 범위를 표현합니다. (예: 월요일 13시 ~ 17시)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 5:07
 */
public class DayHourRange extends HourRangeInDay {

    private static final long serialVersionUID = 2595125864993419600L;

    @Getter private final DayOfWeek dayOfWeek;

    /**
     * Instantiates a new Day hour range.
     *
     * @param dayOfWeek the day of week
     * @param hourOfDay the hour of day
     */
    public DayHourRange(DayOfWeek dayOfWeek, int hourOfDay) {
        super(hourOfDay);
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Instantiates a new Day hour range.
     *
     * @param dayOfWeek      the day of week
     * @param startHourOfDay the start hour of day
     * @param endHOurOfDay   the end h our of day
     */
    public DayHourRange(DayOfWeek dayOfWeek, int startHourOfDay, int endHOurOfDay) {
        super(startHourOfDay, endHOurOfDay);
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(dayOfWeek, getStart(), getEnd());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("dayOfWeek", dayOfWeek);
    }
}
