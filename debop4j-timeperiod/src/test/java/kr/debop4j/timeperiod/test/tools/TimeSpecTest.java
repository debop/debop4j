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

import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.tools.TimeSpecTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 21. 오후 5:50
 */
@Slf4j
public class TimeSpecTest extends TimePeriodTestBase {

    @Test
    public void dateUnitTest() {
        assertThat(TimeSpec.MonthsPerYear).isEqualTo(12);
        assertThat(TimeSpec.HalfyearsPerYear).isEqualTo(2);
        assertThat(TimeSpec.QuartersPerYear).isEqualTo(4);
        assertThat(TimeSpec.QuartersPerHalfyear).isEqualTo(2);
        assertThat(TimeSpec.MaxWeeksPerYear).isEqualTo(54);
        assertThat(TimeSpec.MonthsPerHalfyear).isEqualTo(6);
        assertThat(TimeSpec.MonthsPerQuarter).isEqualTo(3);
        assertThat(TimeSpec.MaxDaysPerMonth).isEqualTo(31);
        assertThat(TimeSpec.DaysPerWeek).isEqualTo(7);
        assertThat(TimeSpec.HoursPerDay).isEqualTo(24);
        assertThat(TimeSpec.MinutesPerHour).isEqualTo(60);
        assertThat(TimeSpec.SecondsPerMinute).isEqualTo(60);
        assertThat(TimeSpec.MillisPerSecond).isEqualTo(1000);
    }

    @Test
    public void halfyearTest() {
        assertThat(TimeSpec.FirstHalfyearMonths.length).isEqualTo(TimeSpec.MonthsPerHalfyear);

        for (int i = 0; i < TimeSpec.FirstHalfyearMonths.length; i++)
            assertThat(TimeSpec.FirstHalfyearMonths[i]).isEqualTo(i + 1);

        assertThat(TimeSpec.SecondHalfyearMonths.length).isEqualTo(TimeSpec.MonthsPerHalfyear);

        for (int i = 0; i < TimeSpec.SecondHalfyearMonths.length; i++)
            assertThat(TimeSpec.SecondHalfyearMonths[i]).isEqualTo(i + 7);
    }

    @Test
    public void quarterTest() {
        assertThat(TimeSpec.FirstQuarterMonth).isEqualTo(1);
        assertThat(TimeSpec.SecondQuarterMonth).isEqualTo(TimeSpec.FirstQuarterMonth + TimeSpec.MonthsPerQuarter);
        assertThat(TimeSpec.ThirdQuarterMonth).isEqualTo(TimeSpec.SecondQuarterMonth + TimeSpec.MonthsPerQuarter);
        assertThat(TimeSpec.FourthQuarterMonth).isEqualTo(TimeSpec.ThirdQuarterMonth + TimeSpec.MonthsPerQuarter);

        assertThat(TimeSpec.FirstQuarterMonths.length).isEqualTo(TimeSpec.MonthsPerQuarter);

        for (int i = 0; i < TimeSpec.FirstQuarterMonths.length; i++)
            assertThat(TimeSpec.FirstQuarterMonths[i]).isEqualTo(i + 1);

        for (int i = 0; i < TimeSpec.SecondQuarterMonths.length; i++)
            assertThat(TimeSpec.SecondQuarterMonths[i]).isEqualTo(i + 1 + TimeSpec.MonthsPerQuarter);

        for (int i = 0; i < TimeSpec.ThirdQuarterMonths.length; i++)
            assertThat(TimeSpec.ThirdQuarterMonths[i]).isEqualTo(i + 1 + 2 * TimeSpec.MonthsPerQuarter);

        for (int i = 0; i < TimeSpec.FourthQuarterMonths.length; i++)
            assertThat(TimeSpec.FourthQuarterMonths[i]).isEqualTo(i + 1 + 3 * TimeSpec.MonthsPerQuarter);
    }

    @Test
    public void durationTest() {
        assertThat(TimeSpec.NoDuration).isEqualTo(Duration.ZERO);
        assertThat(TimeSpec.EmptyDuration).isEqualTo(Duration.ZERO);
        assertThat(TimeSpec.ZeroDuration).isEqualTo(Duration.ZERO);
        assertThat(TimeSpec.MinPositiveDuration).isEqualTo(Duration.millis(1));
        assertThat(TimeSpec.MinNegativeDuration).isEqualTo(Duration.millis(-1));
    }
}
