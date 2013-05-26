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

package kr.debop4j.timeperiod.test.base;

import kr.debop4j.timeperiod.TimePeriodBase;
import kr.debop4j.timeperiod.Timepart;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * {@link Timepart} 테스트 코드
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 18. 오후 9:57
 */
@Slf4j
public class TimepartTest extends TimePeriodBase {

    private static final long serialVersionUID = -5272329190162193573L;

    @Test
    public void timeConstructorTest() {
        DateTime now = DateTime.now();
        Timepart time = new Timepart(now);

        TimepartTest.log.debug("now=[{}], time=[{}]", now, time);

        assertThat(time.getHourOfDay()).isEqualTo(now.getHourOfDay());
        assertThat(time.getMinuteOfHour()).isEqualTo(now.getMinuteOfHour());
        assertThat(time.getSecondOfMinute()).isEqualTo(now.getSecondOfMinute());
        assertThat(time.getMillisOfSecond()).isEqualTo(now.getMillisOfSecond());

        assertThat(time.getMillis()).isEqualTo(now.getMillisOfDay());
    }

    @Test
    public void emptyDateTimeConstructor() {
        DateTime today = Times.today();
        Timepart time = Times.timepart(today);

        assertThat(time.getMillis()).isEqualTo(0);

        assertThat(time.getHourOfDay()).isEqualTo(0);
        assertThat(time.getMinuteOfHour()).isEqualTo(0);
        assertThat(time.getSecondOfMinute()).isEqualTo(0);
        assertThat(time.getMillisOfSecond()).isEqualTo(0);
        assertThat(time.getMillisOfSecond()).isEqualTo(0);

        assertThat(time.getTotalHours()).isEqualTo(0);
        assertThat(time.getTotalMinutes()).isEqualTo(0);
        assertThat(time.getTotalSeconds()).isEqualTo(0);
        assertThat(time.getTotalMillis()).isEqualTo(0);
    }

    @Test
    public void constructorTest() {
        Timepart time = new Timepart(18, 23, 56, 344);

        assertThat(time.getHourOfDay()).isEqualTo(18);
        assertThat(time.getMinuteOfHour()).isEqualTo(23);
        assertThat(time.getSecondOfMinute()).isEqualTo(56);
        assertThat(time.getMillisOfSecond()).isEqualTo(344);
    }

    @Test
    public void emptyConstructorTest() {
        Timepart time = new Timepart();

        assertThat(time.getHourOfDay()).isEqualTo(0);
        assertThat(time.getMinuteOfHour()).isEqualTo(0);
        assertThat(time.getSecondOfMinute()).isEqualTo(0);
        assertThat(time.getMillisOfSecond()).isEqualTo(0);
        assertThat(time.getMillisOfSecond()).isEqualTo(0);

        assertThat(time.getTotalHours()).isEqualTo(0);
        assertThat(time.getTotalMinutes()).isEqualTo(0);
        assertThat(time.getTotalSeconds()).isEqualTo(0);
        assertThat(time.getTotalMillis()).isEqualTo(0);
    }

    @Test
    public void durationTest() {
        Duration test = Durations.hours(18, 23, 56, 344);
        Timepart time = new Timepart(test);

        assertThat(time.getHourOfDay()).isEqualTo(18);
        assertThat(time.getMinuteOfHour()).isEqualTo(23);
        assertThat(time.getSecondOfMinute()).isEqualTo(56);
        assertThat(time.getMillisOfSecond()).isEqualTo(344);

        assertThat(time.getMillis()).isEqualTo(test.getMillis());
    }

    @Test
    public void getDateTimeTest() {
        DateTime now = Times.now();
        Duration test = Durations.hours(18, 23, 56, 344);
        Timepart time = new Timepart(test);

        assertThat(time.getDateTime(now)).isEqualTo(now.withTimeAtStartOfDay().plus(test));
    }

    @Test
    public void getEmptyDateTimeTest() {
        DateTime today = Times.today();
        Timepart time = new Timepart();

        assertThat(time.getDateTime(today)).isEqualTo(today);
        assertThat(time.getDateTime(today).getMillisOfDay()).isEqualTo(0);
    }

}
