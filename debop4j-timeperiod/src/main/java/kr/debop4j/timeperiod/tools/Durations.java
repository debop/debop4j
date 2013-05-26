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

import kr.debop4j.timeperiod.Halfyear;
import kr.debop4j.timeperiod.Quarter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

import static kr.debop4j.timeperiod.tools.Times.startTimeOfHalfyear;
import static kr.debop4j.timeperiod.tools.Times.startTimeOfQuarter;

/**
 * 기간에 대한 메소드를 제공합니다. 참고: {@link Duration}
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:58
 */
@Slf4j
public abstract class Durations {

    private Durations() {}

    public static Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    /** 기간이 0 인 Duration */
    public static final Duration Zero = Duration.ZERO;

    /** 지정한 duration의 부호를 변경한 값을 반환합니다. */
    public static Duration negate(Duration duration) {
        return Duration.millis(-duration.getMillis());
    }

    public static Duration create(DateTime start, DateTime end) {
        return new Duration(start, end);
    }

    /** 해당 년도의 기간 */
    public static Duration year(int year) {
        DateTime start = Times.startTimeOfYear(year);
        DateTime end = start.plusYears(1);
        return new Duration(start, end);
    }

    public static Duration halfyear(int year, Halfyear halfyear) {
        DateTime start = startTimeOfHalfyear(year, halfyear);
        DateTime end = start.plusMonths(TimeSpec.MonthsPerHalfyear);
        return new Duration(start, end);
    }

    public static Duration quarter(int year, Quarter quarter) {
        DateTime start = startTimeOfQuarter(year, quarter);
        DateTime end = start.plusMonths(TimeSpec.MonthsPerQuarter);
        return new Duration(start, end);
    }

    public static Duration month(int year, int monthOfYear) {
        DateTime start = Times.startTimeOfMonth(year, monthOfYear);
        DateTime end = start.plusMonths(1);
        return new Duration(start, end);
    }

    public static final Duration Week = weeks(1);

    public static Duration weeks(int weeks) {
        return (weeks == 0) ? Zero : days(weeks * TimeSpec.DaysPerWeek);
    }

    /** 1일 (하루) */
    public static final Duration Day = Duration.standardDays(1);

    public static Duration days(int days) {
        return (days == 0) ? Zero : Duration.standardDays(days);
    }

    public static Duration days(int days, int hours) {
        return days(days, hours, 0, 0, 0);
    }

    public static Duration days(int days, int hours, int minutes) {
        return days(days, hours, minutes, 0, 0);
    }

    public static Duration days(int days, int hours, int minutes, int seconds) {
        return days(days, hours, minutes, seconds, 0);
    }

    public static Duration days(int days, int hours, int minutes, int seconds, int millis) {
        return Duration.millis(days * TimeSpec.MillisPerDay +
                                       hours * TimeSpec.MillisPerHour +
                                       minutes * TimeSpec.MillisPerMinute +
                                       seconds * TimeSpec.MillisPerSecond +
                                       millis);
    }

    /** 한 시간 */
    public static final Duration Hour = Duration.standardHours(1);

    public static Duration hours(int hours) {
        return Duration.standardHours(hours);
    }

    public static Duration hours(int hours, int minutes) {
        return hours(hours, minutes, 0, 0);
    }

    public static Duration hours(int hours, int minutes, int seconds) {
        return hours(hours, minutes, seconds, 0);
    }

    public static Duration hours(int hours, int minutes, int seconds, int millis) {
        return Duration.millis(hours * TimeSpec.MillisPerHour +
                                       minutes * TimeSpec.MillisPerMinute +
                                       seconds * TimeSpec.MillisPerSecond +
                                       millis);
    }

    public static final Duration Minute = minutes(1, 0, 0);

    public static Duration minutes(int minutes) {
        return minutes(minutes, 0, 0);
    }

    public static Duration minutes(int minutes, int seconds) {
        return minutes(minutes, seconds, 0);
    }

    public static Duration minutes(int minutes, int seconds, int millis) {
        return Duration.millis(minutes * TimeSpec.MillisPerMinute
                                       + seconds * TimeSpec.MillisPerSecond
                                       + millis);
    }


    public static final Duration Second = seconds(1);

    public static Duration seconds(int seconds) {
        return seconds(seconds, 0);
    }

    public static Duration seconds(int seconds, int millis) {
        return Duration.millis(seconds * TimeSpec.MillisPerSecond + millis);
    }

    public static final Duration Millisecond = millis(1L);

    public static Duration millis(long milliseconds) {
        return Duration.millis(milliseconds);
    }
}
