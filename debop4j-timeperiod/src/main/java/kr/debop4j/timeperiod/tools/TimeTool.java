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

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import jodd.util.Tuple2;
import kr.debop4j.core.NotSupportException;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.timerange.*;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * {@link DateTime} 관련 Utility Class
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:44
 */
public abstract class TimeTool {

    private static final Logger log = LoggerFactory.getLogger(TimeTool.class);
    @Getter( lazy = true ) private static final boolean traceEnabled = log.isTraceEnabled();
    @Getter( lazy = true ) private static final boolean debugEnabled = log.isDebugEnabled();

    private TimeTool() {}

    public static final String NullString = "<null>";
    public static final DateTime UnixEpoch = new DateTime(1970, 1, 1, 0, 0, 0, 0);

    public static String asString(ITimePeriod period) {
        return (period == null) ? NullString : period.toString();
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
        return getStartOfWeek(time, TimeSpec.FirstDayOfWeek);
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
        return startTimeOfQuarter(moment.getYear(), moment.getMonthOfYear());
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

    public static DateTime startTimeOfWeek(DateTime moment) {
        return startTimeOfWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public static DateTime startTimeOfWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return getStartOfWeek(moment, firstDayOfWeek);
    }

    public static DateTime startTimeOfWeek(int year, int weekOfYear) {
        return startTimeOfWeek(year, weekOfYear, TimeCalendar.getDefault());
    }

    public static DateTime startTimeOfWeek(int year, int weekOfYear, ITimeCalendar timeCalendar) {

        DateTime current = new DateTime(year, 1, 1, 0, 0).minusWeeks(1);
        while (current.getYear() < year + 2) {
            if (current.getWeekyear() == year && current.getWeekOfWeekyear() == weekOfYear)
                break;
            current = current.plusDays(1);
        }
        return current;
    }

    public static DateTime endTimeOfWeek(DateTime moment) {
        return endTimeOfWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public static DateTime endTimeOfWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return startTimeOfWeek(moment, firstDayOfWeek).plusWeeks(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime endTimeOfWeek(int year, int weekOfYear) {
        return endTimeOfWeek(year, weekOfYear, TimeCalendar.getDefault());
    }

    public static DateTime endTimeOfWeek(int year, int weekOfYear, ITimeCalendar timeCalendar) {
        return startTimeOfWeek(year, weekOfYear, timeCalendar).plusWeeks(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime startTimeOfLastWeek(DateTime moment) {
        return startTimeOfLastWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public static DateTime startTimeOfLastWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
        return startTimeOfWeek(moment, firstDayOfWeek).minusWeeks(1);
    }

    public static DateTime endTimeOfLastWeek(DateTime moment) {
        return endTimeOfLastWeek(moment, TimeSpec.FirstDayOfWeek);
    }

    public static DateTime endTimeOfLastWeek(DateTime moment, DayOfWeek firstDayOfWeek) {
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

    public static QuarterKind quarterOf(int monthOfYear) {
        return QuarterKind.valueOf((monthOfYear - 1) / TimeSpec.MonthsPerQuarter + 1);
    }

    public static QuarterKind quarterOf(DateTime moment) {
        return quarterOf(moment.getMonthOfYear());
    }

    public static QuarterKind previousQuarterOf(DateTime moment) {
        return previousQuarter(moment.getYear(), quarterOf(moment)).getQuarter();
    }

    /** 지정한 일자의 다음 주 같은 요일을 반환합니다. */
    public static DateTime nextDayOfWeek(DateTime moment) {
        return nextDayOfWeek(moment, DayOfWeek.valueOf(moment.getDayOfWeek()));
    }

    /** 기준일 이후로 지정한 요일에 해당하는 일자를 반환합니다. */
    public static DateTime nextDayOfWeek(DateTime moment, DayOfWeek dayOfWeek) {
        int dow = dayOfWeek.getValue();
        DateTime next = moment.plusDays(1);

        while (next.getDayOfWeek() != dow) {
            next = next.plusDays(1);
        }
        return next;
    }

    /** 지정한 일자의 전주의 같은 요일을 반환합니다. */
    public static DateTime previousDayOfWeek(DateTime moment) {
        return previousDayOfWeek(moment, DayOfWeek.valueOf(moment.getDayOfWeek()));
    }

    /** 지정한 일자 이전에 지정한 요일에 해당하는 일자를 반환한다. */
    public static DateTime previousDayOfWeek(DateTime moment, DayOfWeek dayOfWeek) {
        int dow = dayOfWeek.getValue();
        DateTime prev = moment.minusDays(1);

        while (prev.getDayOfWeek() != dow) {
            prev = prev.minusDays(1);
        }
        return prev;
    }

    /** 시간부분을 제외한 날짜 부분만 반환한다 */
    public static DateTime getDatePart(DateTime moment) {
        return moment.withTimeAtStartOfDay();
    }

    /** 일자부분이 존재하는지 */
    public static boolean hasDatePart(DateTime moment) {
        return moment.withTimeAtStartOfDay().getMillis() > 0;
    }

    /** 지정한 날짜에 알자(년/월/일) 부분을 지정한 datePart로 설정합니다. */
    public static DateTime setDatePart(DateTime moment, DateTime datepart) {
        return new DatePart(datepart).getDateTime(new TimePart(moment));
    }

    /** 지정한 날짜의 년, 월, 일을 수정합니다. */
    public static DateTime setDatePart(DateTime moment, int year, int month, int day) {
        return setDatePart(moment, new DateTime(year, month, day, 0, 0));
    }

    /** 지정한 일자의 년도만 수정합니다. */
    public static DateTime setYear(DateTime moment, int year) {
        return setDatePart(moment, year, moment.getMonthOfYear(), moment.getDayOfMonth());
    }

    /** 지정한 일자의 월만 수정합니다. */
    public static DateTime setMonth(DateTime moment, int monthOfYear) {
        return setDatePart(moment, moment.getYear(), monthOfYear, moment.getDayOfMonth());
    }

    /** 지정한 일자의 일만 수정합니다. */
    public static DateTime setDay(DateTime moment, int dayOfMonth) {
        return setDatePart(moment, moment.getYear(), moment.getMonthOfYear(), dayOfMonth);
    }

    /** 일자 부분과 시간 부분을 조합합니다. */
    public static DateTime combine(DateTime datepart, DateTime timepart) {
        return setTimePart(datepart, timepart);
    }

    /** 일자의 시간 부분만을 반환합니다. */
    public static Duration getTimePart(DateTime moment) {
        return new Duration(moment.getMillisOfDay());
    }

    public static boolean hasTimePart(DateTime moment) {
        return moment.getMillisOfDay() > TimeSpec.ZeroMillis;
    }

    public static DateTime setTimePart(DateTime moment, DateTime timepart) {
        return setTimePart(moment, timepart.getMillisOfDay());
    }

    public static DateTime setTimePart(DateTime moment, int millis) {
        return moment.withTimeAtStartOfDay().plusMillis(millis);
    }

    public static DateTime setTimePart(DateTime moment, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        return moment.withTimeAtStartOfDay().withTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public static DateTime setHour(DateTime moment, int hourOfDay) {
        return setTimePart(moment, hourOfDay, moment.getMinuteOfHour(), moment.getSecondOfMinute(), moment.getMillisOfSecond());
    }

    public static DateTime setMinute(DateTime moment, int minuteOfHour) {
        return setTimePart(moment, moment.getHourOfDay(), minuteOfHour, moment.getSecondOfMinute(), moment.getMillisOfSecond());
    }

    public static DateTime setSecond(DateTime moment, int secondOfMinute) {
        return setTimePart(moment, moment.getHourOfDay(), moment.getMinuteOfHour(), secondOfMinute, moment.getMillisOfSecond());
    }

    public static DateTime setMillisecond(DateTime moment, int millisOfSecond) {
        return setTimePart(moment, moment.getHourOfDay(), moment.getMinuteOfHour(), moment.getSecondOfMinute(), millisOfSecond);
    }

    /** 정오 */
    public static DateTime noon(DateTime moment) {
        return moment.withTimeAtStartOfDay().plusHours(12);
    }

    /** 지정한 시각에서 지정한 기간 이전의 시각 */
    public static DateTime ago(DateTime moment, Duration duration) {
        return moment.minus(duration);
    }

    /** 지정한 시각에서 지정한 기간 이후의 시각 */
    public static DateTime from(DateTime moment, Duration duration) {
        return moment.plus(duration);
    }

    public static DateTime fromNow(Duration duration) {
        return from(DateTime.now(), duration);
    }

    /** 지정한 시각에서 지정한 기간 이후의 시각 */
    public static DateTime since(DateTime moment, Duration duration) {
        return moment.plus(duration);
    }

    // endregion << DateTime >>

    // region << Math >>

    public static DateTime min(DateTime a, DateTime b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public static DateTime max(DateTime a, DateTime b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static Duration min(Duration a, Duration b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public static Duration max(Duration a, Duration b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static Tuple2<DateTime, DateTime> adjustPeriod(DateTime start, DateTime end) {
        return (start.compareTo(end) < 0)
                ? Tuple2.tuple(start, end)
                : Tuple2.tuple(end, start);
    }

    public static Tuple2<DateTime, Duration> adjustPeriod(DateTime start, Duration duration) {
        return (duration.getMillis() < 0)
                ? Tuple2.tuple(start.plus(duration), new Duration(-duration.getMillis()))
                : Tuple2.tuple(start, duration);
    }

    // endregion

    // region << Period >>

    public static TimeBlock getTimeBlock(DateTime start, Duration duration) {
        return new TimeBlock(start, duration, false);
    }

    public static TimeBlock getTimeBlock(DateTime start, DateTime end) {
        return new TimeBlock(start, end, false);
    }

    public static TimeRange getTimeRange(DateTime start, Duration duration) {
        return new TimeRange(start, duration);
    }

    public static TimeRange getTimeRange(DateTime start, DateTime end) {
        return new TimeRange(start, end);
    }

    /** 시작 시각으로부터 지정한 년도(years) 이후의 기간 */
    public static TimeRange getRelativeYearPeriod(DateTime start, int years) {
        return getTimeRange(start, start.plusYears(years));
    }

    /** 시작 시각으로부터 지정한 개월(months) 이후의 기간 */
    public static TimeRange getRelativeMonthPeriod(DateTime start, int months) {
        return getTimeRange(start, start.plusMonths(months));
    }

    /** 시작 시각으로부터 지정한 주(weeks) 이후의 기간 */
    public static TimeRange getRelativeWeekPeriod(DateTime start, int weeks) {
        return getTimeRange(start, start.plusWeeks(weeks));
    }

    /** 시작 시각으로부터 지정한 일(days) 이후의 기간 */
    public static TimeRange getRelativeDayPeriod(DateTime start, int days) {
        return getTimeRange(start, start.plusDays(days));
    }

    /** 시작 시각으로부터 지정한 시간(hours) 이후의 기간 */
    public static TimeRange getRelativeHourPeriod(DateTime start, int hours) {
        return getTimeRange(start, start.plusHours(hours));
    }

    /** 시작 시각으로부터 지정한 분(minutes) 이후의 기간 */
    public static TimeRange getRelativeMinutePeriod(DateTime start, int minutes) {
        return getTimeRange(start, start.plusMinutes(minutes));
    }

    /** 시작 시각으로부터 지정한 초(seconds) 이후의 기간 */
    public static TimeRange getRelativeSecondPeriod(DateTime start, int seconds) {
        return getTimeRange(start, start.plusSeconds(seconds));
    }

    /**
     * moment가 속한 특정 종류의 기간
     *
     * @param moment
     * @param periodKind
     * @return
     */
    public static ITimePeriod getPeriodOf(DateTime moment, PeriodKind periodKind) {
        return getPeriodOf(moment, periodKind, TimeCalendar.getDefault());
    }

    /** moment가 속한 특정 종류의 기간 */
    public static ITimePeriod getPeriodOf(DateTime moment, PeriodKind periodKind, ITimeCalendar timeCalendar) {
        if (isTraceEnabled())
            log.trace("일자[{}]가 속한 기간 종류[{}]의 기간을 구합니다.", moment, periodKind);

        switch (periodKind) {
            case Year:
                return getYearRange(moment, timeCalendar);
            case Halfyear:
                return getHalfyearRange(moment, timeCalendar);
            case Quarter:
                return getQuarterRange(moment, timeCalendar);
            case Month:
                return getMonthRange(moment, timeCalendar);
            case Week:
                return getWeekRange(moment, timeCalendar);
            case Day:
                return getDayRange(moment, timeCalendar);
            case Hour:
                return getHourRange(moment, timeCalendar);
            case Minute:
                return getMinuteRange(moment, timeCalendar);

            default:
                throw new NotSupportException("지원하지 않는 Period 종류입니다. periodKind=" + periodKind);
        }
    }

    /**
     * moment 가 속한 특정 종류의 기간에 대해 periodCount 갯수만큼의 기간 정보를 컬렉션으로 반환한다.
     *
     * @param moment
     * @param periodKind
     * @param periodCount
     * @return
     */
    public static ICalendarTimeRange getPeriodsOf(DateTime moment, PeriodKind periodKind, int periodCount) {
        return getPeriodsOf(moment, periodKind, periodCount, TimeCalendar.getDefault());
    }

    /**
     * moment 가 속한 특정 종류의 기간에 대해 periodCount 갯수만큼의 기간 정보를 컬렉션으로 반환한다.
     *
     * @param moment
     * @param periodKind
     * @param periodCount
     * @param timeCalendar
     * @return
     */
    public static ICalendarTimeRange getPeriodsOf(DateTime moment, PeriodKind periodKind, int periodCount, ITimeCalendar timeCalendar) {
        if (isTraceEnabled())
            log.trace("일자[{}]가 속한 기간 종류[{}]의 기간을 구합니다.", moment, periodKind);

        switch (periodKind) {
            case Year:
                return getYearRanges(moment, periodCount, timeCalendar);
            case Halfyear:
                return getHalfyearRanges(moment, periodCount, timeCalendar);
            case Quarter:
                return getQuarterRanges(moment, periodCount, timeCalendar);
            case Month:
                return getMonthRanges(moment, periodCount, timeCalendar);
            case Week:
                return getWeekRanges(moment, periodCount, timeCalendar);
            case Day:
                return getDayRanges(moment, periodCount, timeCalendar);
            case Hour:
                return getHourRanges(moment, periodCount, timeCalendar);
            case Minute:
                return getMinuteRanges(moment, periodCount, timeCalendar);

            default:
                throw new NotSupportException("지원하지 않는 Period 종류입니다. periodKind=" + periodKind);
        }
    }


    public static YearRange getYearRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new YearRange(moment, timeCalendar);
    }

    public static YearRangeCollection getYearRanges(DateTime moment, int years, ITimeCalendar timeCalendar) {
        return new YearRangeCollection(moment, years, timeCalendar);
    }

    public static HalfyearRange getHalfyearRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new HalfyearRange(moment, timeCalendar);
    }

    public static HalfyearRangeCollection getHalfyearRanges(DateTime moment, int halfyears, ITimeCalendar timeCalendar) {
        return new HalfyearRangeCollection(moment, halfyears, timeCalendar);
    }

    public static QuarterRange getQuarterRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new QuarterRange(moment, timeCalendar);
    }

    public static QuarterRangeCollection getQuarterRanges(DateTime moment, int quarters, ITimeCalendar timeCalendar) {
        return new QuarterRangeCollection(moment, quarters, timeCalendar);
    }

    public static MonthRange getMonthRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new MonthRange(moment, timeCalendar);
    }

    public static MonthRangeCollection getMonthRanges(DateTime moment, int months, ITimeCalendar timeCalendar) {
        return new MonthRangeCollection(moment, months, timeCalendar);
    }

    public static WeekRange getWeekRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new WeekRange(moment, timeCalendar);
    }

    public static WeekRangeCollection getWeekRanges(DateTime moment, int weeks, ITimeCalendar timeCalendar) {
        return new WeekRangeCollection(moment, weeks, timeCalendar);
    }

    public static DayRange getDayRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new DayRange(moment, timeCalendar);
    }

    public static DayRangeCollection getDayRanges(DateTime moment, int days, ITimeCalendar timeCalendar) {
        return new DayRangeCollection(moment, days, timeCalendar);
    }

    public static HourRange getHourRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new HourRange(moment, timeCalendar);
    }

    public static HourRangeCollection getHourRanges(DateTime moment, int hours, ITimeCalendar timeCalendar) {
        return new HourRangeCollection(moment, hours, timeCalendar);
    }

    public static MinuteRange getMinuteRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new MinuteRange(moment, timeCalendar);
    }

    public static MinuteRangeCollection getMinuteRanges(DateTime moment, int minutes, ITimeCalendar timeCalendar) {
        return new MinuteRangeCollection(moment, minutes, timeCalendar);
    }

    // endregion << Period >>

    // region << Relation >>

    /**
     * 지정된 기간 안에 일자(target)이 있는지 여부
     *
     * @param period
     * @param target
     * @return
     */
    public static boolean hasInside(ITimePeriod period, DateTime target) {
        shouldNotBeNull(period, "period");
        boolean isInside = target.compareTo(period.getStart()) >= 0 && target.compareTo(period.getEnd()) <= 0;

        if (isTraceEnabled()) log.trace("기간 [{}] 안에 target[{}]이 포함되는가? [{}]", isInside);

        return isInside;
    }

    public static boolean hasInside(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");
        boolean isInside = hasInside(period, target.getStart()) && hasInside(period, target.getEnd());

        if (isTraceEnabled()) log.trace("기간 [{}] 안에 target[{}]이 포함되는가? [{}]", isInside);

        return isInside;
    }

    public static boolean hasPureInside(ITimePeriod period, DateTime target) {
        shouldNotBeNull(period, "period");

        boolean isInside = target.compareTo(period.getStart()) > 0 && target.compareTo(period.getEnd()) < 0;

        if (isTraceEnabled()) log.trace("기간 [{}] 안에 target[{}]이 경계를 제외하고, 포함되는가? [{}]", isInside);

        return isInside;
    }

    public static boolean hasPureInside(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        boolean isInside = hasPureInside(period, target.getStart()) && hasPureInside(period, target.getEnd());

        if (isTraceEnabled()) log.trace("기간 [{}] 안에 target[{}]이 경계를 제외하고, 포함되는가? [{}]", isInside);

        return isInside;
    }

    public static boolean isAnytime(ITimePeriod period) {
        return period != null && period.isAnytime();
    }

    public static boolean isNotAnytime(ITimePeriod period) {
        return period != null && !period.isAnytime();
    }

    /**
     * 기준 기간과 대상 기간의 관계를 파악합니다.
     *
     * @param period
     * @param target
     * @return
     */
    public static PeriodRelation getRelation(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        PeriodRelation relation = PeriodRelation.NoRelation;

        if (period.getStart().compareTo(target.getEnd()) > 0) {
            relation = PeriodRelation.After;
        } else if (period.getEnd().compareTo(target.getStart()) < 0) {
            relation = PeriodRelation.Before;
        } else if (period.getStart().equals(period.getStart()) && period.getEnd().equals(period.getEnd())) {
            relation = PeriodRelation.ExactMatch;
        } else if (period.getStart().equals(period.getEnd())) {
            relation = PeriodRelation.StartTouching;
        } else if (period.getEnd().equals(period.getStart())) {
            relation = PeriodRelation.EndTouching;
        } else if (hasInside(period, target)) {
            if (period.getStart().equals(target.getStart()))
                relation = PeriodRelation.EnclosingStartTouching;
            else
                relation = (period.getEnd().equals(target.getEnd()))
                        ? PeriodRelation.EnclosingEndTouching
                        : PeriodRelation.Inside;
        } else {
            boolean insideStart = hasInside(target, period.getStart());
            boolean insideEnd = hasInside(target, period.getEnd());

            if (insideStart && insideEnd) {
                relation = Objects.equal(period.getStart(), target.getStart())
                        ? PeriodRelation.InsideStartTouching
                        : (Objects.equal(period.getEnd(), target.getEnd()) ? PeriodRelation.InsideEndTouching : PeriodRelation.Inside);
            } else if (insideStart) {
                relation = PeriodRelation.StartInside;
            } else if (insideEnd) {
                relation = PeriodRelation.EndInside;
            }
        }

        if (isDebugEnabled())
            log.debug("period [{}], target [{}]의 관계는 [{}] 입니다.", period, target, relation);

        return relation;
    }

    /** 두 기간이 교차하거나, period가 target의 내부 구간이면 true를 반환합니다. */
    public static boolean intersectsWith(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        boolean isIntersected = hasInside(period, target.getStart()) ||
                hasInside(period, target.getEnd()) ||
                hasPureInside(target, period);

        if (isTraceEnabled())
            log.trace("period[{}]와 target[{}]이 교차 구간인가? result=[{}]", period, target, isIntersected);
        return isIntersected;
    }

    /**
     * 두 구간이 겹치는 구간이 있으면 true를 반환합니다.
     *
     * @param period
     * @param target
     * @return
     */
    public static boolean overlapsWith(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        PeriodRelation relation = getRelation(period, target);

        boolean isOverlaps = relation != PeriodRelation.After &&
                relation != PeriodRelation.StartTouching &&
                relation != PeriodRelation.EndTouching &&
                relation != PeriodRelation.Before;

        if (isTraceEnabled())
            log.trace("period[{}]와 target[{}]이 overlap 되는가? [{}]", period, target, isOverlaps);

        return isOverlaps;
    }

    /** 두 기간의 교집합 (교차 구간)을 구합니다. */
    public static TimeBlock getIntersectionBlock(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        TimeBlock intersection = null;
        if (intersectsWith(period, target)) {
            DateTime start = max(period.getStart(), target.getStart());
            DateTime end = min(period.getEnd(), target.getEnd());
            intersection = new TimeBlock(start, end, period.isReadonly());
        }
        if (isTraceEnabled())
            log.trace("period[{}], target[{}]의 교집합 [{}]", period, target, intersection);

        return intersection;
    }

    /** 두 기간의 합집합 구간을 구합니다. */
    public static TimeBlock getUnionBlock(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        DateTime start = min(period.getStart(), target.getStart());
        DateTime end = max(period.getEnd(), target.getEnd());
        TimeBlock union = new TimeBlock(start, end, period.isReadonly());

        if (isTraceEnabled())
            log.trace("period[{}], target[{}]의 합집합 [{}]", period, target, union);

        return union;
    }

    /** 두 기간의 교집합 (교차 구간)을 구합니다. */
    public static TimeRange getIntersectionRange(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        TimeRange intersection = null;
        if (intersectsWith(period, target)) {
            DateTime start = max(period.getStart(), target.getStart());
            DateTime end = min(period.getEnd(), target.getEnd());
            intersection = new TimeRange(start, end, period.isReadonly());
        }
        if (isTraceEnabled())
            log.trace("period[{}], target[{}]의 교집합 [{}]", period, target, intersection);

        return intersection;
    }

    /** 두 기간의 합집합 구간을 구합니다. */
    public static TimeRange getUnionRange(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        DateTime start = min(period.getStart(), target.getStart());
        DateTime end = max(period.getEnd(), target.getEnd());
        TimeRange union = new TimeRange(start, end, period.isReadonly());

        if (isTraceEnabled())
            log.trace("period[{}], target[{}]의 합집합 [{}]", period, target, union);

        return union;
    }

    // endregion << Relation >>

    // region << Trim >>

    public static DateTime trimToYear(DateTime moment) {
        return new DateTime(moment.getYear(), 1, 1, 0, 0);
    }

    public static DateTime trimToMonth(DateTime moment) {
        return trimToMonth(moment, 1);
    }

    public static DateTime trimToMonth(DateTime moment, int monthOfYear) {
        return new DateTime(moment.getYear(), monthOfYear, 1, 0, 0);
    }

    public static DateTime trimToDay(DateTime moment) {
        return trimToDay(moment, 1);
    }

    public static DateTime trimToDay(DateTime moment, int dayOfMonth) {
        return new DateTime(moment.getYear(), moment.getMonthOfYear(), dayOfMonth, 0, 0);
    }

    public static DateTime trimToHour(DateTime moment) {
        return trimToHour(moment, 0);

    }

    public static DateTime trimToHour(DateTime moment, int hourOfDay) {
        return trimToDay(moment).plusHours(hourOfDay);
    }

    public static DateTime trimToMinute(DateTime moment) {
        return trimToMinute(moment, 0);

    }

    public static DateTime trimToMinute(DateTime moment, int minuteOfHour) {
        return trimToHour(moment).plusMinutes(minuteOfHour);
    }

    public static DateTime trimToSecond(DateTime moment) {
        return trimToSecond(moment, 0);

    }

    public static DateTime trimToSecond(DateTime moment, int secondOfMinute) {
        return trimToMinute(moment).plusSeconds(secondOfMinute);
    }

    public static DateTime trimToMillis(DateTime moment) {
        return trimToMillis(moment, 0);

    }

    public static DateTime trimToMillis(DateTime moment, int millisOfSecond) {
        return trimToSecond(moment).plusMillis(millisOfSecond);
    }

    // endregion << Trim >>

    // region << Validation >>

    public static void assertValidPeriod(DateTime start, DateTime end) {
        if (start != null && end != null)
            assert start.compareTo(end) <= 0
                    : String.format("시작시각이 완료시각보다 이전이어야 합니다. start=[%s], end=[%s]", start, end);
    }

    public static void assertMutable(ITimePeriod period) {
        shouldNotBeNull(period, "period");
        assert !period.isReadonly() : "TimePeriod가 읽기전용입니다. period=" + period;
    }

    public static boolean allItemsAreEqual(Iterable<ITimePeriod> left, Iterable<ITimePeriod> right) {
        shouldNotBeNull(left, "left");
        shouldNotBeNull(right, "right");

        if (Iterables.size(left) != Iterables.size(right))
            return false;

        Iterator<ITimePeriod> leftIter = left.iterator();
        Iterator<ITimePeriod> rightIter = right.iterator();
        while (leftIter.hasNext() && rightIter.hasNext()) {
            if (!Objects.equal(leftIter.next(), rightIter.next()))
                return false;
        }
        return true;
    }

    public static boolean isWeekday(DayOfWeek dayOfWeek) {
        return ArrayTool.contains(TimeSpec.Weekdays, dayOfWeek);
    }

    public static boolean isWeekend(DayOfWeek dayOfWeek) {
        return ArrayTool.contains(TimeSpec.Weekends, dayOfWeek);
    }

    // endregion << Validation >>
}
