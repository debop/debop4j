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
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.timerange.YearRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 14. 오전 12:02
 */
@Slf4j
public class YearRange extends YearTimeRange {

    // region << Constructor >>

    /** Instantiates a new Year range. */
    public YearRange() {
        this(new TimeCalendar());
    }

    /**
     * Instantiates a new Year range.
     *
     * @param calendar the calendar
     */
    public YearRange(ITimeCalendar calendar) {
        this(Times.now().withTimeAtStartOfDay(), calendar);
    }

    /**
     * Instantiates a new Year range.
     *
     * @param moment the moment
     */
    public YearRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    /**
     * Instantiates a new Year range.
     *
     * @param moment   the moment
     * @param calendar the calendar
     */
    public YearRange(DateTime moment, ITimeCalendar calendar) {
        this(moment.getYear(), calendar);
    }

    /**
     * Instantiates a new Year range.
     *
     * @param startYear the start year
     */
    public YearRange(int startYear) {
        super(startYear, 1, new TimeCalendar());
    }

    /**
     * Instantiates a new Year range.
     *
     * @param startYear the start year
     * @param calendar  the calendar
     */
    public YearRange(int startYear, ITimeCalendar calendar) {
        super(startYear, 1, calendar);
    }

    // endregion

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return getStartYear();
    }

    /**
     * Previous year.
     *
     * @return the year range
     */
    public YearRange previousYear() {
        return addYears(-1);
    }

    /**
     * Next year.
     *
     * @return the year range
     */
    public YearRange nextYear() {
        return addYears(1);
    }

    /**
     * Add years.
     *
     * @param years the years
     * @return the year range
     */
    public YearRange addYears(int years) {
        DateTime baseTime = Times.startTimeOfYear(getStartYear());
        return new YearRange(baseTime.plusYears(years), getTimeCalendar());
    }

    private static final long serialVersionUID = 709289105887324670L;
}
