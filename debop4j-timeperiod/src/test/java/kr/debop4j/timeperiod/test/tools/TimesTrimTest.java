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
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static kr.debop4j.core.unitTesting.TestTool.runTasks;
import static kr.debop4j.timeperiod.tools.Times.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.tools.TimesTrimTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 24. 오후 4:50
 */
@Slf4j
public class TimesTrimTest extends TimePeriodTestBase {

    @Test
    public void trimMonthTest() {
        assertThat(trimToMonth(testDate)).isEqualTo(asDate(testDate.getYear(), 1, 1));

        runTasks(TimeSpec.MonthsPerYear,
                 new Action1<Integer>() {
                     @Override
                     public void perform(Integer m) {
                         assertThat(Times.trimToMonth(testDate, m + 1)).isEqualTo(asDate(testDate.getYear(), m + 1, 1));
                     }
                 });
    }

    @Test
    public void trimDayTest() {
        assertThat(trimToDay(testDate)).isEqualTo(asDate(testDate.getYear(), testDate.getMonthOfYear(), 1));

        runTasks(getDaysInMonth(testDate.getYear(), testDate.getMonthOfYear()),
                 new Action1<Integer>() {
                     @Override
                     public void perform(Integer day) {
                         assertThat(trimToDay(testDate, day + 1)).isEqualTo(asDate(testDate.getYear(), testDate.getMonthOfYear(), day + 1));
                     }
                 });
    }

    @Test
    public void trimHourTest() {
        assertThat(trimToHour(testDate)).isEqualTo(getDate(testDate));

        runTasks(TimeSpec.HoursPerDay,
                 new Action1<Integer>() {
                     @Override
                     public void perform(Integer h) {
                         assertThat(Times.trimToHour(testDate, h))
                                 .isEqualTo(getDate(testDate).plusHours(h));
                     }
                 });
    }

    @Test
    public void trimMimuteTest() {
        assertThat(trimToMinute(testDate)).isEqualTo(getDate(testDate).plusHours(testDate.getHourOfDay()));

        runTasks(TimeSpec.MinutesPerHour,
                 new Action1<Integer>() {
                     @Override
                     public void perform(Integer m) {
                         assertThat(trimToMinute(testDate, m))
                                 .isEqualTo(getDate(testDate).plusHours(testDate.getHourOfDay()).plusMinutes(m));
                     }
                 });
    }

    @Test
    public void trimSecondTest() {
        assertThat(trimToSecond(testDate)).isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                                                       testDate.getMinuteOfHour(),
                                                                       0,
                                                                       0));

        runTasks(TimeSpec.SecondsPerMinute,
                 new Action1<Integer>() {
                     @Override
                     public void perform(Integer s) {
                         assertThat(Times.trimToSecond(testDate, s))
                                 .isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                                              testDate.getMinuteOfHour(),
                                                              s,
                                                              0));
                     }
                 });
    }

    @Test
    public void trimMillisTest() {
        assertThat(trimToMillis(testDate)).isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                                                       testDate.getMinuteOfHour(),
                                                                       testDate.getSecondOfMinute(),
                                                                       0));

        runTasks(TimeSpec.MillisPerSecond,
                 new Action1<Integer>() {
                     @Override
                     public void perform(Integer ms) {
                         assertThat(trimToMillis(testDate, ms))
                                 .isEqualTo(testDate.withTime(testDate.getHourOfDay(),
                                                              testDate.getMinuteOfHour(),
                                                              testDate.getSecondOfMinute(),
                                                              ms));
                     }
                 });
    }

}
