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

import kr.debop4j.timeperiod.DayOfWeek;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.clock.ClockProxy;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * 일단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 2:08
 */
@Slf4j
public class DayRange extends DayTimeRange {

    // region << Constructor >>

    public DayRange() {
        this(new TimeCalendar());
    }

    public DayRange(ITimeCalendar calendar) {
        this(ClockProxy.getClock().today(), calendar);
    }

    public DayRange(DateTime moment) {
        super(moment, 1);
    }

    public DayRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    public DayRange(int year, int monthOfYear) {
        super(year, monthOfYear, 1, 1);
    }

    public DayRange(int year, int monthOfYear, int dayOfMonth) {
        super(year, monthOfYear, dayOfMonth, 1);
    }

    public DayRange(int year, int monthOfYear, int dayOfMonth, ITimeCalendar calendar) {
        super(year, monthOfYear, dayOfMonth, 1, calendar);
    }

    // endregion

    public int getYear() { return getStartYear(); }

    public int getMonthOfYear() { return getStartMonthOfYear(); }

    public int getDayOfMonth() { return getStartDayOfMonth(); }

    public DayOfWeek getDayOfWeek() { return getStartDayOfWeek(); }

    public DayRange previousDay() {
        return addDays(-1);
    }

    public DayRange nextDay() {
        return addDays(1);
    }

    public DayRange addDays(int days) {
        return new DayRange(getStart().withTimeAtStartOfDay().plusDays(days), getTimeCalendar());
    }

    private static final long serialVersionUID = 7993201574147735665L;
}
