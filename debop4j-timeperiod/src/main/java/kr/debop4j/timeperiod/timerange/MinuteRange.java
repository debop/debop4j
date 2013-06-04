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
 * 분(Minute) 단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 10:26
 */
public class MinuteRange extends MinuteTimeRange {

    private static final long serialVersionUID = 4111802915947727424L;

    // region << Constructor >>

    /** Instantiates a new Minute range. */
    public MinuteRange() {
        this(new TimeCalendar());
    }

    /**
     * Instantiates a new Minute range.
     *
     * @param timeCalendar the time calendar
     */
    public MinuteRange(ITimeCalendar timeCalendar) {
        this(Times.now(), timeCalendar);
    }

    /**
     * Instantiates a new Minute range.
     *
     * @param moment the moment
     */
    public MinuteRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    /**
     * Instantiates a new Minute range.
     *
     * @param moment   the moment
     * @param calendar the calendar
     */
    public MinuteRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    /**
     * Instantiates a new Minute range.
     *
     * @param year         the year
     * @param monthOfYear  the month of year
     * @param dayOfMonth   the day of month
     * @param hourOfDay    the hour of day
     * @param minuteOfHour the minute of hour
     */
    public MinuteRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 1);
    }

    /**
     * Instantiates a new Minute range.
     *
     * @param year         the year
     * @param monthOfYear  the month of year
     * @param dayOfMonth   the day of month
     * @param hourOfDay    the hour of day
     * @param minuteOfHour the minute of hour
     * @param timeCalendar the time calendar
     */
    public MinuteRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, ITimeCalendar timeCalendar) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 1, timeCalendar);
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
     * Gets minute of hour.
     *
     * @return the minute of hour
     */
    public int getMinuteOfHour() { return getStartMinuteOfHour(); }

    /**
     * Previous minute.
     *
     * @return the minute range
     */
    public MinuteRange previousMinute() {
        return addMinutes(-1);
    }

    /**
     * Next minute.
     *
     * @return the minute range
     */
    public MinuteRange nextMinute() {
        return addMinutes(1);
    }

    /**
     * Add minutes.
     *
     * @param minutes the minutes
     * @return the minute range
     */
    public MinuteRange addMinutes(int minutes) {
        DateTime start = getStart().withTimeAtStartOfDay().withTime(getStartHourOfDay(), getStartMinuteOfHour(), 0, 0);
        return new MinuteRange(start.plusMinutes(minutes), getTimeCalendar());
    }
}
