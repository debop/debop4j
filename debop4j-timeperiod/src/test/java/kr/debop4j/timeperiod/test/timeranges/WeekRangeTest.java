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
import kr.debop4j.timeperiod.timerange.WeekRange;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static kr.debop4j.timeperiod.test.tools.Times.startTimeOfYear;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.WeekRangeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 26. 오전 12:00
 */
@Slf4j
public class WeekRangeTest extends TimePeriodTestBase {


    @Test
    public void initValues() {
        DateTime now = Times.now();
        DateTime firstWeek = Times.startTimeOfWeek(now);
        DateTime secondWeek = firstWeek.plusWeeks(1);

        WeekRange weekRange = new WeekRange(now, TimeCalendar.getEmptyOffset());

        assertThat(weekRange.getStart()).isEqualTo(firstWeek);
        assertThat(weekRange.getEnd()).isEqualTo(secondWeek);
    }

    @Test
    public void defaultCalendarTest() {
        DateTime yearStart = startTimeOfYear(Times.now());

        for (int w = 1; w < 50; w++) {

            WeekRange weekRange = new WeekRange(yearStart.plusWeeks(w));
            assertThat(weekRange.getYear()).isEqualTo(yearStart.getYear());
            assertThat(weekRange.getWeekOfYear()).isEqualTo(w + 1);

            assertThat(weekRange.getUnmappedStart()).isEqualTo(Times.startTimeOfWeek(yearStart.plusWeeks(w)));
            assertThat(weekRange.getUnmappedEnd()).isEqualTo(Times.startTimeOfWeek(yearStart.plusWeeks(w)).plusWeeks(1));
        }
    }


    @Test
    public void getDaysTest() {
        final DateTime now = Times.now();
        final WeekRange weekRange = new WeekRange();
        List<DayRange> days = weekRange.getDays();

        int index = 0;
        for (DayRange day : days) {
            assertThat(day.getStart()).isEqualTo(weekRange.getStart().plusDays(index));
            assertThat(day.getEnd()).isEqualTo(day.getTimeCalendar().mapEnd(day.getStart().plusDays(1)));
            index++;
        }
        assertThat(index).isEqualTo(TimeSpec.DaysPerWeek);
    }

    @Test
    public void addMonthsTest() {

        final DateTime now = Times.now();
        final DateTime startWeek = Times.startTimeOfWeek(now);
        final WeekRange weekRange = new WeekRange(now);


        assertThat(weekRange.previousWeek().getStart()).isEqualTo(startWeek.plusWeeks(-1));
        assertThat(weekRange.nextWeek().getStart()).isEqualTo(startWeek.plusWeeks(1));

        assertThat(weekRange.addWeeks(0)).isEqualTo(weekRange);

        Parallels.run(-60, 120, new Action1<Integer>() {
            @Override
            public void perform(Integer m) {
                assertThat(weekRange.addWeeks(m).getStart()).isEqualTo(startWeek.plusWeeks(m));
            }
        });
    }
}
