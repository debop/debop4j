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

package kr.debop4j.timeperiod.test.timelines;

import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.test.samples.SchoolDay;
import kr.debop4j.timeperiod.timeline.TimeGapCalculator;
import kr.debop4j.timeperiod.timerange.CalendarTimeRange;
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.timerange.DayRangeCollection;
import kr.debop4j.timeperiod.timerange.MonthRange;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static kr.debop4j.timeperiod.tools.Times.asDate;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timelines.TimeGapCalculatorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 26. 오전 12:39
 */
@Slf4j
public class TimeGapCalculatorTest extends TimePeriodTestBase {

    @Test
    public void noPeriods() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 5));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection gaps = calculator.getGaps(new TimePeriodCollection(), limits);
        assertThat(gaps.size()).isEqualTo(1);
        assertThat(gaps.get(0).isSamePeriod(limits)).isTrue();
    }

    @Test
    public void periodEqualsLimits() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 5));
        TimeGapCalculator calculator = new TimeGapCalculator();
        ITimePeriodCollection excludePeriods = new TimePeriodCollection(limits);

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(0);
    }

    @Test
    public void periodLargetThanLimits() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 5));
        TimeGapCalculator calculator = new TimeGapCalculator();
        ITimePeriodCollection excludePeriods = new TimePeriodCollection(new TimeRange(asDate(2011, 2, 1), asDate(2011, 4, 1)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(0);
    }

    @Test
    public void periodOutsideLimits() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 5));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(asDate(2011, 2, 1), asDate(2011, 2, 5)),
                                         new TimeRange(asDate(2011, 4, 1), asDate(2011, 4, 5)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(1);
        assertThat(gaps.get(0).isSamePeriod(limits)).isTrue();
    }

    @Test
    public void periodOutsideTouchingLimits() {
        TimeRange limits = new MonthRange(2011, 3);
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(asDate(2011, 2, 1), asDate(2011, 3, 5)),
                                         new TimeRange(asDate(2011, 3, 20), asDate(2011, 4, 15)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(1);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(asDate(2011, 3, 5), asDate(2011, 3, 20)))).isTrue();
    }

    @Test
    public void simpleGaps() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 20));
        TimeGapCalculator calculator = new TimeGapCalculator();

        TimeRange excludeRange = new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 15));
        ITimePeriodCollection excludePeriods = new TimePeriodCollection(excludeRange);

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(2);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(limits.getStart(), excludeRange.getStart()))).isTrue();
        assertThat(gaps.get(1).isSamePeriod(new TimeRange(excludeRange.getEnd(), limits.getEnd()))).isTrue();
    }

    @Test
    public void periodTouchingLimitsStart() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 20));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 10)));


        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(1);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 20)))).isTrue();
    }

    @Test
    public void periodTouchingLimitsEnd() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 20));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 20)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(1);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 10)))).isTrue();
    }

    @Test
    public void momentPeriod() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 1), asDate(2011, 3, 20));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(asDate(2011, 3, 10), asDate(2011, 3, 10)));

        // Gap 검사 시에 Moment는 제외된다!!!

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(1);
        assertThat(gaps.get(0).isSamePeriod(limits)).isTrue();
    }

    @Test
    public void touchingPeriods() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 29), asDate(2011, 4, 1));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(new DateTime(2011, 3, 30, 0, 00), new DateTime(2011, 3, 30, 8, 30)),
                                         new TimeRange(new DateTime(2011, 3, 30, 8, 30), new DateTime(2011, 3, 30, 12, 00)),
                                         new TimeRange(new DateTime(2011, 3, 30, 10, 00), new DateTime(2011, 3, 31, 00, 00)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(2);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(new DateTime(2011, 3, 29, 0, 0), new DateTime(2011, 3, 30, 0, 0)))).isTrue();
        assertThat(gaps.get(1).isSamePeriod(new TimeRange(new DateTime(2011, 3, 31, 0, 0), new DateTime(2011, 4, 1, 0, 0)))).isTrue();
    }

    @Test
    public void overlappingPeriods1() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 29), asDate(2011, 4, 1));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(new DateTime(2011, 3, 30, 0, 0), new DateTime(2011, 3, 31, 0, 0)),
                                         new TimeRange(new DateTime(2011, 3, 30, 0, 0), new DateTime(2011, 3, 30, 12, 0)),
                                         new TimeRange(new DateTime(2011, 3, 30, 12, 0), new DateTime(2011, 3, 31, 0, 0)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(2);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(new DateTime(2011, 3, 29, 0, 0), new DateTime(2011, 3, 30, 0, 0)))).isTrue();
        assertThat(gaps.get(1).isSamePeriod(new TimeRange(new DateTime(2011, 3, 31, 0, 0), new DateTime(2011, 4, 1, 0, 0)))).isTrue();
    }

    @Test
    public void overlappingPeriods2() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 29), asDate(2011, 4, 1));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(new DateTime(2011, 3, 30, 0, 0), new DateTime(2011, 3, 31, 0, 0)),
                                         new TimeRange(new DateTime(2011, 3, 30, 0, 0), new DateTime(2011, 3, 30, 6, 30)),
                                         new TimeRange(new DateTime(2011, 3, 30, 8, 30), new DateTime(2011, 3, 30, 12, 0)),
                                         new TimeRange(new DateTime(2011, 3, 30, 22, 30), new DateTime(2011, 3, 31, 0, 0)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(2);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(new DateTime(2011, 3, 29, 0, 0), new DateTime(2011, 3, 30, 0, 0)))).isTrue();
        assertThat(gaps.get(1).isSamePeriod(new TimeRange(new DateTime(2011, 3, 31, 0, 0), new DateTime(2011, 4, 1, 0, 0)))).isTrue();
    }

    @Test
    public void overlappingPeriods3() {
        TimeRange limits = new TimeRange(asDate(2011, 3, 29), asDate(2011, 4, 1));
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods =
                new TimePeriodCollection(new TimeRange(new DateTime(2011, 3, 30, 0, 0), new DateTime(2011, 3, 31, 0, 0)),
                                         new TimeRange(new DateTime(2011, 3, 30, 0, 0), new DateTime(2011, 3, 31, 0, 0)));

        ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);
        assertThat(gaps.size()).isEqualTo(2);
        assertThat(gaps.get(0).isSamePeriod(new TimeRange(new DateTime(2011, 3, 29, 0, 0), new DateTime(2011, 3, 30, 0, 0)))).isTrue();
        assertThat(gaps.get(1).isSamePeriod(new TimeRange(new DateTime(2011, 3, 31, 0, 0), new DateTime(2011, 4, 1, 0, 0)))).isTrue();
    }

    @Test
    public void getGap() {
        DateTime now = Times.now();
        SchoolDay schoolDay = new SchoolDay(now);
        TimeGapCalculator calculator = new TimeGapCalculator();

        ITimePeriodCollection excludePeriods = new TimePeriodCollection();
        excludePeriods.addAll(schoolDay);

        assertThat(calculator.getGaps(excludePeriods).size()).isEqualTo(0);
        assertThat(calculator.getGaps(excludePeriods, schoolDay).size()).isEqualTo(0);

        excludePeriods.clear();
        excludePeriods.add(schoolDay.getLesson1());
        excludePeriods.add(schoolDay.getLesson2());
        excludePeriods.add(schoolDay.getLesson3());
        excludePeriods.add(schoolDay.getLesson4());

        ITimePeriodCollection gaps2 = calculator.getGaps(excludePeriods);
        assertThat(gaps2.size()).isEqualTo(3);
        assertThat(gaps2.get(0).isSamePeriod(schoolDay.getBreak1())).isTrue();
        assertThat(gaps2.get(1).isSamePeriod(schoolDay.getBreak2())).isTrue();
        assertThat(gaps2.get(2).isSamePeriod(schoolDay.getBreak3())).isTrue();

        TimeRange testRange3 = new TimeRange(schoolDay.getLesson1().getStart(), schoolDay.getLesson4().getEnd());
        ITimePeriodCollection gaps3 = calculator.getGaps(excludePeriods, testRange3);
        assertThat(gaps3.size()).isEqualTo(3);
        assertThat(gaps3.get(0).isSamePeriod(schoolDay.getBreak1())).isTrue();
        assertThat(gaps3.get(1).isSamePeriod(schoolDay.getBreak2())).isTrue();
        assertThat(gaps3.get(2).isSamePeriod(schoolDay.getBreak3())).isTrue();

        TimeRange testRange4 = new TimeRange(schoolDay.getStart().plusHours(-1), schoolDay.getEnd().plusHours(1));
        ITimePeriodCollection gaps4 = calculator.getGaps(excludePeriods, testRange4);
        assertThat(gaps4.size()).isEqualTo(5);
        assertThat(gaps4.get(0).isSamePeriod(new TimeRange(testRange4.getStart(), schoolDay.getStart()))).isTrue();
        assertThat(gaps4.get(1).isSamePeriod(schoolDay.getBreak1())).isTrue();
        assertThat(gaps4.get(2).isSamePeriod(schoolDay.getBreak2())).isTrue();
        assertThat(gaps4.get(3).isSamePeriod(schoolDay.getBreak3())).isTrue();
        assertThat(gaps4.get(4).isSamePeriod(new TimeRange(schoolDay.getEnd(), testRange4.getEnd()))).isTrue();


        excludePeriods.clear();
        excludePeriods.add(schoolDay.getLesson1());
        ITimePeriodCollection gaps8 = calculator.getGaps(excludePeriods, schoolDay.getLesson1());
        assertThat(gaps8.size()).isEqualTo(0);

        excludePeriods.clear();
        excludePeriods.add(schoolDay.getLesson1());
        ITimePeriod testRange9 = new TimeRange(schoolDay.getLesson1().getStart().plus(-1),
                                               schoolDay.getLesson1().getEnd().plus(1));
        ITimePeriodCollection gaps9 = calculator.getGaps(excludePeriods, testRange9);

        assertThat(gaps9.size()).isEqualTo(2);
        assertThat(gaps9.get(0).getDuration()).isEqualTo(Durations.Millisecond);
        assertThat(gaps9.get(1).getDuration()).isEqualTo(Durations.Millisecond);
    }

    @Test
    public void calendarGetGap() {
        ITimeCalendar[] timeCalendars = new TimeCalendar[] { TimeCalendar.getDefault(), TimeCalendar.getEmptyOffset() };

        for (ITimeCalendar timeCalendar : timeCalendars) {

            // simulation of same reservations
            TimePeriodCollection excludePeriods =
                    new TimePeriodCollection(new DayRangeCollection(2011, 3, 7, 2, timeCalendar),
                                             new DayRangeCollection(2011, 3, 16, 2, timeCalendar));

            // the overall search range
            ITimePeriod limits = new CalendarTimeRange(asDate(2011, 3, 4), asDate(2011, 3, 21), timeCalendar);
            DayRangeCollection days = new DayRangeCollection(limits.getStart(), (int) limits.getDuration().getStandardDays() + 1, timeCalendar);

            // limits 의 내부이고, 주말인 DayRange를 제외목록에 추가합니다.
            for (DayRange day : days.getDays()) {
                if (limits.hasInside(day) && Times.isWeekend(day.getDayOfWeek()))
                    excludePeriods.add(day);
            }

            TimeGapCalculator calculator = new TimeGapCalculator(timeCalendar);
            ITimePeriodCollection gaps = calculator.getGaps(excludePeriods, limits);

            for (ITimePeriod gap : gaps)
                log.trace("gap=[{}]", gap);

            assertThat(gaps.size()).isEqualTo(4);
            assertThat(gaps.get(0).isSamePeriod(new TimeRange(asDate(2011, 3, 4), Durations.days(1)))).isTrue();
            assertThat(gaps.get(1).isSamePeriod(new TimeRange(asDate(2011, 3, 9), Durations.days(3)))).isTrue();
            assertThat(gaps.get(2).isSamePeriod(new TimeRange(asDate(2011, 3, 14), Durations.days(2)))).isTrue();
            assertThat(gaps.get(3).isSamePeriod(new TimeRange(asDate(2011, 3, 18), Durations.days(1)))).isTrue();
        }
    }
}
