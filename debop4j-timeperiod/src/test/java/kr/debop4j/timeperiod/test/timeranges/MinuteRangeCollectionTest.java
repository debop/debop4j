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
import kr.debop4j.timeperiod.timerange.MinuteRange;
import kr.debop4j.timeperiod.timerange.MinuteRangeCollection;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.MinuteRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 9:09
 */
@Slf4j
public class MinuteRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void singleMinutes() {
        DateTime now = Times.now();
        MinuteRangeCollection minutes = new MinuteRangeCollection(now, 1, TimeCalendar.getEmptyOffset());

        DateTime startTime = Times.trimToSecond(now);
        DateTime endTime = Times.trimToSecond(now).plusMinutes(1);

        assertThat(minutes.getMinuteCount()).isEqualTo(1);

        assertThat(minutes.getStart()).isEqualTo(startTime);
        assertThat(minutes.getEnd()).isEqualTo(endTime);

        List<MinuteRange> mins = minutes.getMinutes();

        assertThat(mins.size()).isEqualTo(1);
        assertThat(mins.get(0).getStart()).isEqualTo(startTime);
        assertThat(mins.get(0).getEnd()).isEqualTo(endTime);
    }

    @Test
    public void calendarMinutes() {
        DateTime now = Times.now();

        for (int m = 1; m < 97; m += 5) {
            MinuteRangeCollection minutes = new MinuteRangeCollection(now, m);

            DateTime startTime = Times.trimToSecond(now);
            DateTime endTime = Times.trimToSecond(now).plusMinutes(m).plus(minutes.getTimeCalendar().getEndOffset());

            assertThat(minutes.getMinuteCount()).isEqualTo(m);
            assertThat(minutes.getStart()).isEqualTo(startTime);
            assertThat(minutes.getEnd()).isEqualTo(endTime);

            List<MinuteRange> items = minutes.getMinutes();

            for (int i = 0; i < m; i++) {
                assertThat(items.get(i).getStart()).isEqualTo(startTime.plusMinutes(i));
                assertThat(items.get(i).getUnmappedStart()).isEqualTo(startTime.plusMinutes(i));

                assertThat(items.get(i).getEnd()).isEqualTo(minutes.getTimeCalendar().mapEnd(startTime.plusMinutes(i + 1)));
                assertThat(items.get(i).getUnmappedEnd()).isEqualTo(startTime.plusMinutes(i + 1));
            }
        }
    }

    @Test
    public void minutesTest() {

        final int[] minuteCounts = new int[] { 1, 24, 48, 64, 128 };
        final DateTime now = Times.now();

        for (int minuteCount : minuteCounts) {

            final MinuteRangeCollection minuteRanges = new MinuteRangeCollection(now, minuteCount);
            final DateTime startTime = Times.trimToSecond(now).plus(minuteRanges.getTimeCalendar().getStartOffset());
            final DateTime endTime = startTime.plusMinutes(minuteCount).plus(minuteRanges.getTimeCalendar().getEndOffset());

            assertThat(minuteRanges.getStart()).isEqualTo(startTime);
            assertThat(minuteRanges.getEnd()).isEqualTo(endTime);
            assertThat(minuteRanges.getMinuteCount()).isEqualTo(minuteCount);

            final List<MinuteRange> items = minuteRanges.getMinutes();
            assertThat(items.size()).isEqualTo(minuteCount);

            Parallels.run(minuteCount, new Action1<Integer>() {
                @Override
                public void perform(Integer m) {
                    assertThat(items.get(m).getStart()).isEqualTo(startTime.plusMinutes(m));
                    assertThat(items.get(m).getEnd()).isEqualTo(minuteRanges.getTimeCalendar().mapEnd(startTime.plusMinutes(m + 1)));
                    assertThat(items.get(m).getUnmappedEnd()).isEqualTo(startTime.plusMinutes(m + 1));
                }
            });
        }
    }
}
