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

import com.google.common.collect.Lists;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 분단위의 기간의 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 10:46
 */
public class MinuteRangeCollection extends MinuteTimeRange {

    private static final long serialVersionUID = -5566298718095890768L;

    /**
     * Instantiates a new Minute range collection.
     *
     * @param moment      the moment
     * @param minuteCount the minute count
     */
    public MinuteRangeCollection(DateTime moment, int minuteCount) {
        super(moment, minuteCount);
    }

    /**
     * Instantiates a new Minute range collection.
     *
     * @param moment      the moment
     * @param minuteCount the minute count
     * @param calendar    the calendar
     */
    public MinuteRangeCollection(DateTime moment, int minuteCount, ITimeCalendar calendar) {
        super(moment, minuteCount, calendar);
    }

    /**
     * Instantiates a new Minute range collection.
     *
     * @param year         the year
     * @param monthOfYear  the month of year
     * @param dayOfMonth   the day of month
     * @param hourOfDay    the hour of day
     * @param minuteOfHour the minute of hour
     * @param minuteCount  the minute count
     */
    public MinuteRangeCollection(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int minuteCount) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, minuteCount);
    }

    /**
     * Instantiates a new Minute range collection.
     *
     * @param year         the year
     * @param monthOfYear  the month of year
     * @param dayOfMonth   the day of month
     * @param hourOfDay    the hour of day
     * @param minuteOfHour the minute of hour
     * @param minuteCount  the minute count
     * @param calendar     the calendar
     */
    public MinuteRangeCollection(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int minuteCount, ITimeCalendar calendar) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, minuteCount, calendar);
    }

    @Getter(lazy = true)
    private final List<MinuteRange> minutes = createMinutes();

    /** 기간을 분단위의 기간으로 표현한 클래스인 {@link MinuteRange}의 열거자를 반환합니다. */
    public List<MinuteRange> createMinutes() {
        int minuteCount = getMinuteCount();
        final List<MinuteRange> minutes = Lists.newArrayListWithCapacity(minuteCount);
        final DateTime startMinute = Times.trimToSecond(getStart());

        for (int i = 0; i < minuteCount; i++) {
            minutes.add(new MinuteRange(startMinute.plusMinutes(i), getTimeCalendar()));
        }
        return minutes;
    }
}
