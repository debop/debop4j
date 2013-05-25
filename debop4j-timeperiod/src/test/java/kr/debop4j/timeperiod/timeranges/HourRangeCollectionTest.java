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
import kr.debop4j.timeperiod.timerange.HourRange;
import kr.debop4j.timeperiod.timerange.HourRangeCollection;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.timeranges.HourRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 6:33
 */
@Slf4j
public class HourRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void singleHour() {
        DateTime startTime = new DateTime(2004, 2, 22, 17, 0);
        HourRangeCollection hours = new HourRangeCollection(startTime, 1, TimeCalendar.getEmptyOffset());

        assertThat(hours.getHourCount()).isEqualTo(1);
        assertThat(hours.getStartYear()).isEqualTo(startTime.getYear());
        assertThat(hours.getStartMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
        assertThat(hours.getStartDayOfMonth()).isEqualTo(startTime.getDayOfMonth());
        assertThat(hours.getStartHourOfDay()).isEqualTo(startTime.getHourOfDay());

        assertThat(hours.getEndYear()).isEqualTo(startTime.getYear());
        assertThat(hours.getEndMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
        assertThat(hours.getEndDayOfMonth()).isEqualTo(startTime.getDayOfMonth());
        assertThat(hours.getEndHourOfDay()).isEqualTo(startTime.getHourOfDay() + 1);

        List<HourRange> hourList = hours.getHours();
        assertThat(hourList.size()).isEqualTo(1);
        assertThat(hourList.get(0).isSamePeriod(new HourRange(startTime, TimeCalendar.getEmptyOffset()))).isTrue();
    }

    @Test
    public void calendarHoursTeset() {
        final DateTime startTime = new DateTime(2004, 2, 11, 22, 0);
        final int hourCount = 4;
        HourRangeCollection hours = new HourRangeCollection(startTime, hourCount, TimeCalendar.getEmptyOffset());

        assertThat(hours.getHourCount()).isEqualTo(hourCount);
        assertThat(hours.getStartYear()).isEqualTo(startTime.getYear());
        assertThat(hours.getStartMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
        assertThat(hours.getStartDayOfMonth()).isEqualTo(startTime.getDayOfMonth());
        assertThat(hours.getStartHourOfDay()).isEqualTo(startTime.getHourOfDay());

        assertThat(hours.getEndYear()).isEqualTo(startTime.getYear());
        assertThat(hours.getEndMonthOfYear()).isEqualTo(startTime.getMonthOfYear());
        assertThat(hours.getEndDayOfMonth()).isEqualTo(startTime.getDayOfMonth() + 1);
        assertThat(hours.getEndHourOfDay()).isEqualTo((startTime.getHourOfDay() + hourCount) % 24);

        List<HourRange> hourList = hours.getHours();
        assertThat(hourList.size()).isEqualTo(hourCount);
        for (int h = 0; h < hourCount; h++) {
            assertThat(hourList.get(h).isSamePeriod(new HourRange(startTime.plusHours(h), TimeCalendar.getEmptyOffset()))).isTrue();
        }
    }

    @Test
    public void hoursTest() {

        final int[] hourCounts = new int[] { 1, 24, 48, 64, 128 };
        final DateTime now = Times.now();

        for (int hourCount : hourCounts) {

            final HourRangeCollection hourRanges = new HourRangeCollection(now, hourCount);
            final DateTime startTime = Times.trimToMinute(now).plus(hourRanges.getTimeCalendar().getStartOffset());
            final DateTime endTime = startTime.plusHours(hourCount).plus(hourRanges.getTimeCalendar().getEndOffset());

            assertThat(hourRanges.getStart()).isEqualTo(startTime);
            assertThat(hourRanges.getEnd()).isEqualTo(endTime);
            assertThat(hourRanges.getHourCount()).isEqualTo(hourCount);

            final List<HourRange> items = hourRanges.getHours();
            assertThat(items.size()).isEqualTo(hourCount);

            Parallels.run(hourCount, new Action1<Integer>() {
                @Override
                public void perform(Integer h) {
                    assertThat(items.get(h).getStart()).isEqualTo(startTime.plusHours(h));
                    assertThat(items.get(h).getEnd()).isEqualTo(hourRanges.getTimeCalendar().mapEnd(startTime.plusHours(h + 1)));
                    assertThat(items.get(h).getUnmappedEnd()).isEqualTo(startTime.plusHours(h + 1));
                }
            });
        }
    }
}
