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
import kr.debop4j.timeperiod.clock.ClockProxy;
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

    public static int getYearOf(DateTime moment) {
        return getYearOf(moment.getYear(), moment.getMonthOfYear());
    }

    public static int getYearOf(DateTime moment, ITimeCalendar calendar) {
        return getYearOf(calendar.getYear(moment), calendar.getMonthOfYear(moment));
    }

    public static int getYearOf(int year, int monthOfYear) {
        return monthOfYear >= 1 ? year : year - 1;
    }

    public static YearAndHalfyear nextHalfyear(int startYear, HalfyearKind startHalfyear) {
        return addHalfyear(startYear, startHalfyear, 1);
    }

    public static YearAndHalfyear previousHalfyear(int startYear, HalfyearKind startHalfyear) {
        return addHalfyear(startYear, startHalfyear, -1);
    }

    public static YearAndHalfyear addHalfyear(int startYear, HalfyearKind startHalfyear, int halfyearCount) {
        int offsetYear = (Math.abs(halfyearCount) / TimeSpec.HalfyearsPerYear) + 1;
        int startHalfyearCount = ((startYear + offsetYear) * TimeSpec.HalfyearsPerYear) + (startHalfyear.getValue() - 1);
        int targetHalfyearCount = startHalfyearCount + halfyearCount;

        int year = (targetHalfyearCount / TimeSpec.HalfyearsPerYear) - offsetYear;
        HalfyearKind halfyear = HalfyearKind.valueOf((targetHalfyearCount % TimeSpec.HalfyearsPerYear) + 1);

        if (log.isTraceEnabled())
            log.trace("addHalfyear. startYear=[{}], startHalfyear=[{}], halfyearCount=[{}], year=[{}], halfyear=[{}]",
                      startYear, startHalfyear, halfyearCount, year, halfyear);

        return new YearAndHalfyear(year, halfyear);
    }

    public static HalfyearKind getHalfyearOfMonth(int monthOfYear) {
        assert monthOfYear >= 1 && monthOfYear <= 12;

        return (monthOfYear <= TimeSpec.MonthsPerHalfyear)
                ? HalfyearKind.First
                : HalfyearKind.Second;
    }

    public static int[] getMonthsOfHalfyear(HalfyearKind halfyear) {
        return (halfyear == HalfyearKind.First)
                ? TimeSpec.FirstHalfyearMonths
                : TimeSpec.SecondHalfyearMonths;
    }

    public static YearAndQuarter nextQuarter(int year, QuarterKind quarter) {
        return addQuarter(year, quarter, 1);
    }

    public static YearAndQuarter previousQuarter(int year, QuarterKind quarter) {
        return addQuarter(year, quarter, -1);
    }

    public static YearAndQuarter addQuarter(int year, QuarterKind quarter, int count) {
        int offsetYear = Math.abs(count) / TimeSpec.QuartersPerYear + 1;
        int startQuarters = (year + offsetYear) * TimeSpec.QuartersPerYear + quarter.getValue() - 1;
        int targetQuarters = startQuarters + count;

        int y = targetQuarters / TimeSpec.QuartersPerYear - offsetYear;
        int q = (targetQuarters % TimeSpec.QuartersPerYear) + 1;

        return new YearAndQuarter(y, q);
    }

    public static QuarterKind getQuarterOfMonth(int monthOfYear) {
        int quarter = (monthOfYear - 1) / TimeSpec.MonthsPerQuarter + 1;
        return QuarterKind.valueOf(quarter);
    }

    public static int[] getMonthsOfQuarter(QuarterKind quarter) {
        switch (quarter) {
            case First:
                return TimeSpec.FirstQuarterMonths;
            case Second:
                return TimeSpec.SecondQuarterMonths;
            case Third:
                return TimeSpec.ThirdQuarterMonths;
            case Fourth:
                return TimeSpec.FourthQuarterMonths;
            default:
                throw new IllegalArgumentException("invalid quarter. quarter=" + quarter);
        }
    }

    public static YearAndMonth nextMonth(int year, int monthOfYear) {
        return addMonth(year, monthOfYear, 1);
    }

    public static YearAndMonth previousMonth(int year, int monthOfYear) {
        return addMonth(year, monthOfYear, -1);
    }

    public static YearAndMonth addMonth(int year, int monthOfYear, int count) {
        int offset = Math.abs(count) / TimeSpec.MonthsPerYear + 1;
        int startMonths = (year + offset) * TimeSpec.MonthsPerYear + monthOfYear - 1;
        int endMonths = startMonths + count;

        int y = endMonths / TimeSpec.MonthsPerYear - offset;
        int m = (endMonths % TimeSpec.MonthsPerYear) + 1;

        return new YearAndMonth(y, m);
    }

    public static int getDaysInMonth(int year, int month) {
        DateTime firstDay = new DateTime(year, month, 1, 0, 0);
        return firstDay.plusMonths(1).plusDays(-1).getDayOfMonth();
    }

    public static DateTime getStartOfWeek(DateTime time) {
        getStartOfWeek(time, TimeSpec.FirstDayOfWeek);
    }

    public static DateTime getStartOfWeek(DateTime time, DayOfWeek firstDayOfWeek) {
        DateTime current = time.withTimeAtStartOfDay();
        while (current.getDayOfWeek() != firstDayOfWeek.getValue()) {
            current = current.minusDays(1);
        }
        return current;
    }

    public static YearAndWeek getWeekOfYear(DateTime moment) {
        return getWeekOfYear(moment, null);
    }

    public static YearAndWeek getWeekOfYear(DateTime moment, ITimeCalendar timeCalendar) {
        return new YearAndWeek(moment.getYear(), moment.getWeekOfWeekyear());
    }

    public static int getWeeksOfYear(int year) {
        return getWeeksOfYear(year, null);
    }

    public static int getWeeksOfYear(int year, ITimeCalendar timeCalendar) {
        return new DateTime().withDate(year, 12, 31).getWeekOfWeekyear();
    }

    public static DateTime getStartOfYearWeek(int year, int weekOfYear) {
        return getStartOfYearWeek(year, weekOfYear, null);
    }

    public static DateTime getStartOfYearWeek(int year, int weekOfYear, ITimeCalendar timeCalendar) {
        return new DateTime().withYear(year).withWeekOfWeekyear(weekOfYear);
    }

    public static DateTime dayStart(DateTime moment) {
        return moment.withTimeAtStartOfDay();
    }

    public static DayOfWeek nextDayOfWeek(DayOfWeek day) {
        return addDayOfWeek(day, 1);
    }

    public static DayOfWeek previousDayOfWeek(DayOfWeek day) {
        return addDayOfWeek(day, -1);
    }

    public static DayOfWeek addDayOfWeek(DayOfWeek day, int days) {
        if (days == 0)
            return day;

        int weeks = Math.abs(days) / TimeSpec.DaysPerWeek;
        int offset = weeks * TimeSpec.DaysPerWeek + day.getValue() + days;
        return DayOfWeek.valueOf((offset % TimeSpec.DaysPerWeek) + 1);
    }

    // endregion

    // region << Compare >>

    /** 두 일자의 값이 {@link PeriodKind} 단위까지 같은지 비교합니다. (상위값들도 같아야 합니다.) */
    public static boolean isSameTime(DateTime left, DateTime right, PeriodKind periodKind) {
        if (log.isTraceEnabled())
            log.trace("두 일자가 값은지 비교합니다. left=[{}], right=[{}], periodKind=[{}]", left, right, periodKind);

        switch (periodKind) {
            case Year:
                return isSameYear(left, right);
            case Halfyear:
                return isSameHalfyear(left, right);
            case Quarter:
                return isSameQuarter(left, right);
            case Month:
                return isSameMonth(left, right);
            case Week:
                return isSameWeek(left, right);
            case Day:
                return isSameDay(left, right);
            case Hour:
                return isSameHour(left, right);
            case Minute:
                return isSameMinute(left, right);
            case Second:
                return isSameSecond(left, right);
            case Millisecond:
            default:
                return isSameDateTime(left, right);
        }
    }

    /** 두 일자의 년(Year) 단위까지 같은지 비교합니다. */
    public static boolean isSameYear(DateTime left, DateTime right) {
        return left.getYear() == right.getYear();
    }

    public static boolean isSameHalfyear(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                getHalfyearOfMonth(left.getMonthOfYear()) == getHalfyearOfMonth(right.getMonthOfYear());
    }

    public static boolean isSameQuarter(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                getQuarterOfMonth(left.getMonthOfYear()) == getQuarterOfMonth(right.getMonthOfYear());
    }

    public static boolean isSameMonth(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                left.getMonthOfYear() == right.getMonthOfYear();
    }

    public static boolean isSameWeek(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                left.getWeekOfWeekyear() == right.getWeekOfWeekyear();
    }

    public static boolean isSameDay(DateTime left, DateTime right) {
        return isSameMonth(left, right) &&
                left.dayOfMonth() == right.dayOfMonth();
    }

    public static boolean isSameHour(DateTime left, DateTime right) {
        return isSameDay(left, right) &&
                left.getHourOfDay() == right.getHourOfDay();
    }

    public static boolean isSameMinute(DateTime left, DateTime right) {
        return isSameDay(left, right) &&
                left.getMinuteOfDay() == right.getMinuteOfDay();
    }

    public static boolean isSameSecond(DateTime left, DateTime right) {
        return isSameDay(left, right) &&
                left.getSecondOfDay() == right.getSecondOfDay();
    }

    public static boolean isSameDateTime(DateTime left, DateTime right) {
        return (left != null) && left.equals(right);
    }


    // endregion

    // region << Current >>

    public static DateTime currentYear() {
        DateTime now = ClockProxy.getClock().now();
        return new DateTime(now.getYear(), 1, 1, 0, 0);
    }

    public static DateTime currentHalfyear() {
        DateTime now = ClockProxy.getClock().now();
        HalfyearKind halfyear = getHalfyearOfMonth(now.getMonthOfYear());
        int month = getMonthsOfHalfyear(halfyear)[0];
        return new DateTime(now.getYear(), month, 1, 0, 0);
    }

    public static DateTime currentQuarter() {
        DateTime now = ClockProxy.getClock().now();
        QuarterKind quarter = getQuarterOfMonth(now.getMonthOfYear());
        int month = getMonthsOfQuarter(quarter)[0];
        return new DateTime(now.getYear(), month, 1, 0, 0);
    }

    public static DateTime currentMonth() {
        return trimToDay(ClockProxy.getClock().now());
    }

    public static DateTime currentWeek() {
        return currentWeek(TimeSpec.FirstDayOfWeek);
    }

    public static DateTime currentWeek(DayOfWeek firstDayOfWeek) {
        return getStartOfWeek(ClockProxy.getClock().now(), firstDayOfWeek);
    }

    public static DateTime currentDay() {
        return ClockProxy.getClock().today();
    }

    public static DateTime currentHour() {
        return trimToMinute(ClockProxy.getClock().now());
    }

    public static DateTime currentMinute() {
        return trimToSecond(ClockProxy.getClock().now());
    }

    public static DateTime currentSecond() {
        return trimToMillis(ClockProxy.getClock().now());
    }

    // endregion << Current >>

    // region << DateTime >>

    public static DateTime startTimeOfYear(DateTime moment) {
        return new DateTime(moment.getYear(), 1, 1, 0, 0);
    }

    public static DateTime startTimeOfYear(int year) {
        return new DateTime(year, 1, 1, 0, 0);
    }

    public static DateTime endTimeOfYear(DateTime moment) {
        return startTimeOfYear(moment.getYear() + 1).plusMillis(-1);
    }

    public static DateTime endTimeOfYear(int year) {
        return startTimeOfYear(year + 1).plusMillis(-1);
    }

    /** 작년 시작일 */
    public static DateTime startTimeOfLastYear(DateTime moment) {
        return startTimeOfYear(moment.getYear() - 1);
    }

    public static DateTime endTimeOfLastYear(DateTime moment) {
        return endTimeOfYear(moment.getYear() - 1);
    }


    public static DateTime startTimeOfHalfyear(DateTime moment) {
        return startTimeOfHalfyear(moment.getYear(), moment.getMonthOfYear());
    }

    public static DateTime startTimeOfHalfyear(int year, int monthOfYear) {
        HalfyearKind halfyear = getHalfyearOfMonth(monthOfYear);
        return new DateTime(year, getMonthsOfHalfyear(halfyear)[0], 1, 0, 0);
    }

    public static DateTime endTimeOfHalfyear(DateTime moment) {
        return endTimeOfHalfyear(moment.getYear(), moment.getMonthOfYear());
    }

    public static DateTime endTimeOfHalfyear(int year, int monthOfYear) {
        return startTimeOfHalfyear(year, monthOfYear)
                .plusMonths(TimeSpec.MonthsPerHalfyear)
                .minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfQuarter(DateTime moment) {
        startTimeOfQuarter(moment.getYear(), moment.getMonthOfYear());
    }

    public static DateTime startTimeOfQuarter(int year, int monthOfYear) {
        QuarterKind quarter = getQuarterOfMonth(monthOfYear);
        return new DateTime(year, getMonthsOfQuarter(quarter)[0], 1, 0, 0);
    }

    public static DateTime endTimeOfQuarter(DateTime moment) {
        return endTimeOfQuarter(moment.getYear(), moment.getMonthOfYear());
    }

    public static DateTime endTimeOfQuarter(int year, int monthOfYear) {
        return startTimeOfQuarter(year, monthOfYear)
                .plusMonths(TimeSpec.MonthsPerQuarter)
                .minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfLastQuarter(DateTime moment) {
        return startTimeOfQuarter(moment.minusMonths(TimeSpec.MonthsPerQuarter));
    }

    public static DateTime endTimeOfLastQuarter(DateTime moment) {
        return endTimeOfQuarter(moment.minusMonths(TimeSpec.MonthsPerQuarter));
    }

    public static DateTime startTimeOfMonth(DateTime moment) {
        return new DateTime(moment.getYear(), moment.getMonthOfYear(), 1, 0, 0);
    }

    public static DateTime startTimeOfMonth(int year, MonthKind month) {
        return new DateTime(year, month.getValue(), 1, 0, 0);
    }

    public static DateTime startTimeOfMonth(int year, int month) {
        return new DateTime(year, month, 1, 0, 0);
    }

    public static DateTime endTimeOfMonth(DateTime moment) {
        return startTimeOfMonth(moment).plusMonths(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime endTimeOfMonth(int year, MonthKind month) {
        return startTimeOfMonth(year, month).plusMonths(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime endTimeOfMonth(int year, int month) {
        return startTimeOfMonth(year, month).plusMonths(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfLastMonth(DateTime moment) {
        return startTimeOfMonth(moment.minusMonths(1));
    }

    public static DateTime endTimeOfLastMonth(DateTime moment) {
        return endTimeOfMonth(moment.minusMonths(1));
    }

    public DateTime startTimeOfWeek(DateTime moment) {
        return startTimeOfWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public DateTime startTimeOfWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return getStartOfWeek(moment, firstDayOfWeek);
    }

    public DateTime endTimeOfWeek(DateTime moment) {
        return endTimeOfWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public DateTime endTimeOfWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return startTimeOfWeek(moment, firstDayOfWeek).plusWeeks(1).minus(TimeSpec.MinPositiveDuration);
    }

    public DateTime startTimeOfLastWeek(DateTime moment) {
        return startTimeOfLastWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public DateTime startTimeOfLastWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return startTimeOfWeek(moment, firstDayOfWeek).minusWeeks(1);
    }

    public DateTime endTimeOfLastWeek(DateTime moment) {
        return endTimeOfLastWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public DateTime endTimeOfLastWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return endTimeOfWeek(moment, firstDayOfWeek).minusWeeks(1);
    }

    public static DateTime startTimeOfDay(DateTime moment) {
        return moment.withTimeAtStartOfDay();
    }

    public static DateTime endTimeOfDay(DateTime moment) {
        return startTimeOfDay(moment).plusDays(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfHour(DateTime moment) {
        return trimToMinute(moment);
    }

    public static DateTime endTimeOfHour(DateTime moment) {
        return startTimeOfHour(moment).plusHours(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfMinute(DateTime moment) {
        return trimToSecond(moment);
    }

    public static DateTime endTimeOfMinute(DateTime moment) {
        return startTimeOfMinute(moment).plusMinutes(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfSecond(DateTime moment) {
        return trimToMillis(moment);
    }

    public static DateTime endTimeOfSecond(DateTime moment) {
        return startTimeOfSecond(moment).plusSeconds(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static HalfyearKind halfyearOf(int monthOfYear) {
        return (monthOfYear < 7) ? HalfyearKind.First : HalfyearKind.Second;
    }

    public static HalfyearKind halfyearOf(DateTime moment) {
        return halfyearOf(moment.getMonthOfYear());
    }

    public static int startMonthOfQuarter(QuarterKind quarter) {
        return (quarter.getValue() - 1) * TimeSpec.MonthsPerQuarter + 1;
    }

    public static int endMonthOfQuarter(QuarterKind quarter) {
        return quarter.getValue() * TimeSpec.MonthsPerQuarter;
    }

    public static QuarterKind quarterOf(int month) {
        return QuarterKind.valueOf((month - 1) / TimeSpec.MonthsPerQuarter + 1);
    }


    // endregion << DateTime >>
}
