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

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.timeperiod.YearAndWeek;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.WeekRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import kr.debop4j.timeperiod.tools.Weeks;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static kr.debop4j.timeperiod.tools.Times.asDate;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.tools.WeeksTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 12:55
 */
@Slf4j
public class WeeksTest extends TimePeriodTestBase {

    public static final DateTime[] testTimes = new DateTime[] {
            asDate(2003, 12, 28)
    };

    @Test
    public void getYearAndWeek() {
        for (DateTime moment : testTimes) {
            YearAndWeek yw = Weeks.getYearAndWeek(moment);
            assertThat(yw.getYear()).isEqualTo(moment.getWeekyear());
            assertThat(yw.getWeekOfYear()).isEqualTo(moment.getWeekOfWeekyear());
        }
    }

    @Test
    public void getYearAndWeekTest() {
        Parallels.run(2000, 2100, new Action1<Integer>() {
            @Override
            public void perform(Integer year) {
                DateTime startDay = Times.startTimeOfYear(year);
                DateTime endDay = Times.endTimeOfYear(year - 1);

                YearAndWeek startYW = Weeks.getYearAndWeek(startDay);
                YearAndWeek endYW = Weeks.getYearAndWeek(endDay);

                if (startDay.getDayOfWeek() == TimeSpec.FirstDayOfWeek.getValue())
                    assertThat(endYW.equals(startYW)).isFalse();
                else
                    assertThat(endYW.equals(startYW)).isTrue();
            }
        });
    }

    @Test
    public void getStartWeekRangeOfYear() {
        Parallels.run(2000, 2100, new Action1<Integer>() {
            @Override
            public void perform(Integer year) {
                WeekRange startWeekRange = Weeks.getStartWeekRangeOfYear(year);

                if (log.isTraceEnabled())
                    log.trace("year=[{}], startWeek=[{}]", year, startWeekRange.getStartDayStart());

                assertThat(new Duration(asDate(year - 1, 12, 28), startWeekRange.getStartDayStart()).getStandardDays()).isGreaterThan(0);
                assertThat(new Duration(asDate(year, 1, 3), startWeekRange.getEndDayStart()).getStandardDays()).isGreaterThan(0);
            }
        });
    }

    @Test
    public void getEndYearAndWeekTest() {
        Parallels.run(1980, 2200, new Action1<Integer>() {
            @Override
            public void perform(Integer year) {
                YearAndWeek yw = Weeks.getEndYearAndWeek(year);

                assertThat(year).isEqualTo(yw.getYear());
                assertThat(yw.getWeekOfYear()).isGreaterThanOrEqualTo(52);
            }
        });
    }

    @Test
    public void getEndWeekRangeOfYear() {
        Parallels.run(2000, 2100, new Action1<Integer>() {
            @Override
            public void perform(Integer year) {
                WeekRange startWeekRange = Weeks.getStartWeekRangeOfYear(year);
                WeekRange endWeekRange = Weeks.getEndWeekRangeOfYear(year - 1);

                if (log.isTraceEnabled())
                    log.trace("year=[{}], startWeek=[{}], endWeek=[{}]",
                              year, startWeekRange.getStartDayStart(), endWeekRange.getStartDayStart());

                assertThat(new Duration(asDate(year - 1, 12, 28), startWeekRange.getStartDayStart()).getStandardDays()).isGreaterThan(0);
                assertThat(new Duration(asDate(year, 1, 3), startWeekRange.getEndDayStart()).getStandardDays()).isGreaterThan(0);

                assertThat(endWeekRange.getStartDayStart().plusWeeks(1)).isEqualTo(startWeekRange.getStartDayStart());
                assertThat(endWeekRange.getEndDayStart().plusDays(1)).isEqualTo(startWeekRange.getStartDayStart());

            }
        });
    }

    @Test
    public void getWeekRangeTest() {
        Parallels.run(2000, 2100, new Action1<Integer>() {
            @Override
            public void perform(Integer year) {
                DateTime endDay = Times.endTimeOfYear(year - 1);
                DateTime startDay = Times.startTimeOfYear(year);

                YearAndWeek endDayYearWeek = Weeks.getYearAndWeek(endDay);
                assertThat(endDayYearWeek.getYear()).isGreaterThanOrEqualTo(year - 1);

                YearAndWeek startDayYearWeek = Weeks.getYearAndWeek(startDay);
                assertThat(startDayYearWeek.getYear()).isLessThanOrEqualTo(year);

                // 해당일자가 속한 주차의 일자들을 구한다. 년말/년초 구간은 꼭 7일이 아닐 수 있다.
                WeekRange endDayWeekRange = Weeks.getWeekRange(endDayYearWeek);
                WeekRange startDayWeekRange = Weeks.getWeekRange(startDayYearWeek);

                assertThat(endDayWeekRange.hasPeriod()).isTrue();
                assertThat(startDayWeekRange.hasPeriod()).isTrue();

                log.trace("start day weeks=[{}]", startDayWeekRange);

                if (endDayYearWeek.equals(startDayYearWeek)) {
                    assertThat(startDayWeekRange).isEqualTo(endDayWeekRange);
                } else {
                    assertThat(startDayWeekRange).isNotEqualTo(endDayWeekRange);
                }
            }
        });
    }

    @Test
    public void addWeekOfYearsTest() {
        Parallels.run(2000, 2100, new Action1<Integer>() {
            @Override
            public void perform(Integer year) {

                final int step = 2;
                final int maxAddWeeks = 40;

                YearAndWeek prevResult = null;
                YearAndWeek maxWeek = Weeks.getEndYearAndWeek(year);

                for (int week = 1; week < maxWeek.getWeekOfYear(); week += step) {
                    for (int addWeeks = -maxAddWeeks; addWeeks <= maxAddWeeks; addWeeks += step) {
                        YearAndWeek current = new YearAndWeek(year, week);
                        YearAndWeek result = Weeks.addWeekOfYears(current, addWeeks);

                        if (addWeeks != 0 && prevResult != null) {
                            if (result.getYear().equals(prevResult.getYear()))
                                assertThat(result.getWeekOfYear()).isEqualTo(prevResult.getWeekOfYear() + step);
                        }

                        assertThat(result.getWeekOfYear()).isGreaterThan(0);
                        assertThat(result.getWeekOfYear()).isLessThanOrEqualTo(TimeSpec.MaxWeeksPerYear);

                        prevResult = result;
                    }
                }
            }
        });
    }
}
