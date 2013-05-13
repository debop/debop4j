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

import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.clock.ClockProxy;
import org.joda.time.DateTime;

/**
 * 분(Minute) 단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 10:26
 */
public class MinuteRange extends MinuteTimeRange {

    private static final long serialVersionUID = 4111802915947727424L;

    // region << Constructor >>

    public MinuteRange() {
        this(new TimeCalendar());
    }

    public MinuteRange(ITimeCalendar timeCalendar) {
        this(ClockProxy.getClock().now(), timeCalendar);
    }

    public MinuteRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    public MinuteRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    public MinuteRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 1);
    }

    public MinuteRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, ITimeCalendar timeCalendar) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 1, timeCalendar);
    }

    // endregion

    public int getYear() { return getStartYear(); }

    public int getMonthOfYear() { return getStartMonthOfYear(); }

    public int getDayOfMonth() { return getStartDayOfMonth(); }

    public int getHourOfDay() { return getStartHourOfDay(); }

    public int getMinuteOfHour() { return getStartMinuteOfHour(); }

    public MinuteRange getPreviousMinute() {
        return addMinutes(-1);
    }

    public MinuteRange getNextMinute() {
        return addMinutes(1);
    }

    public MinuteRange addMinutes(int minutes) {
        DateTime start = getStart().withTimeAtStartOfDay().withTime(getStartHourOfDay(), getStartMinuteOfHour(), 0, 0);
        return new MinuteRange(start.plusMinutes(minutes), getTimeCalendar());
    }
}
