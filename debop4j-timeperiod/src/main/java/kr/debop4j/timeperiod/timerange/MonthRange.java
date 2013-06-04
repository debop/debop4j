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
 * 1개월의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 8:16
 */
public class MonthRange extends MonthTimeRange {

    // region << Constructor >>

    /** Instantiates a new Month range. */
    public MonthRange() {
        this(new TimeCalendar());
    }

    /**
     * Instantiates a new Month range.
     *
     * @param calendar the calendar
     */
    public MonthRange(ITimeCalendar calendar) {
        this(Times.today(), calendar);
    }

    /**
     * Instantiates a new Month range.
     *
     * @param moment the moment
     */
    public MonthRange(DateTime moment) {
        super(moment, 1);
    }

    /**
     * Instantiates a new Month range.
     *
     * @param moment   the moment
     * @param calendar the calendar
     */
    public MonthRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    /**
     * Instantiates a new Month range.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     */
    public MonthRange(int year, int monthOfYear) {
        super(year, monthOfYear, 1);
    }

    /**
     * Instantiates a new Month range.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @param calendar    the calendar
     */
    public MonthRange(int year, int monthOfYear, ITimeCalendar calendar) {
        super(year, monthOfYear, 1, calendar);
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
     * Gets days in month.
     *
     * @return the days in month
     */
    public int getDaysInMonth() {
        return Times.getDaysInMonth(getStartYear(), getStartMonthOfYear());
    }

    /**
     * Previous month.
     *
     * @return the month range
     */
    public MonthRange previousMonth() {
        return addMonths(-1);
    }

    /**
     * Next month.
     *
     * @return the month range
     */
    public MonthRange nextMonth() {
        return addMonths(1);
    }

    /**
     * Add months.
     *
     * @param months the months
     * @return the month range
     */
    public MonthRange addMonths(int months) {
        return new MonthRange(Times.startTimeOfMonth(getStart()).plusMonths(months), getTimeCalendar());
    }

    private static final long serialVersionUID = 6337203416072219224L;
}
