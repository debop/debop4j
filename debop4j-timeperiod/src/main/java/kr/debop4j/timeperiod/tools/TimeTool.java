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

import kr.debop4j.timeperiod.*;
import lombok.Getter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kr.debop4j.timeperiod.tools.TimeTool
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:44
 */
public abstract class TimeTool {

    private static final Logger log = LoggerFactory.getLogger(TimeTool.class);
    @Getter(lazy = true) private static final boolean traceEnabled = log.isTraceEnabled();
    @Getter(lazy = true) private static final boolean debugEnabled = log.isDebugEnabled();

    private TimeTool() {}

    public static final String NullString = "<null>";
    public static final DateTime UnixEpoch = new DateTime(1970, 1, 1, 0, 0, 0, 0);

    public static String asString(ITimePeriod period) {
        return (period == null) ? NullString : period.getDescription(TimeFormatter.getInstance());
    }

    public static DateTime toDateTime(String value) {
        return toDateTime(value, new DateTime(0));
    }

    public static DateTime toDateTime(String value, DateTime defaultValue) {
        try {
            return DateTime.parse(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T extends ITimePeriod> ITimePeriodCollection toTimePeriodCollection(Iterable<T> sequence) {
        return new TimePeriodCollection(sequence);
    }

    // region << Calendar >>

    public static int getYearOf(ITimeCalendar calendar, DateTime moment) {
        return getYearOf(calendar.getYear(moment), calendar.getMonthOfYear(moment));
    }

    public static int getYearOf(DateTime moment) {
        return getYearOf(moment.getYear(), moment.getMonthOfYear());
    }

    public static int getYearOf(int year, int monthOfYear) {
        return monthOfYear >= 1 ? year : year - 1;
    }

    public static YearAndHalfyear nextHalfyear(HalfyearKind startHalfyearKind) {

    }


    // endregion

    // region << Compare >>

    public static boolean isSameTime(DateTime left, DateTime right, PeriodKind periodKind) {
        switch (periodKind) {
            case Year:
                return isSameYear(left, right);


            default:
                return isSameDateTime(left, right);
        }
    }

    public static boolean isSameYear(DateTime left, DateTime right) {
        return left.getYear() == right.getYear();
    }

    public static boolean isSameHalfyear(DateTime left, DateTime right) {
        if (!isSameYear(left, right))
            return false;
        return getHalfyearOfMonth(left.getMonthOfYear()) == getHalfyearOfMonth(left.getMonthOfYear());
    }

    // endregion
}
