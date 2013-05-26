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
import kr.debop4j.timeperiod.timerange.YearRange;
import kr.debop4j.timeperiod.timerange.YearRangeCollection;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.YearRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 26. 오전 12:12
 */
@Slf4j
public class YearRangeCollectionTest extends TimePeriodTestBase {
    @Test
    public void singleYears() {
        final int startYear = 2004;

        YearRangeCollection yearRanges = new YearRangeCollection(startYear, 1);
        assertThat(yearRanges.getYearCount()).isEqualTo(1);

        assertThat(yearRanges.getStartYear()).isEqualTo(startYear);
        assertThat(yearRanges.getEndYear()).isEqualTo(startYear);

        List<YearRange> years = yearRanges.getYears();
        assertThat(years.size()).isEqualTo(1);
        assertThat(years.get(0).isSamePeriod(new YearRange(startYear))).isTrue();
    }

    @Test
    public void calenarMonths() {
        final int startYear = 2004;
        final int yearCount = 5;

        YearRangeCollection yearRanges = new YearRangeCollection(startYear, yearCount, TimeCalendar.getEmptyOffset());

        assertThat(yearRanges.getYearCount()).isEqualTo(yearCount);
        assertThat(yearRanges.getStartYear()).isEqualTo(startYear);
        assertThat(yearRanges.getEndYear()).isEqualTo(startYear + yearCount);
    }

    @Test
    public void yearCounts() {
        int[] yearCounts = new int[] { 1, 6, 48, 180, 365 };

        final DateTime now = Times.now();
        final DateTime today = Times.today();

        for (int yearCount : yearCounts) {
            final YearRangeCollection yearRanges = new YearRangeCollection(now, yearCount);

            final DateTime startTime = yearRanges.getTimeCalendar().mapStart(Times.trimToYear(today));
            final DateTime endTime = yearRanges.getTimeCalendar().mapEnd(startTime.plusYears(yearCount));

            assertThat(yearRanges.getStart()).isEqualTo(startTime);
            assertThat(yearRanges.getEnd()).isEqualTo(endTime);

            final List<YearRange> items = yearRanges.getYears();

            Parallels.run(yearCount, new Action1<Integer>() {
                @Override
                public void perform(Integer y) {
                    final YearRange item = items.get(y);
                    assertThat(item.getStart()).isEqualTo(startTime.plusYears(y));
                    assertThat(item.getEnd()).isEqualTo(yearRanges.getTimeCalendar().mapEnd(startTime.plusYears(y + 1)));

                    assertThat(item.getUnmappedStart()).isEqualTo(startTime.plusYears(y));
                    assertThat(item.getUnmappedEnd()).isEqualTo(startTime.plusYears(y + 1));

                    assertThat(item.isSamePeriod(new YearRange(yearRanges.getStart().plusYears(y)))).isTrue();
                }
            });
        }
    }
}
