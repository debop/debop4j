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

package kr.debop4j.timeperiod.test.calendars;

import kr.debop4j.core.Action1;
import kr.debop4j.core.unitTesting.TestTool;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static kr.debop4j.timeperiod.tools.Times.now;
import static kr.debop4j.timeperiod.tools.Times.trimToSecond;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.calendars.BusinessCaseTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 27. 오후 3:02
 */
@Slf4j
public class BusinessCaseTest extends TimePeriodTestBase {

    @Test
    public void timeRangeCalendarTimeRange() {
        final DateTime now = now();

        TestTool.runTasks(500, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                DateTime current = now.plusDays(i);
                TimeRange currentFiveSeconds = new TimeRange(trimToSecond(current, 15), trimToSecond(current, 20));

                assertThat(new YearRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new HalfyearRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new QuarterRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new MonthRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new WeekRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new DayRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new HourRange(current).hasInside(currentFiveSeconds)).isTrue();
                assertThat(new MinuteRange(current).hasInside(currentFiveSeconds)).isTrue();
            }
        });

        TimeRange anytime = new TimeRange();

        assertThat(new YearRange().hasInside(anytime)).isFalse();
        assertThat(new HalfyearRange().hasInside(anytime)).isFalse();
        assertThat(new QuarterRange().hasInside(anytime)).isFalse();
        assertThat(new MonthRange().hasInside(anytime)).isFalse();
        assertThat(new WeekRange().hasInside(anytime)).isFalse();
        assertThat(new DayRange().hasInside(anytime)).isFalse();
        assertThat(new HourRange().hasInside(anytime)).isFalse();
        assertThat(new MinuteRange().hasInside(anytime)).isFalse();
    }
}
