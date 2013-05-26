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
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

import static kr.debop4j.core.Guard.shouldBePositiveNumber;

/**
 * 반기(Halfyear) 단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 10:29
 */
public abstract class HalfyearTimeRange extends YearCalendarTimeRange {

    private static final long serialVersionUID = -1813124170996786502L;

    public HalfyearTimeRange(int startYear, Halfyear startHalfyear, int halfyearCount, ITimeCalendar calendar) {
        super(getPeriodOf(startYear, startHalfyear, halfyearCount), calendar);
        shouldBePositiveNumber(halfyearCount, "halfyearCount");

        this.startYear = startYear;
        this.startHalfyear = startHalfyear;

        this.halfyearCount = halfyearCount;

        YearAndHalfyear endYH = Times.addHalfyear(startYear, startHalfyear, halfyearCount - 1);
        this.endYear = endYH.getYear();
        this.endHalfyear = endYH.getHalfyear();
    }

    @Getter private final int startYear;
    @Getter private final Halfyear startHalfyear;
    @Getter private final int endYear;
    @Getter private final Halfyear endHalfyear;
    @Getter private final int halfyearCount;

    /** 반기(HalfYear) 기간의 시작 년도와 완료 년도가 다른가 여부 */
    public boolean isMultipleCalendarYears() {
        return startYear != endYear;
    }

    public List<QuarterRange> getQuarters() {
        int quarterCount = this.halfyearCount * TimeSpec.QuartersPerHalfyear;
        int startQuarter = (startHalfyear.getValue() - 1) * TimeSpec.QuartersPerHalfyear;

        List<QuarterRange> quarters = Lists.newArrayListWithCapacity(quarterCount);
        for (int q = 0; q < quarterCount; q++) {
            int targetQuarter = (startQuarter + q) % TimeSpec.QuartersPerYear;
            int year = startYear + (targetQuarter / TimeSpec.QuartersPerYear);
            quarters.add(new QuarterRange(year, Quarter.valueOf(targetQuarter + 1), getTimeCalendar()));
        }
        return quarters;
    }

    public List<MonthRange> getMonths() {
        DateTime startMonth = Times.asDate(getStartYear(), 1, 1);
        int monthCount = getHalfyearCount() * TimeSpec.MonthsPerHalfyear;

        List<MonthRange> months = Lists.newArrayListWithCapacity(monthCount);
        for (int m = 0; m < monthCount; m++) {
            months.add(new MonthRange(startMonth.plusMonths(m), getTimeCalendar()));
        }
        return months;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), startYear, startHalfyear, endYear, endHalfyear, halfyearCount);
    }

//    @Override
//    protected Objects.ToStringHelper buildStringHelper() {
//        return super.buildStringHelper()
//                .add("startYear", startYear)
//                .add("startHalfyear", startHalfyear)
//                .add("endYear", endYear)
//                .add("endHalfyear", endHalfyear)
//                .add("halfyearCount", halfyearCount);
//    }

    private static TimeRange getPeriodOf(int year, Halfyear halfyear, int halfyearCount) {
        assert halfyearCount > 0;

        DateTime yearStart = Times.startTimeOfYear(year);
        DateTime start = yearStart.plusMonths((halfyear.getValue() - 1) * TimeSpec.MonthsPerHalfyear);
        DateTime end = start.plusMonths(halfyearCount * TimeSpec.MonthsPerHalfyear);

        return new TimeRange(start, end);
    }
}
