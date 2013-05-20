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
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 분기단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 8:26
 */
public abstract class QuarterTimeRange extends YearCalendarTimeRange {

    @Getter private int startYear;
    @Getter private QuarterKind startQuarter;
    @Getter private int endYear;
    @Getter private QuarterKind endQuarter;
    @Getter private int quarterCount;

    public QuarterTimeRange(int startYear, QuarterKind startQuarter, int quarterCount) {
        this(startYear, startQuarter, quarterCount, new TimeCalendar());
    }

    public QuarterTimeRange(int startYear, QuarterKind startQuarter, int quarterCount, ITimeCalendar calendar) {
        super(getPeriodOf(startYear, startQuarter, quarterCount, calendar), calendar);

        this.startYear = startYear;
        this.startQuarter = startQuarter;
        this.quarterCount = quarterCount;

        YearAndQuarter yq = Times.addQuarter(startYear, startQuarter, quarterCount - 1);
        this.endYear = yq.getYear();
        this.endQuarter = yq.getQuarter();

        this.quarterCount = quarterCount;
    }


    public int getStartMonthOfYear() {
        int months = (startQuarter.getValue() - 1) * TimeSpec.MonthsPerQuarter;
        return Times.addMonth(startYear, 1, months).getMonthOfYear();
    }

    public int getEndMonthOfYear() {
        int months = (startQuarter.getValue() - 1 + quarterCount) * TimeSpec.MonthsPerQuarter;
        return Times.addMonth(endYear, 1, months).getMonthOfYear();
    }

    public boolean isMultipleCalendarYears() {
        int months = (startQuarter.getValue() - 1) * TimeSpec.MonthsPerQuarter;
        YearAndMonth startYM = Times.addMonth(startYear, 1, months);

        months = (startQuarter.getValue() - 1 + quarterCount) * TimeSpec.MonthsPerQuarter;
        YearAndMonth endYM = Times.addMonth(endYear, 1, months);

        return !startYM.getYear().equals(endYM.getYear());
    }

    public List<MonthRange> getMonths() {
        DateTime startMonth = new DateTime(startYear, 1, 1, 0, 0);
        int monthCount = quarterCount * TimeSpec.MonthsPerQuarter;
        List<MonthRange> months = Lists.newArrayListWithCapacity(monthCount);

        for (int m = 0; m < monthCount; m++) {
            months.add(new MonthRange(startMonth.plusMonths(m), getTimeCalendar()));
        }
        return months;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), startYear, startQuarter, quarterCount, endQuarter);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("startYear", startYear)
                .add("startQuarter", startQuarter)
                .add("quarterCount", quarterCount)
                .add("endQuarter", endQuarter);
    }

    private static ITimePeriod getPeriodOf(int year, QuarterKind quarterKind, int quarterCount, ITimeCalendar calendar) {
        assert quarterCount >= 0;

        int quarter = quarterKind.getValue();
        DateTime yearStart = new DateTime(year, 1, 1, 0, 0);
        DateTime start = yearStart.plusMonths((quarter - 1) * TimeSpec.MonthsPerQuarter);
        DateTime end = start.plusMonths(quarterCount * TimeSpec.MonthsPerQuarter);

        return new TimeRange(start, end);
    }

    private static final long serialVersionUID = -1642725884160403253L;
}