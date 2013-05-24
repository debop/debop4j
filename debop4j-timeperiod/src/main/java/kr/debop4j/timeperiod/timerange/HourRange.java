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
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

/**
 * 시(Hour) 단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오전 11:12
 */
public class HourRange extends HourTimeRange {

    private static final long serialVersionUID = 2876823794105220883L;

    // region << Contructor >>

    public HourRange() {
        this(new TimeCalendar());
    }

    public HourRange(ITimeCalendar timeCalendar) {
        this(ClockProxy.getClock().now(), timeCalendar);
    }

    public HourRange(DateTime moment) {
        super(moment, 1);
    }

    public HourRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    public HourRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, 1);
    }

    public HourRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, ITimeCalendar calendar) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, 1, calendar);
    }

    // endregion

    public int getYear() { return getStartYear(); }

    public int getMonthOfYear() { return getStartMonthOfYear(); }

    public int getDayOfMonth() { return getStartDayOfMonth(); }

    public int getHourOfDay() { return getStartHourOfDay(); }

    public HourRange previousHour() {
        return addHours(-1);
    }

    public HourRange nextHour() {
        return addHours(1);
    }

    public HourRange addHours(int hours) {
        DateTime startHour = Times.trimToHour(getStart(), getStartHourOfDay());
        return new HourRange(startHour.plusHours(hours), getTimeCalendar());
    }
}
