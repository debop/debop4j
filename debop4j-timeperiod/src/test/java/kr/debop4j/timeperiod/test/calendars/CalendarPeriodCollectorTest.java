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

package kr.debop4j.timeperiod.test.calendars;

import com.google.common.collect.Lists;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.calendars.CalendarPeriodCollector;
import kr.debop4j.timeperiod.calendars.CalendarPeriodCollectorFilter;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.*;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.calendars.CalendarPeriodCollectorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 27. 오후 3:23
 */
@Slf4j
public class CalendarPeriodCollectorTest extends TimePeriodTestBase {

    @Test
    public void collectYearsTest() {
        CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

        filter.getYears().addAll(Lists.newArrayList(2006, 2007, 2012));

        ITimePeriod limits = new CalendarTimeRange(Times.asDate(2001, 1, 1), Times.asDate(2019, 12, 31));
        CalendarPeriodCollector collector = new CalendarPeriodCollector(filter, limits);

        collector.collectYears();

        log.trace("Collect years... periods=[{}]", StringTool.listToString(collector.getPeriods()));

        for (int i = 0; i < collector.getPeriods().size(); i++) {
            ITimePeriod period = collector.getPeriods().get(i);
            log.trace("period=[{}]", period);
            assertThat(period.isSamePeriod(new YearRange(filter.getYears().get(i)))).isTrue();
        }
    }

    @Test
    public void collectMonthsTest() {
        CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

        filter.getMonthOfYears().addAll(Lists.newArrayList(Month.January.getValue()));

        ITimePeriod limits = new CalendarTimeRange(Times.asDate(2010, 1, 1), Times.asDate(2011, 12, 31));
        CalendarPeriodCollector collector = new CalendarPeriodCollector(filter, limits);

        collector.collectMonths();

        log.trace("Collect months... periods=[{}]", StringTool.listToString(collector.getPeriods()));

        assertThat(collector.getPeriods().size()).isEqualTo(2);
        assertThat(collector.getPeriods().get(0).isSamePeriod(new MonthRange(2010, 1))).isTrue();
        assertThat(collector.getPeriods().get(1).isSamePeriod(new MonthRange(2011, 1))).isTrue();
    }

    @Test
    public void collectDaysTest() {
        CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

        // 1월의 금요일만 추출
        filter.getMonthOfYears().addAll(Lists.newArrayList(Month.January.getValue()));
        filter.getWeekDays().add(DayOfWeek.FriDay);

        ITimePeriod limits = new CalendarTimeRange(Times.asDate(2010, 1, 1), Times.asDate(2011, 12, 31));
        CalendarPeriodCollector collector = new CalendarPeriodCollector(filter, limits);

        collector.collectDays();

        for (ITimePeriod period : collector.getPeriods()) {
            log.trace("Day=[{}]", period);
        }

        assertThat(collector.getPeriods().size()).isEqualTo(9);

        assertThat(collector.getPeriods().get(0).isSamePeriod(new DayRange(2010, 1, 1))).isTrue();
        assertThat(collector.getPeriods().get(1).isSamePeriod(new DayRange(2010, 1, 8))).isTrue();
        assertThat(collector.getPeriods().get(2).isSamePeriod(new DayRange(2010, 1, 15))).isTrue();
        assertThat(collector.getPeriods().get(3).isSamePeriod(new DayRange(2010, 1, 22))).isTrue();
        assertThat(collector.getPeriods().get(4).isSamePeriod(new DayRange(2010, 1, 29))).isTrue();

        assertThat(collector.getPeriods().get(5).isSamePeriod(new DayRange(2011, 1, 7))).isTrue();
        assertThat(collector.getPeriods().get(6).isSamePeriod(new DayRange(2011, 1, 14))).isTrue();
        assertThat(collector.getPeriods().get(7).isSamePeriod(new DayRange(2011, 1, 21))).isTrue();
        assertThat(collector.getPeriods().get(8).isSamePeriod(new DayRange(2011, 1, 28))).isTrue();
    }

    @Test
    public void collectHoursTest() {
        CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

        // 1월의 금요일의 08:00~18:00 추출
        filter.getMonthOfYears().addAll(Lists.newArrayList(Month.January.getValue()));
        filter.getWeekDays().add(DayOfWeek.FriDay);
        filter.getCollectingHours().add(new HourRangeInDay(8, 18));

        ITimePeriod limits = new CalendarTimeRange(Times.asDate(2010, 1, 1), Times.asDate(2011, 12, 31));
        CalendarPeriodCollector collector = new CalendarPeriodCollector(filter, limits);

        collector.collectHours();

        for (ITimePeriod period : collector.getPeriods()) {
            log.trace("Hours=[{}]", period);
        }

        assertThat(collector.getPeriods().size()).isEqualTo(9);

        assertThat(collector.getPeriods().get(0).isSamePeriod(new HourRangeCollection(2010, 1, 1, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(1).isSamePeriod(new HourRangeCollection(2010, 1, 8, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(2).isSamePeriod(new HourRangeCollection(2010, 1, 15, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(3).isSamePeriod(new HourRangeCollection(2010, 1, 22, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(4).isSamePeriod(new HourRangeCollection(2010, 1, 29, 8, 10))).isTrue();

        assertThat(collector.getPeriods().get(5).isSamePeriod(new HourRangeCollection(2011, 1, 7, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(6).isSamePeriod(new HourRangeCollection(2011, 1, 14, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(7).isSamePeriod(new HourRangeCollection(2011, 1, 21, 8, 10))).isTrue();
        assertThat(collector.getPeriods().get(8).isSamePeriod(new HourRangeCollection(2011, 1, 28, 8, 10))).isTrue();
    }

    @Test
    public void collectHoursWithMinutes() {
        CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();

        // 1월의 금요일의 08:30~18:30 추출
        filter.getMonthOfYears().addAll(Lists.newArrayList(Month.January.getValue()));
        filter.getWeekDays().add(DayOfWeek.FriDay);
        filter.getCollectingHours().add(new HourRangeInDay(new TimeValue(8, 30), new TimeValue(18, 50)));

        ITimePeriod limits = new CalendarTimeRange(Times.asDate(2010, 1, 1), Times.asDate(2011, 12, 31));
        CalendarPeriodCollector collector = new CalendarPeriodCollector(filter, limits);

        collector.collectHours();

        for (ITimePeriod period : collector.getPeriods()) {
            log.trace("Hours=[{}]", period);
        }

        assertThat(collector.getPeriods().size()).isEqualTo(9);

        assertThat(collector.getPeriods().get(0).isSamePeriod(new CalendarTimeRange(new DateTime(2010, 1, 1, 8, 30), new DateTime(2010, 1, 1, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(1).isSamePeriod(new CalendarTimeRange(new DateTime(2010, 1, 8, 8, 30), new DateTime(2010, 1, 8, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(2).isSamePeriod(new CalendarTimeRange(new DateTime(2010, 1, 15, 8, 30), new DateTime(2010, 1, 15, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(3).isSamePeriod(new CalendarTimeRange(new DateTime(2010, 1, 22, 8, 30), new DateTime(2010, 1, 22, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(4).isSamePeriod(new CalendarTimeRange(new DateTime(2010, 1, 29, 8, 30), new DateTime(2010, 1, 29, 18, 50)))).isTrue();

        assertThat(collector.getPeriods().get(5).isSamePeriod(new CalendarTimeRange(new DateTime(2011, 1, 7, 8, 30), new DateTime(2011, 1, 7, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(6).isSamePeriod(new CalendarTimeRange(new DateTime(2011, 1, 14, 8, 30), new DateTime(2011, 1, 14, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(7).isSamePeriod(new CalendarTimeRange(new DateTime(2011, 1, 21, 8, 30), new DateTime(2011, 1, 21, 18, 50)))).isTrue();
        assertThat(collector.getPeriods().get(8).isSamePeriod(new CalendarTimeRange(new DateTime(2011, 1, 28, 8, 30), new DateTime(2011, 1, 28, 18, 50)))).isTrue();
    }

    @Test
    public void collectExcludePeriod() {

        final int workingDays2011 = 365 - 2 - (51 * 2) - 1;
        final int workingDaysMarch2011 = 31 - 8; // total days - weekend days

        YearRange year2011 = new YearRange(2011);

        CalendarPeriodCollectorFilter filter1 = new CalendarPeriodCollectorFilter();
        filter1.addWorkingWeekdays();

        CalendarPeriodCollector collector1 = new CalendarPeriodCollector(filter1, year2011);
        collector1.collectDays();
        assertThat(collector1.getPeriods().size()).isEqualTo(workingDays2011);

        // 3월 제외 (23일 제외)
        CalendarPeriodCollectorFilter filter2 = new CalendarPeriodCollectorFilter();
        filter2.addWorkingWeekdays();
        filter2.getExcludePeriods().add(new MonthRange(2011, 3));

        CalendarPeriodCollector collector2 = new CalendarPeriodCollector(filter2, year2011);
        collector2.collectDays();
        assertThat(collector2.getPeriods().size()).isEqualTo(workingDays2011 - workingDaysMarch2011);


        // 2011 년 26주차 ~ 27주차 (여름휴가)
        CalendarPeriodCollectorFilter filter3 = new CalendarPeriodCollectorFilter();
        filter3.addWorkingWeekdays();
        filter3.getExcludePeriods().add(new MonthRange(2011, 3));
        filter3.getExcludePeriods().add(new WeekRangeCollection(2011, 26, 2));

        CalendarPeriodCollector collector3 = new CalendarPeriodCollector(filter3, year2011);
        collector3.collectDays();
        assertThat(collector3.getPeriods().size()).isEqualTo(workingDays2011 - workingDaysMarch2011 - 2 * TimeSpec.WeekDaysPerWeek);
    }
}
