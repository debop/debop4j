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

package kr.debop4j.timeperiod.test.calendars.seekers;

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.timeperiod.SeekDirection;
import kr.debop4j.timeperiod.calendars.CalendarVisitorFilter;
import kr.debop4j.timeperiod.calendars.seeker.DaySeeker;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.timerange.DayRangeCollection;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.calendars.seekers.DaySeekerTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 27. 오후 6:16
 */
@Slf4j
public class DaySeekerTest extends TimePeriodTestBase {

    @Test
    public void simpleForward() {

        final DayRange start = new DayRange();
        final DaySeeker daySeeker = new DaySeeker();

        DayRange day1 = daySeeker.findDay(start, 0);
        assertThat(day1.isSamePeriod(start)).isTrue();

        DayRange day2 = daySeeker.findDay(start, 1);
        assertThat(day2.isSamePeriod(start.nextDay())).isTrue();

        Parallels.run(-10, 20, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                Integer offset = i * 5;
                DayRange day = daySeeker.findDay(start, offset);
                assertThat(day.isSamePeriod(start.addDays(offset))).isTrue();
            }
        });
    }

    @Test
    public void simpleBackward() {

        final DayRange start = new DayRange();
        final DaySeeker daySeeker = new DaySeeker(SeekDirection.Backward);

        DayRange day1 = daySeeker.findDay(start, 0);
        assertThat(day1.isSamePeriod(start)).isTrue();

        DayRange day2 = daySeeker.findDay(start, 1);
        assertThat(day2.isSamePeriod(start.previousDay())).isTrue();

        Parallels.run(-10, 20, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                Integer offset = i * 5;
                DayRange day = daySeeker.findDay(start, offset);
                assertThat(day.isSamePeriod(start.addDays(-offset))).isTrue();
            }
        });
    }

    @Test
    public void seekDirectionTest() {
        final DayRange start = new DayRange();
        final DaySeeker daySeeker = new DaySeeker();

        Parallels.run(-10, 20, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                Integer offset = i * 5;
                DayRange day = daySeeker.findDay(start, offset);
                assertThat(day.isSamePeriod(start.addDays(offset))).isTrue();
            }
        });

        final DaySeeker backwardSeeker = new DaySeeker(SeekDirection.Backward);

        Parallels.run(-10, 20, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                Integer offset = i * 5;
                DayRange day = backwardSeeker.findDay(start, offset);
                assertThat(day.isSamePeriod(start.addDays(-offset))).isTrue();
            }
        });
    }

    @Test
    public void minDateTest() {
        DaySeeker daySeeker = new DaySeeker();
        DayRange day = daySeeker.findDay(new DayRange(TimeSpec.MinPeriodTime), -10);
        assertThat(day).isNull();
    }

    @Test
    public void maxDateTest() {
        DaySeeker daySeeker = new DaySeeker();
        DayRange day = daySeeker.findDay(new DayRange(TimeSpec.MaxPeriodTime), 10);
        assertThat(day).isNull();
    }

    @Test
    public void seekWeekendHolidayTest() {

        DayRange start = new DayRange(Times.asDate(2011, 2, 15));

        CalendarVisitorFilter filter = new CalendarVisitorFilter();
        filter.addWorkingWeekdays();
        filter.getExcludePeriods().add(new DayRangeCollection(2011, 2, 27, 14)); // 14 days -> week 9 and week 10

        DaySeeker daySeeker = new DaySeeker(filter);

        DayRange day1 = daySeeker.findDay(start, 3);
        assertThat(day1).isEqualTo(new DayRange(2011, 2, 18));

        DayRange day2 = daySeeker.findDay(start, 4);                // 주말 (19, 20) 제외
        assertThat(day2).isEqualTo(new DayRange(2011, 2, 21));

        DayRange day3 = daySeeker.findDay(start, 10);                // 주말 (19, 20) 제외, 2.27부터 14일간 휴가
        assertThat(day3).isEqualTo(new DayRange(2011, 3, 15));
    }

}
