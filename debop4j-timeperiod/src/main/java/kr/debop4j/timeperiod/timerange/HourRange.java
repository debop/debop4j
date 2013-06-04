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
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

/**
 * 시(Hour) 단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오전 11:12
 */
public class HourRange extends HourTimeRange {

    // region << Contructor >>

    /** Instantiates a new Hour range. */
    public HourRange() {
        this(new TimeCalendar());
    }

    /**
     * Instantiates a new Hour range.
     *
     * @param timeCalendar the time calendar
     */
    public HourRange(ITimeCalendar timeCalendar) {
        this(Times.now(), timeCalendar);
    }

    /**
     * Instantiates a new Hour range.
     *
     * @param moment the moment
     */
    public HourRange(DateTime moment) {
        super(moment, 1);
    }

    /**
     * Instantiates a new Hour range.
     *
     * @param moment   the moment
     * @param calendar the calendar
     */
    public HourRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    /**
     * Instantiates a new Hour range.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @param dayOfMonth  the day of month
     * @param hourOfDay   the hour of day
     */
    public HourRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, 1);
    }

    /**
     * Instantiates a new Hour range.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @param dayOfMonth  the day of month
     * @param hourOfDay   the hour of day
     * @param calendar    the calendar
     */
    public HourRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, ITimeCalendar calendar) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, 1, calendar);
    }

    // endregion

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() { return getStartYear(); }

    /**
     * Gets month of year.
     *
     * @return the month of year
     */
    public int getMonthOfYear() { return getStartMonthOfYear(); }

    /**
     * Gets day of month.
     *
     * @return the day of month
     */
    public int getDayOfMonth() { return getStartDayOfMonth(); }

    /**
     * Gets hour of day.
     *
     * @return the hour of day
     */
    public int getHourOfDay() { return getStartHourOfDay(); }

    /**
     * Previous hour.
     *
     * @return the hour range
     */
    public HourRange previousHour() {
        return addHours(-1);
    }

    /**
     * Next hour.
     *
     * @return the hour range
     */
    public HourRange nextHour() {
        return addHours(1);
    }

    /**
     * Add hours.
     *
     * @param hours the hours
     * @return the hour range
     */
    public HourRange addHours(int hours) {
        DateTime startHour = Times.trimToHour(getStart(), getStartHourOfDay());
        return new HourRange(startHour.plusHours(hours), getTimeCalendar());
    }

    private static final long serialVersionUID = 2876823794105220883L;
}
