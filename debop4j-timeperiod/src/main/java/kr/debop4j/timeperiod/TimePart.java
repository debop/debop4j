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

package kr.debop4j.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.timeperiod.test.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * {@link DateTime}의 Time part 만을 나타냅니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 2:23
 */
public class Timepart extends ValueObjectBase implements Comparable<Timepart> {

    private static final long serialVersionUID = -4029003873537088627L;

    public static Timepart now() {
        return new Timepart(DateTime.now());
    }

    public Timepart() {
        this(new DateTime().withTimeAtStartOfDay());
    }

    public Timepart(Duration duration) {
        this.timepart = new DateTime(0).withMillisOfDay((int) duration.getMillis());
    }

    public Timepart(DateTime time) {
        this.timepart = new DateTime(0).withMillisOfDay(time.getMillisOfDay());
    }

    public Timepart(int hourOfDay) {
        this(hourOfDay, 0, 0, 0);
    }

    public Timepart(int hourOfDay, int minuteOfHour) {
        this(hourOfDay, minuteOfHour, 0, 0);
    }

    public Timepart(int hourOfDay, int minuteOfHour, int secondOfMinute) {
        this(hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    public Timepart(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        this(new DateTime().withTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    @Getter
    private DateTime timepart;

    public int getHourOfDay() {
        return timepart.getHourOfDay();
    }

    public int getMinuteOfHour() {
        return timepart.getMinuteOfHour();
    }

    public int getSecondOfMinute() {
        return timepart.getSecondOfMinute();
    }

    public int getMillisOfSecond() {
        return timepart.getMillisOfSecond();
    }

    public double getTotalHours() {
        return getMillis() / TimeSpec.MillisPerHour;
    }

    public double getTotalMinutes() {
        return getMillis() / TimeSpec.MillisPerMinute;
    }

    public double getTotalSeconds() {
        return getMillis() / TimeSpec.MillisPerSecond;
    }

    public long getTotalMillis() {
        return timepart.getMillisOfDay();
    }

    public long getMillis() {
        return timepart.getMillisOfDay();
    }

    public DateTime getDateTime(DateTime moment) {
        return moment.withTimeAtStartOfDay().plus(getMillis());
    }

    public DateTime getDateTime(Datepart datepart) {
        return datepart.getDateTime(this);
    }

    @Override
    public int compareTo(Timepart o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public int hashCode() {
        return (int) getMillis();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("timepart", timepart);
    }
}
