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

package kr.debop4j.timeperiod.timeranges;

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.YearRange;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.timeranges.YearRangeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 26. 오전 12:23
 */
@Slf4j
public class YearRangeTest extends TimePeriodTestBase {

    @Test
    public void initValuesTest() {
        DateTime now = Times.now();
        DateTime thisYear = Times.startTimeOfYear(now);
        DateTime nextYear = thisYear.plusYears(1);

        YearRange yearRange = new YearRange(now, TimeCalendar.getEmptyOffset());

        assertThat(yearRange.getStart().getYear()).isEqualTo(thisYear.getYear());
        assertThat(yearRange.getStart()).isEqualTo(thisYear);
        assertThat(yearRange.getEnd()).isEqualTo(nextYear);
    }

    @Test
    public void startYear() {
        int currentYear = Times.currentYear().getYear();

        assertThat(new YearRange(2008).getBaseYear()).isEqualTo(2008);
        assertThat(new YearRange(currentYear).getBaseYear()).isEqualTo(currentYear);

        assertThat(new YearRange(Times.asDate(2008, 7, 28))).isEqualTo(2008);
    }

    @Test
    public void yearIndex() {
        int yearIndex = 1994;
        YearRange yearRange = new YearRange(yearIndex, TimeCalendar.getEmptyOffset());
        assertThat(yearRange.isReadonly()).isTrue();
        assertThat(yearRange.getBaseYear()).isEqualTo(yearIndex);
        assertThat(yearRange.getStart()).isEqualTo(Times.startTimeOfYear(yearIndex));
        assertThat(yearRange.getEnd()).isEqualTo(Times.startTimeOfYear(yearIndex + 1));
    }

    @Test
    public void addYearsTest() {

        final DateTime now = Times.now();
        final DateTime startYear = Times.startTimeOfYear(now);
        final YearRange yearRange = new YearRange(now);


        assertThat(yearRange.previousYear().getStart()).isEqualTo(startYear.plusYears(-1));
        assertThat(yearRange.nextYear().getStart()).isEqualTo(startYear.plusYears(1));

        assertThat(yearRange.addYears(0)).isEqualTo(yearRange);

        Parallels.run(-60, 120, new Action1<Integer>() {
            @Override
            public void perform(Integer y) {
                assertThat(yearRange.addYears(y).getStart()).isEqualTo(startYear.plusYears(y));
            }
        });
    }
}
