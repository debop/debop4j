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

import kr.debop4j.timeperiod.HalfyearKind;
import kr.debop4j.timeperiod.QuarterKind;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * 기간에 대한 메소드를 제공합니다. 참고: {@link Duration}
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:58
 */
@Slf4j
public abstract class DurationTool {

    private DurationTool() {}

    public static Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    public static final Duration Zero = Duration.ZERO;

    public static Duration year(int year) {
        return days(DateTime.now().withDate(year, 12, 31).getDayOfYear());
    }

    public static Duration halfyear(int year, HalfyearKind halfyear) {
        int[] monthsOfHalfyear = TimeTool.getMonthsOfHalfyear(halfyear);
        Duration duration = Duration.millis(0);
        for (int month : monthsOfHalfyear) {
            duration = duration.plus(month(year, month));
        }
        return duration;
    }

    public static Duration quarter(int year, QuarterKind quarter) {
        int[] monthsOfQuarter = TimeTool.getMonthsOfQuarter(quarter);
        Duration duration = Duration.millis(0);
        for (int month : monthsOfQuarter) {
            duration = duration.plus(month(year, month));
        }
        return duration;
    }

    public static Duration month(int year, int month) {
        if (month == 12)
            return days(31);

        DateTime startDate = new DateTime(year, month, 1, 0, 0);
        int days = startDate.getDayOfYear();
        int nextDays = startDate.plusMonths(1).getDayOfYear();
        return days(nextDays - days);
    }

    public static final Duration Week = weeks(1);

    public static Duration weeks(int weeks) {
        return days(weeks * TimeSpec.DaysPerWeek);
    }

    public static final Duration Day = days(1);

    public static Duration days(int days) {
        return days(days, 0, 0, 0, 0);
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

    private static final Duration Hour = hours(1);

    public static Duration hours(int hours) {
        return hours(hours, 0, 0, 0);
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
        return Duration.millis(minutes * TimeSpec.MillisPerMinute +
                                       seconds * TimeSpec.MillisPerSecond +
                                       millis);
    }


    public static final Duration Second = seconds(1);

    public static Duration seconds(int seconds) {
        return seconds(seconds, 0);
    }

    public static Duration seconds(int seconds, int millis) {
        return Duration.millis(seconds * TimeSpec.MillisPerSecond +
                                       millis);
    }

    public static final Duration Millisecond = millis(1L);

    public static Duration millis(long milliseconds) {
        return Duration.millis(milliseconds);
    }
}
