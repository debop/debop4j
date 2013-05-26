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

import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.test.tools.Times;
import kr.debop4j.timeperiod.timerange.CalendarTimeRange;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.CalendarTimeRangeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 2:24
 */
@Slf4j
public class CalendarTimeRangeTest extends TimePeriodTestBase {

    @Test
    public void calendarTest() {
        ITimeCalendar calendar = new TimeCalendar();
        CalendarTimeRange timeRange = new CalendarTimeRange(TimeRange.Anytime, calendar);

        assertThat(timeRange.getTimeCalendar()).isEqualTo(calendar);
        assertThat(timeRange.isAnytime()).isTrue();
    }

    @Test( expected = AssertionError.class )
    public void momentTest() {
        DateTime today = Times.today();
        new CalendarTimeRange(today, today);
    }
}
