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

import kr.debop4j.core.Pair;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.tools.TimesMathTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 24. 오전 5:09
 */
@Slf4j
public class TimesMathTest extends TimePeriodTestBase {

    public final DateTime min = new DateTime(2000, 10, 2, 13, 45, 53, 754);
    public final DateTime max = new DateTime(2002, 9, 3, 7, 14, 22, 234);

    @Test
    public void minTest() {
        assertThat(Times.min(min, max)).isEqualTo(min);
        assertThat(Times.min(min, min)).isEqualTo(min);
        assertThat(Times.min(max, max)).isEqualTo(max);

        assertThat(Times.min(min, null)).isEqualTo(min);
        assertThat(Times.min(null, min)).isEqualTo(min);
        assertThat(Times.min((DateTime) null, null)).isNull();
    }

    @Test
    public void maxTest() {
        assertThat(Times.max(min, max)).isEqualTo(max);
        assertThat(Times.max(min, min)).isEqualTo(min);
        assertThat(Times.max(max, max)).isEqualTo(max);

        assertThat(Times.max(max, null)).isEqualTo(max);
        assertThat(Times.max(null, max)).isEqualTo(max);
        assertThat(Times.max((DateTime) null, null)).isNull();
    }

    @Test
    public void adjustPeriodTest() {

        Pair<DateTime, DateTime> pair = Times.adjustPeriod(max, min);
        assertThat(pair.getV1()).isEqualTo(min);
        assertThat(pair.getV2()).isEqualTo(max);

        pair = Times.adjustPeriod(min, max);
        assertThat(pair.getV1()).isEqualTo(min);
        assertThat(pair.getV2()).isEqualTo(max);
    }

    @Test
    public void adjustPeriodByDurationTest() {
        DateTime start = min;
        Duration duration = Durations.Day;

        Pair<DateTime, Duration> pair = Times.adjustPeriod(start, duration);
        assertThat(pair.getV1()).isEqualTo(min);
        assertThat(pair.getV2()).isEqualTo(Durations.Day);

        pair = Times.adjustPeriod(start, Durations.negate(duration));
        assertThat(pair.getV1()).isEqualTo(min.minus(Durations.Day));
        assertThat(pair.getV2()).isEqualTo(Durations.Day);
    }

}

