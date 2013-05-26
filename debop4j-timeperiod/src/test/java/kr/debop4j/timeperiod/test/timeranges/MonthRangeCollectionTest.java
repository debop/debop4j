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
import kr.debop4j.timeperiod.YearAndMonth;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.MonthRange;
import kr.debop4j.timeperiod.timerange.MonthRangeCollection;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.MonthRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 10:33
 */
@Slf4j
public class MonthRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void singleMonths() {
        final int startYear = 2004;
        final int startMonth = 6;

        MonthRangeCollection monthRanges = new MonthRangeCollection(startYear, startMonth, 1);
        assertThat(monthRanges.getMonthCount()).isEqualTo(1);

        List<MonthRange> months = monthRanges.getMonths();
        assertThat(months.size()).isEqualTo(1);
        assertThat(months.get(0).isSamePeriod(new MonthRange(startYear, startMonth))).isTrue();

        assertThat(monthRanges.getStartYear()).isEqualTo(startYear);
        assertThat(monthRanges.getEndYear()).isEqualTo(startYear);
        assertThat(monthRanges.getStartMonthOfYear()).isEqualTo(startMonth);
        assertThat(monthRanges.getEndMonthOfYear()).isEqualTo(startMonth);
    }

    @Test
    public void calenarMonths() {
        final int startYear = 2004;
        final int startMonth = 11;
        final int monthCount = 5;

        MonthRangeCollection monthRanges = new MonthRangeCollection(startYear, startMonth, monthCount);

        assertThat(monthRanges.getMonthCount()).isEqualTo(monthCount);
        assertThat(monthRanges.getStartYear()).isEqualTo(startYear);
        assertThat(monthRanges.getStartMonthOfYear()).isEqualTo(startMonth);
        assertThat(monthRanges.getEndYear()).isEqualTo(startYear + 1);
        assertThat(monthRanges.getEndMonthOfYear()).isEqualTo((startMonth + monthCount - 1) % TimeSpec.MonthsPerYear);
    }

    @Test
    public void monthCounts() {
        int[] monthCounts = new int[] { 1, 6, 48, 180, 365 };

        final DateTime now = Times.now();
        final DateTime today = Times.today();

        for (int monthCount : monthCounts) {
            final MonthRangeCollection monthRanges = new MonthRangeCollection(now, monthCount);

            final DateTime startTime = monthRanges.getTimeCalendar().mapStart(Times.trimToDay(today));
            final DateTime endTime = monthRanges.getTimeCalendar().mapEnd(startTime.plusMonths(monthCount));

            assertThat(monthRanges.getStart()).isEqualTo(startTime);
            assertThat(monthRanges.getEnd()).isEqualTo(endTime);

            final List<MonthRange> items = monthRanges.getMonths();

            Parallels.run(monthCount, new Action1<Integer>() {
                @Override
                public void perform(Integer m) {
                    final MonthRange item = items.get(m);
                    assertThat(item.getStart()).isEqualTo(startTime.plusMonths(m));
                    assertThat(item.getEnd()).isEqualTo(monthRanges.getTimeCalendar().mapEnd(startTime.plusMonths(m + 1)));

                    assertThat(item.getUnmappedStart()).isEqualTo(startTime.plusMonths(m));
                    assertThat(item.getUnmappedEnd()).isEqualTo(startTime.plusMonths(m + 1));

                    assertThat(item.isSamePeriod(new MonthRange(monthRanges.getStart().plusMonths(m)))).isTrue();

                    YearAndMonth ym = Times.addMonth(now.getYear(), now.getMonthOfYear(), m);
                    assertThat(item.isSamePeriod(new MonthRange(ym.getYear(), ym.getMonthOfYear()))).isTrue();
                }
            });
        }
    }
}
