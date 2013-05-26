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

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.timeperiod.HourRangeInDay;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.SeekBoundaryMode;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.calendars.CalendarDateAdd;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static kr.debop4j.timeperiod.tools.Times.asDate;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.calendars.CalendarDateAddTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 26. 오후 9:22
 */
@Slf4j
public class CalendarDateAddTest extends TimePeriodTestBase {

    @Test
    public void noPeriodTest() {
        final CalendarDateAdd calendarDateAdd = new CalendarDateAdd();
        final DateTime now = Times.now();

        Parallels.run(-10, 20, new Action1<Integer>() {
            @Override
            public void perform(Integer index) {

                int offset = index * 5;

                assertThat(calendarDateAdd.add(now, Durations.days(offset))).isEqualTo(now.plusDays(offset));
                assertThat(calendarDateAdd.add(now, Durations.days(-offset))).isEqualTo(now.plusDays(-offset));

                assertThat(calendarDateAdd.subtract(now, Durations.days(offset))).isEqualTo(now.plusDays(-offset));
                assertThat(calendarDateAdd.subtract(now, Durations.days(-offset))).isEqualTo(now.plusDays(offset));
            }
        });
    }

    @Test
    public void periodLimitsAdd() {
        DateTime test = asDate(2011, 4, 12);
        ITimePeriod period1 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 25));
        ITimePeriod period2 = new TimeRange(asDate(2011, 4, 30), (DateTime) null); // 4월 30일 이후

        CalendarDateAdd dateAdd = new CalendarDateAdd();

        // 예외기간을 설정합니다. 4월 20일 ~ 4월25일, 4월 30일 이후
        dateAdd.getExcludePeriods().add(period1);
        dateAdd.getExcludePeriods().add(period2);

        assertThat(dateAdd.add(test, Durations.Day)).isEqualTo(test.plus(Durations.Day));

        // 4월 12일에 8일을 더하면 4월 20일이지만, 20~25일까지 제외되므로, 4월 25일이 된다.
        assertThat(dateAdd.add(test, Durations.days(8))).isEqualTo(period1.getEnd());

        // 4월 12에 20일을 더하면 4월 20~25일을 제외한 후 계산하면 4월 30 이후가 된다. (5월 3일).
        // 하지만 4월 30 이후는 모두 제외되므로 결과값은 null이다.
        assertThat(dateAdd.add(test, Durations.days(20))).isNull();

        assertThat(dateAdd.subtract(test, Durations.days(3))).isEqualTo(test.minus(Durations.days(3)));
    }

    @Test
    public void periodLimitsSubtract() {
        DateTime test = asDate(2011, 4, 30);
        ITimePeriod period1 = new TimeRange(asDate(2011, 4, 20), asDate(2011, 4, 25));
        ITimePeriod period2 = new TimeRange(null, asDate(2011, 4, 6)); // 4월 6일까지

        CalendarDateAdd dateAdd = new CalendarDateAdd();

        // 예외기간을 설정합니다. 4월 6일 이전, 4월 20일 ~ 4월 25일
        dateAdd.getExcludePeriods().add(period1);
        dateAdd.getExcludePeriods().add(period2);

        assertThat(dateAdd.subtract(test, Durations.Day)).isEqualTo(test.minus(Durations.Day));

        // 4월 30일로부터 5일 전이면 4월 25일이지만, 예외기간이므로 4월20일이 된다.
        assertThat(dateAdd.subtract(test, Durations.days(5))).isEqualTo(period1.getStart());

        // 4월 30일로부터 20일 전이면, 5월 전이 4월20일이므로, 4월 5일이 된다. 근데, 4월 6일 이전은 모두 제외기간이므로 null을 반환한다.
        assertThat(dateAdd.subtract(test, Durations.days(20))).isNull();
    }

    @Test
    public void excludeTest() {
        DateTime test = asDate(2011, 4, 12);
        ITimePeriod period = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));

        CalendarDateAdd dateAdd = new CalendarDateAdd();
        dateAdd.getExcludePeriods().add(period);

        assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
        assertThat(dateAdd.add(test, Durations.days(1))).isEqualTo(test.plusDays(1));
        assertThat(dateAdd.add(test, Durations.days(2))).isEqualTo(test.plusDays(2));
        assertThat(dateAdd.add(test, Durations.days(3))).isEqualTo(period.getEnd());
        assertThat(dateAdd.add(test, Durations.days(3, 0, 0, 0, 1))).isEqualTo(period.getEnd().plusMillis(1));
        assertThat(dateAdd.add(test, Durations.days(5))).isEqualTo(period.getEnd().plusDays(2));
    }

    @Test
    public void excludeSplit() {
        DateTime test = asDate(2011, 4, 12);
        ITimePeriod period1 = new TimeRange(asDate(2011, 4, 15), asDate(2011, 4, 20));
        ITimePeriod period2 = new TimeRange(asDate(2011, 4, 22), asDate(2011, 4, 25));

        CalendarDateAdd dateAdd = new CalendarDateAdd();
        dateAdd.getExcludePeriods().add(period1);
        dateAdd.getExcludePeriods().add(period2);

        assertThat(dateAdd.add(test, Durations.Zero)).isEqualTo(test);
        assertThat(dateAdd.add(test, Durations.days(1))).isEqualTo(test.plusDays(1));
        assertThat(dateAdd.add(test, Durations.days(2))).isEqualTo(test.plusDays(2));
        assertThat(dateAdd.add(test, Durations.days(3))).isEqualTo(period1.getEnd());
        assertThat(dateAdd.add(test, Durations.days(4))).isEqualTo(period1.getEnd().plusDays(1));
        assertThat(dateAdd.add(test, Durations.days(5))).isEqualTo(period2.getEnd());
        assertThat(dateAdd.add(test, Durations.days(6))).isEqualTo(period2.getEnd().plusDays(1));
        assertThat(dateAdd.add(test, Durations.days(7))).isEqualTo(period2.getEnd().plusDays(2));
    }

    @Test
    public void calendarDateAddSeekBoundaryMode() {
        CalendarDateAdd dateAdd = new CalendarDateAdd();

        dateAdd.addWorkingWeekDays();
        dateAdd.getExcludePeriods().add(new DayRange(2011, 4, 4, dateAdd.getTimeCalendar()));
        dateAdd.getWorkingHours().add(new HourRangeInDay(8, 18));

        DateTime start = new DateTime(2011, 4, 1, 9, 0);

        assertThat(dateAdd.add(start, Durations.hours(29), SeekBoundaryMode.Fill)).isEqualTo(new DateTime(2011, 4, 6, 18, 0, 0));
        assertThat(dateAdd.add(start, Durations.hours(29), SeekBoundaryMode.Next)).isEqualTo(new DateTime(2011, 4, 7, 8, 0, 0));
        assertThat(dateAdd.add(start, Durations.hours(29))).isEqualTo(new DateTime(2011, 4, 7, 8, 0, 0));
    }

    @Test
    public void calendarDateAdd1() {
        CalendarDateAdd dateAdd = new CalendarDateAdd();

        dateAdd.addWorkingWeekDays();
        dateAdd.getExcludePeriods().add(new DayRange(2011, 4, 4, dateAdd.getTimeCalendar()));
        dateAdd.getWorkingHours().add(new HourRangeInDay(8, 18));

        DateTime start = new DateTime(2011, 4, 1, 9, 0);

        assertThat(dateAdd.add(start, Durations.hours(22))).isEqualTo(new DateTime(2011, 4, 6, 11, 0, 0));
        assertThat(dateAdd.add(start, Durations.hours(22), SeekBoundaryMode.Fill)).isEqualTo(new DateTime(2011, 4, 6, 11, 0, 0));

        assertThat(dateAdd.add(start, Durations.hours(29))).isEqualTo(new DateTime(2011, 4, 7, 8, 0, 0));
        assertThat(dateAdd.add(start, Durations.hours(29), SeekBoundaryMode.Fill)).isEqualTo(new DateTime(2011, 4, 6, 18, 0, 0));
    }

    @Test
    public void calendarDateAdd2() {
        CalendarDateAdd dateAdd = new CalendarDateAdd();

        dateAdd.addWorkingWeekDays();
        dateAdd.getExcludePeriods().add(new DayRange(2011, 4, 4, dateAdd.getTimeCalendar()));
        dateAdd.getWorkingHours().add(new HourRangeInDay(8, 12));
        dateAdd.getWorkingHours().add(new HourRangeInDay(13, 18));

        DateTime start = new DateTime(2011, 4, 1, 9, 0);

        assertThat(dateAdd.add(start, Durations.hours(3))).isEqualTo(new DateTime(2011, 4, 6, 11, 0, 0));
        assertThat(dateAdd.add(start, Durations.hours(4), SeekBoundaryMode.Fill)).isEqualTo(new DateTime(2011, 4, 1, 14, 0, 0));
        assertThat(dateAdd.add(start, Durations.hours(8), SeekBoundaryMode.Fill)).isEqualTo(new DateTime(2011, 4, 5, 8, 0, 0));
    }
}
