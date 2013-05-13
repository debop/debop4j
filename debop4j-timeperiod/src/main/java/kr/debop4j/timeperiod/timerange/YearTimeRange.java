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

import com.google.common.collect.Lists;
import kr.debop4j.timeperiod.HalfyearKind;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.QuarterKind;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * kr.debop4j.timeperiod.timerange.YearTimeRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 11:48
 */
public class YearTimeRange extends YearCalendarTimeRange {
    private static final long serialVersionUID = 1604523513628691621L;

    public YearTimeRange(DateTime moment, int yearCount, ITimeCalendar calendar) {
        this(moment.getYear(), yearCount, calendar);
    }

    public YearTimeRange(int startYear, int yearCount, ITimeCalendar calendar) {
        super(getPeriodOf(startYear, yearCount), calendar);

        this.yearCount = yearCount;
    }

    @Getter private final int yearCount;

    public String getStartYearName() {
        return getTimeCalendar().getYearName(getStartYear());
    }

    public String getEndYearName() {
        return getTimeCalendar().getYearName(getEndYear());
    }

    public List<HalfyearRange> getHalfyears() {
        int startYear = getStartYear();

        List<HalfyearRange> halfyears = Lists.newArrayListWithCapacity(yearCount * TimeSpec.HalfyearsPerYear);
        for (int y = 0; y < yearCount; y++) {
            halfyears.add(new HalfyearRange(startYear + y, HalfyearKind.First, getTimeCalendar()));
            halfyears.add(new HalfyearRange(startYear + y, HalfyearKind.Second, getTimeCalendar()));
        }
        return halfyears;
    }

    public List<QuarterRange> getQuarters() {
        int startYear = getStartYear();

        List<QuarterRange> quarters = Lists.newArrayListWithCapacity(yearCount * TimeSpec.QuartersPerYear);
        QuarterKind[] quarterKinds = QuarterKind.values();
        for (int y = 0; y < yearCount; y++) {
            for (QuarterKind quarter : quarterKinds) {
                quarters.add(new QuarterRange(startYear + y, quarter, getTimeCalendar()));
            }
        }
        return quarters;
    }

    public List<MonthRange> getMonths() {
        int startYear = getStartYear();

        List<MonthRange> months = Lists.newArrayListWithCapacity(yearCount * TimeSpec.MonthsPerYear);

        for (int y = 0; y < yearCount; y++) {
            for (int m = 0; m < TimeSpec.MonthsPerYear; m++) {
                months.add(new MonthRange(startYear + y, m, getTimeCalendar()));
            }
        }
        return months;
    }


    private static TimeRange getPeriodOf(int year, int yearCount) {
        assert yearCount >= 0;

        DateTime startYear = new DateTime(year, 1, 1, 0, 0);
        return new TimeRange(startYear, startYear.plusYears(yearCount));
    }
}
