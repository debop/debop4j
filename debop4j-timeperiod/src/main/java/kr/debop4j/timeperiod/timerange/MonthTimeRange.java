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
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.test.tools.TimeSpec;
import kr.debop4j.timeperiod.test.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 월(Month) 단위로 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 7:56
 */
@Slf4j
public abstract class MonthTimeRange extends CalendarTimeRange {

    @Getter private final int monthCount;
    @Getter private final int endYear;
    @Getter private final int endMonthOfYear;

    // region << Constructor >>

    public MonthTimeRange(DateTime moment, int monthCount) {
        this(moment, monthCount, new TimeCalendar());
    }

    public MonthTimeRange(DateTime moment, int monthCount, ITimeCalendar calendar) {
        this(calendar.getYear(moment), calendar.getMonthOfYear(moment), monthCount, calendar);
    }

    public MonthTimeRange(int year, int monthOfYear, int monthCount) {
        this(year, monthOfYear, monthCount, new TimeCalendar());
    }

    public MonthTimeRange(int year, int monthOfYear, int monthCount, ITimeCalendar calendar) {
        super(getPeriodOf(year, monthOfYear, monthCount), calendar);
        Guard.shouldBePositiveNumber(monthCount, "monthCount");

        this.monthCount = monthCount;

        YearAndMonth ym = Times.addMonth(year, monthOfYear, monthCount - 1);
        endYear = ym.getYear();
        endMonthOfYear = ym.getMonthOfYear();
    }

    // endregion

    /** 현재 월(Month)에 속한 일단위 기간({@link DayRange}) 컬렉션을 빌드합니다. */
    public List<DayRange> getDays() {
        DateTime startMonth = Times.startTimeOfMonth(getStart());
        List<DayRange> days = Lists.newArrayListWithCapacity(monthCount * TimeSpec.MaxDaysPerMonth);

        for (int m = 0; m < monthCount; m++) {
            DateTime monthStart = startMonth.plusMonths(m);
            int daysOfMonth = Times.getDaysInMonth(monthStart.getYear(), monthStart.getMonthOfYear());
            for (int d = 0; d < daysOfMonth; d++) {
                days.add(new DayRange(monthStart.plusDays(d), getTimeCalendar()));
            }
        }
        return days;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), monthCount, endYear, endMonthOfYear);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("monthCount", monthCount)
                .add("endYear", endYear)
                .add("endMonthOfYear", endMonthOfYear);
    }

    private static ITimePeriod getPeriodOf(int year, int month, int monthCount) {
        assert monthCount > 0;
        DateTime start = new DateTime(year, month, 1, 0, 0);
        return new TimeRange(start, start.plusMonths(monthCount));
    }

    private static final long serialVersionUID = 8872234404060777601L;
}
