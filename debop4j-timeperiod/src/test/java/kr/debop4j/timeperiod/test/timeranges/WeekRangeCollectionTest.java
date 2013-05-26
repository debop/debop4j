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
import kr.debop4j.timeperiod.YearAndWeek;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.test.tools.Times;
import kr.debop4j.timeperiod.test.tools.Weeks;
import kr.debop4j.timeperiod.timerange.WeekRange;
import kr.debop4j.timeperiod.timerange.WeekRangeCollection;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.WeekRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 11:49
 */
@Slf4j
public class WeekRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void singleMonths() {
        final int startYear = 2004;
        final int startWeek = 22;

        WeekRangeCollection weekRanges = new WeekRangeCollection(startYear, startWeek, 1);
        assertThat(weekRanges.getWeekCount()).isEqualTo(1);

        assertThat(weekRanges.getStartYear()).isEqualTo(startYear);
        assertThat(weekRanges.getEndYear()).isEqualTo(startYear);
        assertThat(weekRanges.getStartWeekOfYear()).isEqualTo(startWeek);
        assertThat(weekRanges.getEndWeekOfYear()).isEqualTo(startWeek);

        List<WeekRange> weeks = weekRanges.getWeeks();
        assertThat(weeks.size()).isEqualTo(1);
        assertThat(weeks.get(0).isSamePeriod(new WeekRange(startYear, startWeek))).isTrue();
    }

    @Test
    public void calenarWeeks() {
        final int startYear = 2004;
        final int startWeek = 22;
        final int weekCount = 5;

        WeekRangeCollection weekRanges = new WeekRangeCollection(startYear, startWeek, weekCount);

        assertThat(weekRanges.getWeekCount()).isEqualTo(weekCount);
        assertThat(weekRanges.getStartYear()).isEqualTo(startYear);
        assertThat(weekRanges.getStartWeekOfYear()).isEqualTo(startWeek);
        assertThat(weekRanges.getEndYear()).isEqualTo(startYear);
        assertThat(weekRanges.getEndWeekOfYear()).isEqualTo((startWeek + weekCount - 1));
    }

    @Test
    public void weeksCountsTest() {
        int[] weekCounts = new int[] { 1, 6, 48, 180, 365 };

        final DateTime now = Times.now();
        final DateTime today = Times.today();

        for (int weekCount : weekCounts) {
            final WeekRangeCollection weekRanges = new WeekRangeCollection(now, weekCount);

            final DateTime startTime = weekRanges.getTimeCalendar().mapStart(Times.startTimeOfWeek(today));
            final DateTime endTime = weekRanges.getTimeCalendar().mapEnd(startTime.plusWeeks(weekCount));

            assertThat(weekRanges.getStart()).isEqualTo(startTime);
            assertThat(weekRanges.getEnd()).isEqualTo(endTime);

            final List<WeekRange> items = weekRanges.getWeeks();

            Parallels.run(weekCount, new Action1<Integer>() {
                @Override
                public void perform(Integer w) {
                    final WeekRange item = items.get(w);
                    assertThat(item.getStart()).isEqualTo(startTime.plusWeeks(w));
                    assertThat(item.getEnd()).isEqualTo(weekRanges.getTimeCalendar().mapEnd(startTime.plusWeeks(w + 1)));

                    assertThat(item.getUnmappedStart()).isEqualTo(startTime.plusWeeks(w));
                    assertThat(item.getUnmappedEnd()).isEqualTo(startTime.plusWeeks(w + 1));

                    assertThat(item.isSamePeriod(new WeekRange(weekRanges.getStart().plusWeeks(w)))).isTrue();

                    YearAndWeek yw = Weeks.addWeekOfYears(now.getYear(), now.getWeekOfWeekyear(), w);
                    assertThat(item.isSamePeriod(new WeekRange(yw.getYear(), yw.getWeekOfYear()))).isTrue();
                }
            });
        }
    }
}
