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

import kr.debop4j.timeperiod.Halfyear;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.YearAndHalfyear;
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

/**
 * 반기를 기간으로 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 11:12
 */
public class HalfyearRange extends HalfyearTimeRange {

    private static final long serialVersionUID = -2193932067151170186L;

    // region << Constructor >>

    /** Instantiates a new Halfyear range. */
    public HalfyearRange() {
        this(new TimeCalendar());
    }

    /**
     * Instantiates a new Halfyear range.
     *
     * @param calendar the calendar
     */
    public HalfyearRange(ITimeCalendar calendar) {
        this(Times.now().withTimeAtStartOfDay(), calendar);
    }

    /**
     * Instantiates a new Halfyear range.
     *
     * @param moment the moment
     */
    public HalfyearRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    /**
     * Instantiates a new Halfyear range.
     *
     * @param moment   the moment
     * @param calendar the calendar
     */
    public HalfyearRange(DateTime moment, ITimeCalendar calendar) {
        this(Times.getYearOf(calendar.getYear(moment), calendar.getMonthOfYear(moment)),
             Times.getHalfyearOfMonth(moment.getMonthOfYear()), calendar);
    }

    /**
     * Instantiates a new Halfyear range.
     *
     * @param startYear     the start year
     * @param startHalfyear the start halfyear
     */
    public HalfyearRange(int startYear, Halfyear startHalfyear) {
        super(startYear, startHalfyear, 1, new TimeCalendar());
    }

    /**
     * Instantiates a new Halfyear range.
     *
     * @param startYear     the start year
     * @param startHalfyear the start halfyear
     * @param calendar      the calendar
     */
    public HalfyearRange(int startYear, Halfyear startHalfyear, ITimeCalendar calendar) {
        super(startYear, startHalfyear, 1, calendar);
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
     * Gets halfyear.
     *
     * @return the halfyear
     */
    public Halfyear getHalfyear() {
        return getStartHalfyear();
    }

    /**
     * Previous halfyear.
     *
     * @return the halfyear range
     */
    public HalfyearRange previousHalfyear() {
        return addHalfyears(-1);
    }

    /**
     * Next halfyear.
     *
     * @return the halfyear range
     */
    public HalfyearRange nextHalfyear() {
        return addHalfyears(1);
    }

    /**
     * Add halfyears.
     *
     * @param count the count
     * @return the halfyear range
     */
    public HalfyearRange addHalfyears(int count) {
        YearAndHalfyear yhy = Times.addHalfyear(getYear(), getHalfyear(), count);
        return new HalfyearRange(yhy.getYear(), yhy.getHalfyear(), getTimeCalendar());
    }
}
