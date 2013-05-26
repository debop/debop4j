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

package kr.debop4j.timeperiod.test.tools;

import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.*;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static kr.debop4j.timeperiod.tools.Times.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.tools.TimesPeriodTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 24. 오전 5:22
 */
@Slf4j
public class TimesPeriodTest extends TimePeriodTestBase {

    public static final int PeriodCount = 24;

    private DateTime startTime = new DateTime(2008, 4, 10, 5, 33, 24, 345);
    private DateTime endTime = new DateTime(2018, 10, 20, 13, 43, 12, 599);
    private Duration duration = new Duration(startTime, endTime);

    @Test
    public void getTimeBlockByDuration() {
        TimeBlock block = getTimeBlock(startTime, duration);
        assertThat(block.getStart()).isEqualTo(startTime);
        assertThat(block.getEnd()).isEqualTo(endTime);
        assertThat(block.getDuration()).isEqualTo(duration);
    }

    @Test
    public void getTimeBlockByStartAndEnd() {
        TimeBlock block = getTimeBlock(startTime, endTime);
        assertThat(block.getStart()).isEqualTo(startTime);
        assertThat(block.getEnd()).isEqualTo(endTime);
        assertThat(block.getDuration()).isEqualTo(duration);
    }

    @Test
    public void getTimeRangeByDuration() {
        TimeRange range = getTimeRange(startTime, duration);
        assertThat(range.getStart()).isEqualTo(startTime);
        assertThat(range.getEnd()).isEqualTo(endTime);
        assertThat(range.getDuration()).isEqualTo(duration);

        range = getTimeRange(startTime, Durations.negate(duration));
        assertThat(range.getStart()).isEqualTo(startTime.minus(duration));
        assertThat(range.getEnd()).isEqualTo(endTime.minus(duration));
        assertThat(range.getDuration()).isEqualTo(duration);
    }

    @Test
    public void getTimeRangeByStartAndEnd() {
        TimeRange range = getTimeRange(startTime, endTime);
        assertThat(range.getStart()).isEqualTo(startTime);
        assertThat(range.getEnd()).isEqualTo(endTime);
        assertThat(range.getDuration()).isEqualTo(duration);
    }

    @Test
    public void getPeriodOfTest() {
        for (PeriodUnit unit : PeriodUnit.values()) {
            if (unit == PeriodUnit.Millisecond)
                continue;

            DateTime moment = startTime;
            ITimePeriod period = Times.getPeriodOf(moment, unit);

            assertThat(period.hasInside(moment)).isTrue();
            assertThat(period.hasInside(endTime)).isFalse();

            if (log.isTraceEnabled())
                log.trace("[{}] : period[{}] hasInside=[{}]", unit, period, moment);
        }
    }

    @Test
    public void getPeriodOfWithCalendar() {
        for (PeriodUnit unit : PeriodUnit.values()) {
            if (unit == PeriodUnit.Millisecond)
                continue;

            DateTime moment = startTime;
            ITimeCalendar calendar = TimeCalendar.getEmptyOffset();
            ITimePeriod period = Times.getPeriodOf(moment, unit, calendar);

            assertThat(period.hasInside(moment)).isTrue();
            assertThat(period.hasInside(endTime)).isFalse();

            if (log.isTraceEnabled())
                log.trace("[{}] : period[{}] hasInside=[{}]", unit, period, moment);
        }
    }

    @Test
    public void getPeriodsOfTest() {
        for (PeriodUnit unit : PeriodUnit.values()) {
            if (unit == PeriodUnit.Millisecond)
                continue;

            for (int count = 1; count < 5; count++) {
                DateTime moment = startTime;
                ITimeCalendar calendar = TimeCalendar.getEmptyOffset();
                ITimePeriod period = Times.getPeriodsOf(moment, unit, count, calendar);

                assertThat(period.hasPeriod()).isTrue();
                assertThat(period.hasInside(moment)).isTrue();
                assertThat(period.hasInside(endTime)).isFalse();

                if (log.isTraceEnabled())
                    log.trace("[{}] : period[{}] hasInside=[{}]", unit, period, moment);
            }
        }
    }

    @Test
    public void getYearRangeTest() {
        YearRange yearRange = getYearRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfYear(startTime);

        assertThat(yearRange.getStart()).isEqualTo(start);
        assertThat(yearRange.getStartYear()).isEqualTo(start.getYear());
        assertThat(yearRange.getEnd()).isEqualTo(start.plusYears(1));
        assertThat(yearRange.getEndYear()).isEqualTo(start.plusYears(1).getYear());
    }

    @Test
    public void getYearRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            YearRangeCollection yearRanges = getYearRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfYear(startTime);

            assertThat(yearRanges.getStart()).isEqualTo(start);
            assertThat(yearRanges.getStartYear()).isEqualTo(start.getYear());
            assertThat(yearRanges.getEnd()).isEqualTo(start.plusYears(i));
            assertThat(yearRanges.getEndYear()).isEqualTo(start.plusYears(i).getYear());
        }
    }

    @Test
    public void getHalfyearRangeTest() {
        HalfyearRange hy = getHalfyearRange(startTime, TimeCalendar.getEmptyOffset());

        DateTime start = startTimeOfHalfyear(startTime);
        assertThat(hy.getStart()).isEqualTo(start);
        assertThat(hy.getEnd()).isEqualTo(hy.nextHalfyear().getStart());
    }

    @Test
    public void getHalfyearRangesTest() {

        for (int i = 1; i < PeriodCount; i++) {
            HalfyearRangeCollection hys = getHalfyearRanges(startTime, i, TimeCalendar.getEmptyOffset());

            DateTime start = startTimeOfHalfyear(startTime);
            assertThat(hys.getStart()).isEqualTo(start);
            assertThat(hys.getEnd()).isEqualTo(start.plusMonths(i * TimeSpec.MonthsPerHalfyear));
            assertThat(hys.getHalfyearCount()).isEqualTo(i);
        }
    }

    @Test
    public void getQuarterRangeTest() {
        QuarterRange qr = getQuarterRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfQuarter(startTime);

        assertThat(qr.getStart()).isEqualTo(start);
        assertThat(qr.getEnd()).isEqualTo(qr.nextQuarter().getStart());
    }

    @Test
    public void getQuarterRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            QuarterRangeCollection quarters = getQuarterRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfQuarter(startTime);

            assertThat(quarters.getStart()).isEqualTo(start);
            assertThat(quarters.getEnd()).isEqualTo(start.plusMonths(i * TimeSpec.MonthsPerQuarter));
            assertThat(quarters.getQuarterCount()).isEqualTo(i);
        }
    }

    @Test
    public void getMonthRangeTest() {
        MonthRange mr = getMonthRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfMonth(startTime);

        assertThat(mr.getStart()).isEqualTo(start);
        assertThat(mr.getEnd()).isEqualTo(mr.nextMonth().getStart());
    }

    @Test
    public void getMonthRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            MonthRangeCollection mrs = getMonthRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfMonth(startTime);

            assertThat(mrs.getStart()).isEqualTo(start);
            assertThat(mrs.getEnd()).isEqualTo(start.plusMonths(i));
            assertThat(mrs.getMonthCount()).isEqualTo(i);
        }
    }

    @Test
    public void getWeekRangeTest() {
        WeekRange wr = Times.getWeekRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfWeek(startTime);

        assertThat(wr.getStart()).isEqualTo(start);
        assertThat(wr.getEnd()).isEqualTo(wr.nextWeek().getStart());
    }

    @Test
    public void getWeekRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            WeekRangeCollection wks = getWeekRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfWeek(startTime);

            assertThat(wks.getStart()).isEqualTo(start);
            assertThat(wks.getEnd()).isEqualTo(start.plusWeeks(i));
            assertThat(wks.getWeekCount()).isEqualTo(i);
        }
    }

    @Test
    public void getDayRangeTest() {
        DayRange dr = getDayRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfDay(startTime);

        assertThat(dr.getStart()).isEqualTo(start);
        assertThat(dr.getEnd()).isEqualTo(dr.nextDay().getStart());
    }

    @Test
    public void getDayRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            DayRangeCollection drs = getDayRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfDay(startTime);

            assertThat(drs.getStart()).isEqualTo(start);
            assertThat(drs.getEnd()).isEqualTo(start.plusDays(i));
            assertThat(drs.getDayCount()).isEqualTo(i);
        }
    }

    @Test
    public void getHourRangeTest() {
        HourRange hr = getHourRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfHour(startTime);

        assertThat(hr.getStart()).isEqualTo(start);
        assertThat(hr.getEnd()).isEqualTo(hr.nextHour().getStart());
    }

    @Test
    public void getHourRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            HourRangeCollection drs = getHourRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfHour(startTime);

            assertThat(drs.getStart()).isEqualTo(start);
            assertThat(drs.getEnd()).isEqualTo(start.plusHours(i));
            assertThat(drs.getHourCount()).isEqualTo(i);
        }
    }

    @Test
    public void getMinuteRangeTest() {
        MinuteRange hr = getMinuteRange(startTime, TimeCalendar.getEmptyOffset());
        DateTime start = startTimeOfMinute(startTime);

        assertThat(hr.getStart()).isEqualTo(start);
        assertThat(hr.getEnd()).isEqualTo(hr.nextMinute().getStart());
    }

    @Test
    public void getMinuteRangesTest() {
        for (int i = 1; i < PeriodCount; i++) {
            MinuteRangeCollection drs = getMinuteRanges(startTime, i, TimeCalendar.getEmptyOffset());
            DateTime start = startTimeOfMinute(startTime);

            assertThat(drs.getStart()).isEqualTo(start);
            assertThat(drs.getEnd()).isEqualTo(start.plusMinutes(i));
            assertThat(drs.getMinuteCount()).isEqualTo(i);
        }
    }

}
