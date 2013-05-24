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

package kr.debop4j.timeperiod.tools;

import kr.debop4j.timeperiod.TimePeriodTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static kr.debop4j.timeperiod.tools.Times.*;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.tools.TimesDateTimeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 23. 오후 11:32
 */
@Slf4j
public class TimesDateTimeTest extends TimePeriodTestBase {

    @Test
    public void getDateTest() {
        assertThat(getDate(testDate)).isEqualTo(testDate.withTimeAtStartOfDay());
        assertThat(getDate(testNow)).isEqualTo(testNow.withTimeAtStartOfDay());
    }

    @Test
    public void setDateTest() {
        assertThat(getDate(setDate(testDate, testNow))).isEqualTo(testNow.withTimeAtStartOfDay());
        assertThat(getDate(setDate(testNow, testDate))).isEqualTo(testDate.withTimeAtStartOfDay());
    }

    @Test
    public void hasTimeOfDayTest() {
        assertThat(hasTime(testDate)).isTrue();
        assertThat(hasTime(testNow)).isTrue();
        assertThat(hasTime(getDate(testNow))).isFalse();

        assertThat(hasTime(setTime(testNow, 1))).isTrue();
        assertThat(hasTime(setTime(testNow, 0, 1))).isTrue();

        assertThat(hasTime(setTime(testNow, 0, 0, 0, 0))).isFalse();
    }

    @Test
    public void setTimeOfDayTest() {
        assertThat(hasTime(setTime(testDate, testNow))).isTrue();
        assertThat(setTime(testDate, testNow).getMillisOfDay()).isEqualTo(testNow.getMillisOfDay());
        assertThat(setTime(testNow, testDate).getMillisOfDay()).isEqualTo(testDate.getMillisOfDay());
    }
}
