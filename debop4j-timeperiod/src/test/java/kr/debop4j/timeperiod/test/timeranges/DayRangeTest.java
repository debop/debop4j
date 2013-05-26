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

package kr.debop4j.timeperiod.test.timeranges;

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.test.tools.TimeSpec;
import kr.debop4j.timeperiod.test.tools.Times;
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.timerange.HourRange;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static kr.debop4j.timeperiod.test.tools.Times.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.DayRangeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 4:04
 */
@Slf4j
public class DayRangeTest extends TimePeriodTestBase {

    @Test
    public void initValues() {
        DateTime now = Times.now();
        DateTime firstDay = Times.startTimeOfDay(now);
        DateTime secondDay = firstDay.plusDays(1);

        DayRange dr = new DayRange(now, TimeCalendar.getEmptyOffset());

        assertThat(dr.getStart()).isEqualTo(firstDay);
        assertThat(dr.getEnd()).isEqualTo(secondDay);
    }

    @Test
    public void defaultCalendarTest() {
        DateTime yearStart = startTimeOfYear(Times.now());

        for (int m = 1; m <= TimeSpec.MonthsPerYear; m++) {
            DateTime monthStart = asDate(yearStart.getYear(), m, 1);
            DateTime monthEnd = endTimeOfMonth(yearStart.getYear(), m);

            for (int day = monthStart.getDayOfMonth(); day < monthEnd.getDayOfMonth(); day++) {

                DayRange dr = new DayRange(monthStart.plusDays(day - monthStart.getDayOfMonth()));

                assertThat(dr.getYear()).isEqualTo(yearStart.getYear());
                assertThat(dr.getMonthOfYear()).isEqualTo(monthStart.getMonthOfYear());
            }
        }
    }

    @Test
    public void constructorTest() {
        DateTime now = Times.now();

        DayRange dr = new DayRange(now);
        assertThat(dr.getStart()).isEqualTo(now.withTimeAtStartOfDay());

        dr = new DayRange(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());
        assertThat(dr.getStart()).isEqualTo(now.withTimeAtStartOfDay());
    }

    @Test
    public void dayOfWeekTest() {
        DateTime now = Times.now();
        DayRange dr = new DayRange(now, TimeCalendar.getDefault());
        assertThat(dr.getDayOfWeek()).isEqualTo(TimeCalendar.getDefault().getDayOfWeek(now));
    }

    @Test
    public void addDaysTest() {

        final DateTime now = Times.now();
        final DateTime today = Times.today();
        final DayRange dr = new DayRange(now);


        assertThat(dr.previousDay().getStart()).isEqualTo(today.minusDays(1));
        assertThat(dr.nextDay().getStart()).isEqualTo(today.plusDays(1));

        assertThat(dr.addDays(0)).isEqualTo(dr);

        Parallels.run(-60, 120, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                assertThat(dr.addDays(i).getStart()).isEqualTo(today.plusDays(i));
            }
        });
    }

    @Test
    public void getHoursTest() {
        DayRange dr = new DayRange();
        List<HourRange> hours = dr.getHours();

        int index = 0;
        for (HourRange hour : hours) {
            assertThat(hour.getStart()).isEqualTo(dr.getStart().plusHours(index));
            assertThat(hour.getEnd()).isEqualTo(hour.getTimeCalendar().mapEnd(hour.getStart().plusHours(1)));
            index++;
        }
        assertThat(index).isEqualTo(TimeSpec.HoursPerDay);
    }
}
