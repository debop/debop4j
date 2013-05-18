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

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import kr.debop4j.timeperiod.tools.Weeks;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * kr.debop4j.timeperiod.timerange.WeekTimeRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 2:29
 */
public abstract class WeekTimeRange extends CalendarTimeRange {

    private static final long serialVersionUID = -1899389597363540458L;

    protected WeekTimeRange(ITimePeriod period, ITimeCalendar timeCalendar) {
        super(period, timeCalendar);
        this.year = period.getStart().getYear();
        this.startWeekOfYear = Weeks.getYearAndWeek(period.getStart(), timeCalendar).getWeekOfYear();
        this.weekCount = 1;
    }

    protected WeekTimeRange(DateTime moment, int weekCount, ITimeCalendar timeCalendar) {
        super(getPeriodOf(moment, weekCount, timeCalendar), timeCalendar);

        YearAndWeek yw = Weeks.getYearAndWeek(moment, timeCalendar);
        this.year = yw.getYear();
        this.startWeekOfYear = yw.getWeekOfYear();
        this.weekCount = weekCount;
    }

    protected WeekTimeRange(int year, int weekOfYear, int weekCount) {
        this(year, weekOfYear, weekCount, new TimeCalendar());
    }

    protected WeekTimeRange(int year, int weekOfYear, int weekCount, ITimeCalendar timeCalendar) {
        super(getPeriodOf(year, weekOfYear, weekCount, timeCalendar), timeCalendar);
        this.year = year;
        this.startWeekOfYear = weekOfYear;
        this.weekCount = weekCount;
    }

    @Getter private final int year;
    @Getter private final int startWeekOfYear;
    @Getter private final int weekCount;

    public int getEndWeekOfYear() {
        return startWeekOfYear + weekCount - 1;
    }

    public String getStartWeekOfYearName() {
        return getTimeCalendar().getWeekOfYearName(year, startWeekOfYear);
    }

    public String getEndWeekOfYearName() {
        return getTimeCalendar().getWeekOfYearName(year, getEndWeekOfYear());
    }

    public List<DayRange> getDays() {
        DateTime startDay = Times.getStartOfYearWeek(year, startWeekOfYear, getTimeCalendar());
        int dayCount = weekCount * TimeSpec.DaysPerWeek;

        List<DayRange> days = Lists.newArrayListWithCapacity(dayCount);
        for (int d = 0; d < dayCount; d++) {
            days.add(new DayRange(startDay.plusDays(d), getTimeCalendar()));
        }
        return days;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), year, startWeekOfYear, weekCount);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("year", year)
                .add("startWeekOfYear", startWeekOfYear)
                .add("weekCount", weekCount);
    }

    private static TimeRange getPeriodOf(DateTime moment, int weekCount, ITimeCalendar timeCalendar) {
        DateTime startOfWeek = Times.startTimeOfWeek(moment, timeCalendar.getFirstDayOfWeek());
        return new TimeRange(startOfWeek, Durations.weeks(weekCount));
    }

    private static TimeRange getPeriodOf(int year, int weekOfYear, int weekCount, ITimeCalendar timeCalendar) {
        assert weekCount >= 0;

        DateTime startWeek = Times.startTimeOfWeek(year, weekOfYear, timeCalendar);
        return new TimeRange(startWeek, startWeek.plusWeeks(1));
    }
}
