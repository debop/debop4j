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
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * 한 주(Week)를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 3:30
 */
@Slf4j
public class WeekRange extends WeekTimeRange {

    // region << Constructor >>

    public WeekRange() {
        this(new TimeCalendar());
    }

    public WeekRange(ITimeCalendar timeCalendar) {
        this(ClockProxy.getClock().now(), timeCalendar);
    }

    public WeekRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    public WeekRange(DateTime moment, ITimeCalendar timeCalendar) {
        super(moment, 1, timeCalendar);
    }

    public WeekRange(ITimePeriod period, ITimeCalendar timeCalendar) {
        super(period, timeCalendar);
    }

    public WeekRange(DateTime moment, int weekCount, ITimeCalendar timeCalendar) {
        super(moment, weekCount, timeCalendar);
    }

    public WeekRange(int year, int weekOfYear) {
        super(year, weekOfYear, 1);
    }

    public WeekRange(int year, int weekOfYear, ITimeCalendar timeCalendar) {
        super(year, weekOfYear, 1, timeCalendar);
    }

    // endregion

    public int getWeekOfYear() {
        return getStartWeekOfYear();
    }

    public DateTime getFirstDayOfWeek() {
        return getStart();
    }

    public DateTime getLastDayOfWeek() {
        return getStart().plusDays(6);
    }

    public boolean isMultipleCalendarYears() {
        return getTimeCalendar().getYear(getFirstDayOfWeek()) != getTimeCalendar().getYear(getLastDayOfWeek());
    }

    public WeekRange getPreviousWeek() {
        return addWeeks(-1);
    }

    public WeekRange getNextWeek() {
        return addWeeks(1);
    }

    public WeekRange addWeeks(int weeks) {
        DateTime startOfWeek = Times.getStartOfYearWeek(getYear(), getStartWeekOfYear(), getTimeCalendar());
        return new WeekRange(startOfWeek.plusWeeks(weeks), getTimeCalendar());
    }

    private static final long serialVersionUID = 562359121625029972L;
}
