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
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.timerange.MonthRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static kr.debop4j.timeperiod.tools.Times.startTimeOfYear;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.MonthRangeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 11:04
 */
@Slf4j
public class MonthRangeTest extends TimePeriodTestBase {

    @Test
    public void initValues() {
        DateTime now = Times.now();
        DateTime firstMonth = Times.startTimeOfMonth(now);
        DateTime secondMonth = firstMonth.plusMonths(1);

        MonthRange monthRange = new MonthRange(now, TimeCalendar.getEmptyOffset());

        assertThat(monthRange.getStart()).isEqualTo(firstMonth);
        assertThat(monthRange.getEnd()).isEqualTo(secondMonth);
    }

    @Test
    public void defaultCalendarTest() {
        DateTime yearStart = startTimeOfYear(Times.now());

        for (int m = 0; m < TimeSpec.MonthsPerYear; m++) {

            MonthRange monthRange = new MonthRange(yearStart.plusMonths(m));
            assertThat(monthRange.getYear()).isEqualTo(yearStart.getYear());
            assertThat(monthRange.getMonthOfYear()).isEqualTo(m + 1);

            assertThat(monthRange.getUnmappedStart()).isEqualTo(yearStart.plusMonths(m));
            assertThat(monthRange.getUnmappedEnd()).isEqualTo(yearStart.plusMonths(m + 1));
        }
    }


    @Test
    public void getDaysTest() {
        final DateTime now = Times.now();
        final MonthRange monthRange = new MonthRange();
        List<DayRange> days = monthRange.getDays();

        int index = 0;
        for (DayRange day : days) {
            assertThat(day.getStart()).isEqualTo(monthRange.getStart().plusDays(index));
            assertThat(day.getEnd()).isEqualTo(day.getTimeCalendar().mapEnd(day.getStart().plusDays(1)));
            index++;
        }
        assertThat(index).isEqualTo(Times.getDaysInMonth(now.getYear(), now.getMonthOfYear()));
    }

    @Test
    public void addMonthsTest() {

        final DateTime now = Times.now();
        final DateTime startMonth = Times.startTimeOfMonth(now);
        final MonthRange monthRange = new MonthRange(now);


        assertThat(monthRange.previousMonth().getStart()).isEqualTo(startMonth.minusMonths(1));
        assertThat(monthRange.nextMonth().getStart()).isEqualTo(startMonth.plusMonths(1));

        assertThat(monthRange.addMonths(0)).isEqualTo(monthRange);

        Parallels.run(-60, 120, new Action1<Integer>() {
            @Override
            public void perform(Integer m) {
                assertThat(monthRange.addMonths(m).getStart()).isEqualTo(startMonth.plusMonths(m));
            }
        });
    }
}
