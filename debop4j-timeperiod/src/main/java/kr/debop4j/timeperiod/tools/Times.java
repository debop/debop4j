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
import com.google.common.collect.Lists;
import kr.debop4j.core.Function1;
import kr.debop4j.core.NotSupportException;
import kr.debop4j.core.Pair;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.timerange.*;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static kr.debop4j.core.Guard.shouldBe;
import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * {@link DateTime} 관련 Utility Class 입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:44
 */
public abstract class Times {

    private static final Logger log = LoggerFactory.getLogger(Times.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private Times() {
    }

    public static final String NullString = "<null>";
    public static final DateTime UnixEpoch = new DateTime(1970, 1, 1, 0, 0, 0, 0);

    /**
     * Now date time.
     *
     * @return the date time
     */
    public static DateTime now() {
        return DateTime.now();
    }

    /**
     * Today date time.
     *
     * @return the date time
     */
    public static DateTime today() {
        return DateTime.now().withTimeAtStartOfDay();
    }

    /**
     * DateValue datepart.
     *
     * @param moment the moment
     * @return the datepart
     */
    public static DateValue datepart(DateTime moment) {
        return new DateValue(moment);
    }

    /**
     * TimeValue timepart.
     *
     * @param moment the moment
     * @return the timepart
     */
    public static TimeValue timepart(DateTime moment) {
        return new TimeValue(moment);
    }

    /**
     * 날짜 부분만 가진 {@link DateTime} 을 만듭니다.
     *
     * @param year        년도
     * @param monthOfYear 월
     * @param dayOfMonth  일
     * @return 년 /월/일을 가진 DateTime
     */
    public static DateTime asDate(int year, int monthOfYear, int dayOfMonth) {
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    /**
     * As string.
     *
     * @param period the period
     * @return the string
     */
    public static String asString(ITimePeriod period) {
        return (period == null) ? NullString : period.toString();
    }

    /**
     * To date time.
     *
     * @param value the value
     * @return the date time
     */
    public static DateTime toDateTime(String value) {
        return toDateTime(value, new DateTime(0));
    }

    /**
     * To date time.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the date time
     */
    public static DateTime toDateTime(String value, DateTime defaultValue) {
        try {
            return DateTime.parse(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * To time period collection.
     *
     * @param sequence the sequence
     * @return the i time period collection
     */
    public static <T extends ITimePeriod> ITimePeriodCollection toTimePeriodCollection(Iterable<T> sequence) {
        return new TimePeriodCollection(sequence);
    }

    // region << Calendar >>

    /**
     * Gets year of.
     *
     * @param moment the moment
     * @return the year of
     */
    public static int getYearOf(DateTime moment) {
        return getYearOf(moment.getYear(), moment.getMonthOfYear());
    }

    /**
     * Gets year of.
     *
     * @param moment   the moment
     * @param calendar the calendar
     * @return the year of
     */
    public static int getYearOf(DateTime moment, ITimeCalendar calendar) {
        return getYearOf(calendar.getYear(moment), calendar.getMonthOfYear(moment));
    }

    /**
     * Gets year of.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @return the year of
     */
    public static int getYearOf(int year, int monthOfYear) {
        return monthOfYear >= 1 ? year : year - 1;
    }

    /**
     * Gets days of year.
     *
     * @param year the year
     * @return the days of year
     */
    public static int getDaysOfYear(int year) {
        return startTimeOfYear(year + 1).minusMillis(1).getDayOfYear();
    }

    /**
     * Next halfyear.
     *
     * @param startYear     the start year
     * @param startHalfyear the start halfyear
     * @return the year and halfyear
     */
    public static YearAndHalfyear nextHalfyear(int startYear, Halfyear startHalfyear) {
        return addHalfyear(startYear, startHalfyear, 1);
    }

    /**
     * Previous halfyear.
     *
     * @param startYear     the start year
     * @param startHalfyear the start halfyear
     * @return the year and halfyear
     */
    public static YearAndHalfyear previousHalfyear(int startYear, Halfyear startHalfyear) {
        return addHalfyear(startYear, startHalfyear, -1);
    }

    /**
     * Add halfyear.
     *
     * @param startYear     the start year
     * @param startHalfyear the start halfyear
     * @param halfyearCount the halfyear count
     * @return the year and halfyear
     */
    public static YearAndHalfyear addHalfyear(int startYear, Halfyear startHalfyear, int halfyearCount) {
        int offsetYear = (Math.abs(halfyearCount) / TimeSpec.HalfyearsPerYear) + 1;
        int startHalfyearCount = ((startYear + offsetYear) * TimeSpec.HalfyearsPerYear) + (startHalfyear.getValue() - 1);
        int targetHalfyearCount = startHalfyearCount + halfyearCount;

        int year = (targetHalfyearCount / TimeSpec.HalfyearsPerYear) - offsetYear;
        Halfyear halfyear = Halfyear.valueOf((targetHalfyearCount % TimeSpec.HalfyearsPerYear) + 1);

        if (isTraceEnabled)
            log.trace("addHalfyear. startYear=[{}], startHalfyear=[{}], halfyearCount=[{}], year=[{}], halfyear=[{}]",
                    startYear, startHalfyear, halfyearCount, year, halfyear);

        return new YearAndHalfyear(year, halfyear);
    }

    /**
     * Gets halfyear of month.
     *
     * @param monthOfYear the month of year
     * @return the halfyear of month
     */
    public static Halfyear getHalfyearOfMonth(int monthOfYear) {
        assert monthOfYear >= 1 && monthOfYear <= 12;

        return (monthOfYear <= TimeSpec.MonthsPerHalfyear)
                ? Halfyear.First
                : Halfyear.Second;
    }

    /**
     * Get months of halfyear.
     *
     * @param halfyear the halfyear
     * @return the int [ ]
     */
    public static int[] getMonthsOfHalfyear(Halfyear halfyear) {
        return (halfyear == Halfyear.First)
                ? TimeSpec.FirstHalfyearMonths
                : TimeSpec.SecondHalfyearMonths;
    }

    /**
     * Next quarter.
     *
     * @param year    the year
     * @param quarter the quarter
     * @return the year and quarter
     */
    public static YearAndQuarter nextQuarter(int year, Quarter quarter) {
        return addQuarter(year, quarter, 1);
    }

    /**
     * Previous quarter.
     *
     * @param year    the year
     * @param quarter the quarter
     * @return the year and quarter
     */
    public static YearAndQuarter previousQuarter(int year, Quarter quarter) {
        return addQuarter(year, quarter, -1);
    }

    /**
     * Add quarter.
     *
     * @param year    the year
     * @param quarter the quarter
     * @param count   the count
     * @return the year and quarter
     */
    public static YearAndQuarter addQuarter(int year, Quarter quarter, int count) {
        int offsetYear = Math.abs(count) / TimeSpec.QuartersPerYear + 1;
        int startQuarters = (year + offsetYear) * TimeSpec.QuartersPerYear + quarter.getValue() - 1;
        int targetQuarters = startQuarters + count;

        int y = targetQuarters / TimeSpec.QuartersPerYear - offsetYear;
        int q = (targetQuarters % TimeSpec.QuartersPerYear) + 1;

        return new YearAndQuarter(y, q);
    }

    /**
     * Gets quarter of month.
     *
     * @param monthOfYear the month of year
     * @return the quarter of month
     */
    public static Quarter getQuarterOfMonth(int monthOfYear) {
        int quarter = (monthOfYear - 1) / TimeSpec.MonthsPerQuarter + 1;
        return Quarter.valueOf(quarter);
    }

    /**
     * Get months of quarter.
     *
     * @param quarter the quarter
     * @return the int [ ]
     */
    public static int[] getMonthsOfQuarter(Quarter quarter) {
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

    /**
     * Next month.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @return the year and month
     */
    public static YearAndMonth nextMonth(int year, int monthOfYear) {
        return addMonth(year, monthOfYear, 1);
    }

    /**
     * Previous month.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @return the year and month
     */
    public static YearAndMonth previousMonth(int year, int monthOfYear) {
        return addMonth(year, monthOfYear, -1);
    }

    /**
     * Add month.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @param count       the count
     * @return the year and month
     */
    public static YearAndMonth addMonth(int year, int monthOfYear, int count) {
        int offset = Math.abs(count) / TimeSpec.MonthsPerYear + 1;
        int startMonths = (year + offset) * TimeSpec.MonthsPerYear + monthOfYear - 1;
        int endMonths = startMonths + count;

        int y = endMonths / TimeSpec.MonthsPerYear - offset;
        int m = (endMonths % TimeSpec.MonthsPerYear) + 1;

        return new YearAndMonth(y, m);
    }

    /**
     * Gets days in month.
     *
     * @param year  the year
     * @param month the month
     * @return the days in month
     */
    public static int getDaysInMonth(int year, int month) {
        DateTime firstDay = new DateTime(year, month, 1, 0, 0);
        return firstDay.plusMonths(1).plusDays(-1).getDayOfMonth();
    }

    /**
     * Gets start of week.
     *
     * @param time the time
     * @return the start of week
     */
    public static DateTime getStartOfWeek(DateTime time) {
        return getStartOfWeek(time, TimeSpec.FirstDayOfWeek);
    }

    /**
     * Gets start of week.
     *
     * @param time           the time
     * @param firstDayOfWeek the first day of week
     * @return the start of week
     */
    public static DateTime getStartOfWeek(DateTime time, DayOfWeek firstDayOfWeek) {
        DateTime current = time.withTimeAtStartOfDay();
        while (current.getDayOfWeek() != firstDayOfWeek.getValue()) {
            current = current.minusDays(1);
        }
        return current;
    }

    /**
     * Gets week of year.
     *
     * @param moment the moment
     * @return the week of year
     */
    public static YearAndWeek getWeekOfYear(DateTime moment) {
        return getWeekOfYear(moment, TimeCalendar.getDefault());
    }

    /**
     * Gets week of year.
     *
     * @param moment       the moment
     * @param timeCalendar the time calendar
     * @return the week of year
     */
    public static YearAndWeek getWeekOfYear(DateTime moment, ITimeCalendar timeCalendar) {
        return new YearAndWeek(moment.getWeekyear(), moment.getWeekOfWeekyear());
    }

    /**
     * Gets weeks of year.
     *
     * @param year the year
     * @return the weeks of year
     */
    public static int getWeeksOfYear(int year) {
        return getWeeksOfYear(year, TimeCalendar.getDefault());
    }

    /**
     * Gets weeks of year.
     *
     * @param year         the year
     * @param timeCalendar the time calendar
     * @return the weeks of year
     */
    public static int getWeeksOfYear(int year, ITimeCalendar timeCalendar) {
        return asDate(year, 12, 28).getWeekOfWeekyear();
    }

    /**
     * Gets start of year week.
     *
     * @param year       the year
     * @param weekOfYear the week of year
     * @return the start of year week
     */
    public static DateTime getStartOfYearWeek(int year, int weekOfYear) {
        return getStartOfYearWeek(year, weekOfYear, null);
    }

    /**
     * Gets start of year week.
     *
     * @param year         the year
     * @param weekOfYear   the week of year
     * @param timeCalendar the time calendar
     * @return the start of year week
     */
    public static DateTime getStartOfYearWeek(int year, int weekOfYear, ITimeCalendar timeCalendar) {
        return new DateTime().withYear(year).withWeekOfWeekyear(weekOfYear);
    }

    /**
     * Day start.
     *
     * @param moment the moment
     * @return the date time
     */
    public static DateTime dayStart(DateTime moment) {
        return moment.withTimeAtStartOfDay();
    }

    /**
     * Next day of week.
     *
     * @param day the day
     * @return the day of week
     */
    public static DayOfWeek nextDayOfWeek(DayOfWeek day) {
        return addDayOfWeek(day, 1);
    }

    /**
     * Previous day of week.
     *
     * @param day the day
     * @return the day of week
     */
    public static DayOfWeek previousDayOfWeek(DayOfWeek day) {
        return addDayOfWeek(day, -1);
    }

    /**
     * Add day of week.
     *
     * @param day  the day
     * @param days the days
     * @return the day of week
     */
    public static DayOfWeek addDayOfWeek(DayOfWeek day, int days) {
        if (days == 0)
            return day;

        int weeks = Math.abs(days) / TimeSpec.DaysPerWeek + 1;
        int offset = weeks * TimeSpec.DaysPerWeek + day.getValue() - 1 + days;
        return DayOfWeek.valueOf((offset % TimeSpec.DaysPerWeek) + 1);
    }

    // endregion << Calendar >>

    // region << Compare >>

    /**
     * 두 일자의 값이 {@link kr.debop4j.timeperiod.PeriodUnit} 단위까지 같은지 비교합니다. (상위값들도 같아야 합니다.)
     *
     * @param left       the left
     * @param right      the right
     * @param periodUnit the period unit
     * @return the boolean
     */
    public static boolean isSameTime(DateTime left, DateTime right, PeriodUnit periodUnit) {
        if (isTraceEnabled)
            log.trace("두 일자가 값은지 비교합니다. left=[{}], right=[{}], periodUnit=[{}]", left, right, periodUnit);

        switch (periodUnit) {
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

    /**
     * 두 일자의 년(Year) 단위까지 같은지 비교합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameYear(DateTime left, DateTime right) {
        return left.getYear() == right.getYear();
    }

    /**
     * 두 일자가 반기(halfyear) 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameHalfyear(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                getHalfyearOfMonth(left.getMonthOfYear()) == getHalfyearOfMonth(right.getMonthOfYear());
    }

    /**
     * 두 일자가 분기(quarter) 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameQuarter(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                getQuarterOfMonth(left.getMonthOfYear()) == getQuarterOfMonth(right.getMonthOfYear());
    }

    /**
     * 두 일자가 월(Month) 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameMonth(DateTime left, DateTime right) {
        return isSameYear(left, right) &&
                left.getMonthOfYear() == right.getMonthOfYear();
    }

    /**
     * 두 일자가 주(Week) 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameWeek(DateTime left, DateTime right) {
        return isSameYear(left, right) && left.getWeekOfWeekyear() == right.getWeekOfWeekyear();
    }

    /**
     * 두 일자가 일(Day) 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameDay(DateTime left, DateTime right) {
        return isSameMonth(left, right) && left.getDayOfMonth() == right.getDayOfMonth();
    }

    /**
     * 두 일자가 시(Hour) 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameHour(DateTime left, DateTime right) {
        return isSameDay(left, right) &&
                left.getHourOfDay() == right.getHourOfDay();
    }

    /**
     * 두 일자가 분 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameMinute(DateTime left, DateTime right) {
        return isSameDay(left, right) &&
                left.getMinuteOfDay() == right.getMinuteOfDay();
    }

    /**
     * 두 일자가 초 단위까지 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameSecond(DateTime left, DateTime right) {
        return isSameDay(left, right) &&
                left.getSecondOfDay() == right.getSecondOfDay();
    }

    /**
     * 두 일자가 같으면 true를 아니면 false를 반환합니다.
     *
     * @param left  the left
     * @param right the right
     * @return the boolean
     */
    public static boolean isSameDateTime(DateTime left, DateTime right) {
        return (left != null) && left.equals(right);
    }

    // endregion << Compare >>

    // region << Current >>

    /**
     * 현재 시각이 속한 년도의 시작일
     */
    public static DateTime currentYear() {
        return asDate(now().getYear(), 1, 1);
    }

    /**
     * 현재 시각이 속한 반기의 시작일
     */
    public static DateTime currentHalfyear() {
        DateTime now = now();
        Halfyear halfyear = getHalfyearOfMonth(now.getMonthOfYear());
        int month = getMonthsOfHalfyear(halfyear)[0];

        return asDate(now.getYear(), month, 1);
    }

    /**
     * 현재 시각이 속한 분기의 시작일
     */
    public static DateTime currentQuarter() {
        DateTime now = now();
        Quarter quarter = getQuarterOfMonth(now.getMonthOfYear());
        int month = getMonthsOfQuarter(quarter)[0];

        return asDate(now.getYear(), month, 1);
    }

    /**
     * 현재 시각이 속한 월의 시작일
     */
    public static DateTime currentMonth() {
        return trimToDay(now());
    }

    /**
     * 현재 시각이 속한 주의 시작일
     */
    public static DateTime currentWeek() {
        return currentWeek(TimeSpec.FirstDayOfWeek);
    }

    /**
     * 현재 시각이 속한 주의 시작일
     */
    public static DateTime currentWeek(DayOfWeek firstDayOfWeek) {
        return getStartOfWeek(now(), firstDayOfWeek);
    }

    /**
     * 오늘
     */
    public static DateTime currentDay() {
        return today();
    }

    /**
     * 현재 시각이 속한 시각의 처음
     */
    public static DateTime currentHour() {
        return trimToMinute(now());
    }

    /**
     * 현재 시각이 속한 분의 처음
     */
    public static DateTime currentMinute() {
        return trimToSecond(now());
    }

    /**
     * 현재 시각이 속한 초단위의 처음
     */
    public static DateTime currentSecond() {
        return trimToMillis(now());
    }

    // endregion << Current >>

    // region << DateTime >>

    /**
     * moment 가 속한 년도의 시작 시각
     */
    public static DateTime startTimeOfYear(DateTime moment) {
        return new DateTime(moment.getYear(), 1, 1, 0, 0);
    }

    /**
     * 지정한 년도의 시작 시각
     */
    public static DateTime startTimeOfYear(int year) {
        return new DateTime(year, 1, 1, 0, 0);
    }

    /**
     * moment가 속한 년도의 마지막 시각
     */
    public static DateTime endTimeOfYear(DateTime moment) {
        return startTimeOfYear(moment.getYear() + 1).plusMillis(-1);
    }

    /**
     * 지정한 년도의 마지막 시각
     */
    public static DateTime endTimeOfYear(int year) {
        return startTimeOfYear(year + 1).plusMillis(-1);
    }

    /**
     * 작년 시작 시각
     */
    public static DateTime startTimeOfLastYear(DateTime moment) {
        return startTimeOfYear(moment.getYear() - 1);
    }

    /**
     * 작년 마지막 시각
     */
    public static DateTime endTimeOfLastYear(DateTime moment) {
        return endTimeOfYear(moment.getYear() - 1);
    }

    /**
     * 해당 일자가 속한 반기의 시작 시각
     *
     * @param moment
     * @return
     */
    public static DateTime startTimeOfHalfyear(DateTime moment) {
        return startTimeOfHalfyear(moment.getYear(), moment.getMonthOfYear());
    }

    /**
     * 해당 년/월이 속한 반기의 시작 시각
     *
     * @param year
     * @param monthOfYear
     * @return
     */
    public static DateTime startTimeOfHalfyear(int year, int monthOfYear) {
        return startTimeOfHalfyear(year, getHalfyearOfMonth(monthOfYear));
    }

    /**
     * 해당 년 / 반기의 시작 시각
     *
     * @param year     year
     * @param halfyear halfyear
     * @return start time of half year
     */
    public static DateTime startTimeOfHalfyear(int year, Halfyear halfyear) {
        return new DateTime(year, getMonthsOfHalfyear(halfyear)[0], 1, 0, 0);
    }

    /**
     * End time of halfyear.
     *
     * @param moment the moment
     * @return the date time
     */
    public static DateTime endTimeOfHalfyear(DateTime moment) {
        return endTimeOfHalfyear(moment.getYear(), moment.getMonthOfYear());
    }

    /**
     * End time of halfyear.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @return the date time
     */
    public static DateTime endTimeOfHalfyear(int year, int monthOfYear) {
        return startTimeOfHalfyear(year, monthOfYear)
                .plusMonths(TimeSpec.MonthsPerHalfyear)
                .minus(TimeSpec.MinPositiveDuration);
    }

    /**
     * Start time of quarter.
     *
     * @param moment the moment
     * @return the date time
     */
    public static DateTime startTimeOfQuarter(DateTime moment) {
        return startTimeOfQuarter(moment.getYear(), moment.getMonthOfYear());
    }

    /**
     * Start time of quarter.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @return the date time
     */
    public static DateTime startTimeOfQuarter(int year, int monthOfYear) {
        return startTimeOfQuarter(year, getQuarterOfMonth(monthOfYear));
    }

    /**
     * Start time of quarter.
     *
     * @param year    the year
     * @param quarter the quarter
     * @return the date time
     */
    public static DateTime startTimeOfQuarter(int year, Quarter quarter) {
        return new DateTime(year, getMonthsOfQuarter(quarter)[0], 1, 0, 0);
    }

    /**
     * End time of quarter.
     *
     * @param moment the moment
     * @return the date time
     */
    public static DateTime endTimeOfQuarter(DateTime moment) {
        return endTimeOfQuarter(moment.getYear(), moment.getMonthOfYear());
    }

    /**
     * End time of quarter.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @return the date time
     */
    public static DateTime endTimeOfQuarter(int year, int monthOfYear) {
        return startTimeOfQuarter(year, monthOfYear)
                .plusMonths(TimeSpec.MonthsPerQuarter)
                .minus(TimeSpec.MinPositiveDuration);
    }

    /**
     * Start time of last quarter.
     *
     * @param moment the moment
     * @return the date time
     */
    public static DateTime startTimeOfLastQuarter(DateTime moment) {
        return startTimeOfQuarter(moment.minusMonths(TimeSpec.MonthsPerQuarter));
    }

    public static DateTime endTimeOfLastQuarter(DateTime moment) {
        return endTimeOfQuarter(moment.minusMonths(TimeSpec.MonthsPerQuarter));
    }

    public static DateTime startTimeOfMonth(DateTime moment) {
        return new DateTime(moment.getYear(), moment.getMonthOfYear(), 1, 0, 0);
    }

    public static DateTime startTimeOfMonth(int year, Month month) {
        return new DateTime(year, month.getValue(), 1, 0, 0);
    }

    public static DateTime startTimeOfMonth(int year, int month) {
        return new DateTime(year, month, 1, 0, 0);
    }

    public static DateTime endTimeOfMonth(DateTime moment) {
        return startTimeOfMonth(moment).plusMonths(1).minus(TimeSpec.MinPositiveDuration);
    }

    public static DateTime endTimeOfMonth(int year, Month month) {
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
        DateTime current = startTimeOfYear(year).minusWeeks(1);
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

    public static Halfyear halfyearOf(int monthOfYear) {
        return (monthOfYear < 7) ? Halfyear.First : Halfyear.Second;
    }

    public static Halfyear halfyearOf(DateTime moment) {
        return halfyearOf(moment.getMonthOfYear());
    }

    public static int startMonthOfQuarter(Quarter quarter) {
        return (quarter.getValue() - 1) * TimeSpec.MonthsPerQuarter + 1;
    }

    public static int endMonthOfQuarter(Quarter quarter) {
        return quarter.getValue() * TimeSpec.MonthsPerQuarter;
    }

    public static Quarter quarterOf(int monthOfYear) {
        return Quarter.valueOf((monthOfYear - 1) / TimeSpec.MonthsPerQuarter + 1);
    }

    public static Quarter quarterOf(DateTime moment) {
        return quarterOf(moment.getMonthOfYear());
    }

    public static Quarter previousQuarterOf(DateTime moment) {
        return previousQuarter(moment.getYear(), quarterOf(moment)).getQuarter();
    }

    /**
     * 지정한 일자의 다음 주 같은 요일을 반환합니다.
     *
     * @param moment 기준 일자
     */
    public static DateTime nextDayOfWeek(DateTime moment) {
        return nextDayOfWeek(moment, DayOfWeek.valueOf(moment.getDayOfWeek()));
    }

    /**
     * 기준일 이후로 지정한 요일에 해당하는 일자를 반환합니다.
     *
     * @param moment    기준 일자
     * @param dayOfWeek 원하는 요일
     */
    public static DateTime nextDayOfWeek(DateTime moment, DayOfWeek dayOfWeek) {
        int dow = dayOfWeek.getValue();
        DateTime next = moment.plusDays(1);

        while (next.getDayOfWeek() != dow) {
            next = next.plusDays(1);
        }
        return next;
    }

    /**
     * 지정한 일자의 전주의 같은 요일을 반환합니다.
     *
     * @param moment 기준 일자
     */
    public static DateTime previousDayOfWeek(DateTime moment) {
        return previousDayOfWeek(moment, DayOfWeek.valueOf(moment.getDayOfWeek()));
    }

    /**
     * 지정한 일자 이전에 지정한 요일에 해당하는 일자를 반환한다.
     *
     * @param moment    기준 일자
     * @param dayOfWeek 원하는 요일
     */
    public static DateTime previousDayOfWeek(DateTime moment, DayOfWeek dayOfWeek) {
        int dow = dayOfWeek.getValue();
        DateTime prev = moment.minusDays(1);

        while (prev.getDayOfWeek() != dow) {
            prev = prev.minusDays(1);
        }
        return prev;
    }

    /**
     * 시간부분을 제외한 날짜 부분만 반환한다
     */
    public static DateTime getDate(DateTime moment) {
        return moment.withTimeAtStartOfDay();
    }

    /**
     * 일자부분이 존재하는지
     */
    public static boolean hasDate(DateTime moment) {
        return moment.withTimeAtStartOfDay().getMillis() > 0;
    }

    /**
     * 지정한 날짜에 알자(년/월/일) 부분을 지정한 datePart로 설정합니다.
     */
    public static DateTime setDate(DateTime moment, DateTime datepart) {
        return new DateValue(datepart).getDateTime(new TimeValue(moment));
    }

    /**
     * 지정한 날짜의 년, 월, 일을 수정합니다.
     */
    public static DateTime setDate(DateTime moment, int year, int month, int day) {
        return setDate(moment, new DateTime(year, month, day, 0, 0));
    }

    /**
     * 지정한 일자의 년도만 수정합니다.
     */
    public static DateTime setYear(DateTime moment, int year) {
        return setDate(moment, year, moment.getMonthOfYear(), moment.getDayOfMonth());
    }

    /**
     * 지정한 일자의 월만 수정합니다.
     */
    public static DateTime setMonth(DateTime moment, int monthOfYear) {
        return setDate(moment, moment.getYear(), monthOfYear, moment.getDayOfMonth());
    }

    /**
     * 지정한 일자의 일만 수정합니다.
     */
    public static DateTime setDay(DateTime moment, int dayOfMonth) {
        return setDate(moment, moment.getYear(), moment.getMonthOfYear(), dayOfMonth);
    }

    /**
     * 일자 부분과 시간 부분을 조합합니다.
     */
    public static DateTime combine(DateTime datepart, DateTime timepart) {
        return setTime(datepart, timepart);
    }

    /**
     * 일자의 시간 부분만을 반환합니다.
     */
    public static Duration getTime(DateTime moment) {
        return new Duration(moment.getMillisOfDay());
    }

    /**
     * 시각에 시간부분의 값이 존재하는지 여부
     */
    public static boolean hasTime(DateTime moment) {
        return moment.getMillisOfDay() > 0;
    }

    /**
     * 해당 일자의 시간 부분을 지정한 값으로 설정합니다.
     *
     * @param moment   기준 일자
     * @param timepart 지정할 시간 부분
     * @return 계산한 일자
     */
    public static DateTime setTime(DateTime moment, DateTime timepart) {
        return setTime(moment, timepart.getMillisOfDay());
    }

    public static DateTime setTime(DateTime moment, int millis) {
        return moment.withTimeAtStartOfDay().plusMillis(millis);
    }

    public static DateTime setTime(DateTime moment, int hourOfDay, int minuteOfHour) {
        return moment.withTimeAtStartOfDay().withTime(hourOfDay, minuteOfHour, 0, 0);
    }

    public static DateTime setTime(DateTime moment, int hourOfDay, int minuteOfHour, int secondOfMinute) {
        return moment.withTimeAtStartOfDay().withTime(hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    public static DateTime setTime(DateTime moment, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        return moment.withTimeAtStartOfDay().withTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public static DateTime setHour(DateTime moment, int hourOfDay) {
        return setTime(moment, hourOfDay, moment.getMinuteOfHour(), moment.getSecondOfMinute(), moment.getMillisOfSecond());
    }

    public static DateTime setMinute(DateTime moment, int minuteOfHour) {
        return setTime(moment, moment.getHourOfDay(), minuteOfHour, moment.getSecondOfMinute(), moment.getMillisOfSecond());
    }

    public static DateTime setSecond(DateTime moment, int secondOfMinute) {
        return setTime(moment, moment.getHourOfDay(), moment.getMinuteOfHour(), secondOfMinute, moment.getMillisOfSecond());
    }

    public static DateTime setMillisecond(DateTime moment, int millisOfSecond) {
        return setTime(moment, moment.getHourOfDay(), moment.getMinuteOfHour(), moment.getSecondOfMinute(), millisOfSecond);
    }

    /**
     * 정오
     */
    public static DateTime noon(DateTime moment) {
        return moment.withTimeAtStartOfDay().plusHours(12);
    }

    /**
     * 지정한 시각에서 지정한 기간 이전의 시각
     */
    public static DateTime ago(DateTime moment, Duration duration) {
        return moment.minus(duration);
    }

    /**
     * 지정한 시각에서 지정한 기간 이후의 시각
     */
    public static DateTime from(DateTime moment, Duration duration) {
        return moment.plus(duration);
    }

    public static DateTime fromNow(Duration duration) {
        return from(DateTime.now(), duration);
    }

    /**
     * 지정한 시각에서 지정한 기간 이후의 시각
     */
    public static DateTime since(DateTime moment, Duration duration) {
        return moment.plus(duration);
    }

    // endregion << DateTime >>

    // region << Math >>

    public static DateTime min(DateTime a, DateTime b) {
        if (a == null && b == null) return null;
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) < 0 ? a : b;
    }

    public static DateTime max(DateTime a, DateTime b) {
        if (a == null && b == null) return null;
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) > 0 ? a : b;
    }

    public static Duration min(Duration a, Duration b) {
        if (a == null && b == null) return null;
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) < 0 ? a : b;
    }

    public static Duration max(Duration a, Duration b) {
        if (a == null && b == null) return null;
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) > 0 ? a : b;
    }

    public static Pair<DateTime, DateTime> adjustPeriod(DateTime start, DateTime end) {
        return Pair.create(min(start, end), max(start, end));
    }

    public static Pair<DateTime, Duration> adjustPeriod(DateTime start, Duration duration) {
        shouldNotBeNull(start, "start");
        shouldNotBeNull(duration, "duration");
        return (duration.getMillis() < 0)
                ? Pair.create(start.plus(duration), new Duration(-duration.getMillis()))
                : Pair.create(start, duration);
    }

    // endregion << Math >>

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

    /**
     * 시작 시각으로부터 지정한 년도(years) 이후의 기간
     */
    public static TimeRange getRelativeYearPeriod(DateTime start, int years) {
        return getTimeRange(start, start.plusYears(years));
    }

    /**
     * 시작 시각으로부터 지정한 개월(months) 이후의 기간
     */
    public static TimeRange getRelativeMonthPeriod(DateTime start, int months) {
        return getTimeRange(start, start.plusMonths(months));
    }

    /**
     * 시작 시각으로부터 지정한 주(weeks) 이후의 기간
     */
    public static TimeRange getRelativeWeekPeriod(DateTime start, int weeks) {
        return getTimeRange(start, start.plusWeeks(weeks));
    }

    /**
     * 시작 시각으로부터 지정한 일(days) 이후의 기간
     */
    public static TimeRange getRelativeDayPeriod(DateTime start, int days) {
        return getTimeRange(start, start.plusDays(days));
    }

    /**
     * 시작 시각으로부터 지정한 시간(hours) 이후의 기간
     */
    public static TimeRange getRelativeHourPeriod(DateTime start, int hours) {
        return getTimeRange(start, start.plusHours(hours));
    }

    /**
     * 시작 시각으로부터 지정한 분(minutes) 이후의 기간
     */
    public static TimeRange getRelativeMinutePeriod(DateTime start, int minutes) {
        return getTimeRange(start, start.plusMinutes(minutes));
    }

    /**
     * 시작 시각으로부터 지정한 초(seconds) 이후의 기간
     */
    public static TimeRange getRelativeSecondPeriod(DateTime start, int seconds) {
        return getTimeRange(start, start.plusSeconds(seconds));
    }

    /**
     * moment가 속한 특정 종류의 기간
     */
    public static ITimePeriod getPeriodOf(DateTime moment, PeriodUnit periodUnit) {
        return getPeriodOf(moment, periodUnit, TimeCalendar.getDefault());
    }

    /**
     * moment가 속한 특정 종류의 기간
     */
    public static ITimePeriod getPeriodOf(DateTime moment, PeriodUnit periodUnit, ITimeCalendar timeCalendar) {
        if (isTraceEnabled)
            log.trace("일자[{}]가 속한 기간 종류[{}]의 기간을 구합니다.", moment, periodUnit);

        if (timeCalendar == null)
            timeCalendar = TimeCalendar.getDefault();

        switch (periodUnit) {
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
            case Second:
                return new TimeRange(trimToMillis(moment), Durations.Second);

            default:
                throw new NotSupportException("지원하지 않는 Period 종류입니다. periodUnit=" + periodUnit);
        }
    }

    /**
     * moment 가 속한 특정 종류의 기간에 대해 periodCount 갯수만큼의 기간 정보를 컬렉션으로 반환한다.
     */
    public static ICalendarTimeRange getPeriodsOf(DateTime moment, PeriodUnit periodUnit, int periodCount) {
        return getPeriodsOf(moment, periodUnit, periodCount, TimeCalendar.getDefault());
    }

    /**
     * moment 가 속한 특정 종류의 기간에 대해 periodCount 갯수만큼의 기간 정보를 컬렉션으로 반환한다.
     */
    public static ICalendarTimeRange getPeriodsOf(DateTime moment, PeriodUnit periodUnit, int periodCount, ITimeCalendar timeCalendar) {
        if (isTraceEnabled)
            log.trace("일자[{}]가 속한 기간 종류[{}]의 기간을 구합니다.", moment, periodUnit);

        if (timeCalendar == null)
            timeCalendar = TimeCalendar.getDefault();

        switch (periodUnit) {
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
            case Second:
                return new CalendarTimeRange(trimToMillis(moment), trimToMillis(moment).plusSeconds(periodCount), timeCalendar);

            default:
                throw new NotSupportException("지원하지 않는 Period 종류입니다. periodUnit=" + periodUnit);
        }
    }


    public static YearRange getYearRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new YearRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static YearRangeCollection getYearRanges(DateTime moment, int years, ITimeCalendar timeCalendar) {
        return new YearRangeCollection(moment, years, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static HalfyearRange getHalfyearRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new HalfyearRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static HalfyearRangeCollection getHalfyearRanges(DateTime moment, int halfyears, ITimeCalendar timeCalendar) {
        return new HalfyearRangeCollection(moment, halfyears, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static QuarterRange getQuarterRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new QuarterRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static QuarterRangeCollection getQuarterRanges(DateTime moment, int quarters, ITimeCalendar timeCalendar) {
        return new QuarterRangeCollection(moment, quarters, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static MonthRange getMonthRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new MonthRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static MonthRangeCollection getMonthRanges(DateTime moment, int months, ITimeCalendar timeCalendar) {
        return new MonthRangeCollection(moment, months, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static WeekRange getWeekRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new WeekRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static WeekRangeCollection getWeekRanges(DateTime moment, int weeks, ITimeCalendar timeCalendar) {
        return new WeekRangeCollection(moment, weeks, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static DayRange getDayRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new DayRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static DayRangeCollection getDayRanges(DateTime moment, int days, ITimeCalendar timeCalendar) {
        return new DayRangeCollection(moment, days, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static HourRange getHourRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new HourRange(moment, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static HourRangeCollection getHourRanges(DateTime moment, int hours, ITimeCalendar timeCalendar) {
        return new HourRangeCollection(moment, hours, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    public static MinuteRange getMinuteRange(DateTime moment, ITimeCalendar timeCalendar) {
        return new MinuteRange(moment, timeCalendar);
    }

    public static MinuteRangeCollection getMinuteRanges(DateTime moment, int minutes, ITimeCalendar timeCalendar) {
        return new MinuteRangeCollection(moment, minutes, (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault());
    }

    // endregion << Period >>

    // region << Relation >>

    /**
     * 지정된 기간 안에 일자(target)이 있는지 여부
     */
    public static boolean hasInside(ITimePeriod period, DateTime target) {
        shouldNotBeNull(period, "period");
        boolean isInside = target.compareTo(period.getStart()) >= 0 && target.compareTo(period.getEnd()) <= 0;

        if (isTraceEnabled)
            log.trace("기간 [{}] 안에 target[{}]이 포함되는가? [{}]", period, target, isInside);

        return isInside;
    }

    /**
     * 지정된 기간 안에 대상 기간이 있는지 여부
     */
    public static boolean hasInside(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");
        boolean isInside = hasInside(period, target.getStart()) && hasInside(period, target.getEnd());

        if (isTraceEnabled)
            log.trace("기간 [{}] 안에 target[{}]이 포함되는가? [{}]", period, target, isInside);

        return isInside;
    }

    public static boolean hasPureInside(ITimePeriod period, DateTime target) {
        shouldNotBeNull(period, "period");

        boolean isInside = target.compareTo(period.getStart()) > 0 && target.compareTo(period.getEnd()) < 0;

        if (isTraceEnabled)
            log.trace("기간 [{}] 안에 target[{}]이 경계를 제외하고, 포함되는가? [{}]", period, target, isInside);

        return isInside;
    }

    public static boolean hasPureInside(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        boolean isInside = hasPureInside(period, target.getStart()) && hasPureInside(period, target.getEnd());

        if (isTraceEnabled)
            log.trace("기간 [{}] 안에 target[{}]이 경계를 제외하고, 포함되는가? [{}]", period, target, isInside);

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
     */
    public static PeriodRelation getRelation(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        PeriodRelation relation = PeriodRelation.NoRelation;

        if (period.getStart().compareTo(target.getEnd()) > 0) {
            relation = PeriodRelation.After;
        } else if (period.getEnd().compareTo(target.getStart()) < 0) {
            relation = PeriodRelation.Before;
        } else if (period.getStart().equals(target.getStart()) && period.getEnd().equals(target.getEnd())) {
            relation = PeriodRelation.ExactMatch;
        } else if (period.getStart().equals(target.getEnd())) {
            relation = PeriodRelation.StartTouching;
        } else if (period.getEnd().equals(target.getStart())) {
            relation = PeriodRelation.EndTouching;
        } else if (hasInside(period, target)) {
            if (Objects.equal(period.getStart(), target.getStart()))
                relation = PeriodRelation.EnclosingStartTouching;
            else
                relation = Objects.equal(period.getEnd(), target.getEnd())
                        ? PeriodRelation.EnclosingEndTouching
                        : PeriodRelation.Enclosing;
        } else {
            // 기간이 대상 내부에 속할 때
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

        if (isDebugEnabled)
            log.debug("period [{}], target [{}]의 관계는 [{}]입니다.", period, target, relation);

        return relation;
    }

    /**
     * 두 기간이 교차하거나, period가 target의 내부 구간이면 true를 반환합니다.
     */
    public static boolean intersectsWith(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        boolean isIntersected = hasInside(period, target.getStart()) ||
                hasInside(period, target.getEnd()) ||
                hasPureInside(target, period);

        if (isTraceEnabled)
            log.trace("period[{}]와 target[{}]이 교차 구간인가? result=[{}]", period, target, isIntersected);
        return isIntersected;
    }

    /**
     * 두 구간이 겹치는 구간이 있으면 true를 반환합니다.
     *
     * @param period 기준 기간
     * @param target 대상 기간
     * @return 겹치는지 여부
     */
    public static boolean overlapsWith(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        PeriodRelation relation = getRelation(period, target);

        boolean isOverlaps = relation != PeriodRelation.After &&
                relation != PeriodRelation.StartTouching &&
                relation != PeriodRelation.EndTouching &&
                relation != PeriodRelation.Before;

        if (isTraceEnabled)
            log.trace("period[{}]와 target[{}]이 overlap 되는가? [{}]", period, target, isOverlaps);

        return isOverlaps;
    }

    /**
     * 두 기간의 교집합 (교차 구간)을 구합니다.
     */
    public static TimeBlock getIntersectionBlock(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        TimeBlock intersection = null;
        if (intersectsWith(period, target)) {
            DateTime start = max(period.getStart(), target.getStart());
            DateTime end = min(period.getEnd(), target.getEnd());
            intersection = new TimeBlock(start, end, period.isReadonly());
        }
        if (isTraceEnabled)
            log.trace("period[{}], target[{}]의 교집합 [{}]", period, target, intersection);

        return intersection;
    }

    /**
     * 두 기간의 합집합 구간을 구합니다.
     */
    public static TimeBlock getUnionBlock(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        DateTime start = min(period.getStart(), target.getStart());
        DateTime end = max(period.getEnd(), target.getEnd());
        TimeBlock union = new TimeBlock(start, end, period.isReadonly());

        if (isTraceEnabled)
            log.trace("period[{}], target[{}]의 합집합 [{}]", period, target, union);

        return union;
    }

    /**
     * 두 기간의 교집합 (교차 구간)을 구합니다.
     */
    public static TimeRange getIntersectionRange(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        TimeRange intersection = null;
        if (intersectsWith(period, target)) {
            DateTime start = max(period.getStart(), target.getStart());
            DateTime end = min(period.getEnd(), target.getEnd());
            intersection = new TimeRange(start, end, period.isReadonly());
        }
        if (isTraceEnabled)
            log.trace("period[{}], target[{}]의 교집합 [{}]", period, target, intersection);

        return intersection;
    }

    /**
     * 두 기간의 합집합 구간을 구합니다.
     */
    public static TimeRange getUnionRange(ITimePeriod period, ITimePeriod target) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(target, "target");

        DateTime start = min(period.getStart(), target.getStart());
        DateTime end = max(period.getEnd(), target.getEnd());
        TimeRange union = new TimeRange(start, end, period.isReadonly());

        if (isTraceEnabled)
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
        return trimToMinute(moment, 0).withHourOfDay(hourOfDay);
    }

    public static DateTime trimToMinute(DateTime moment) {
        return trimToMinute(moment, 0);

    }

    public static DateTime trimToMinute(DateTime moment, int minuteOfHour) {
        return trimToSecond(moment, 0).withMinuteOfHour(minuteOfHour);
    }

    public static DateTime trimToSecond(DateTime moment) {
        return trimToSecond(moment, 0);

    }

    public static DateTime trimToSecond(DateTime moment, int secondOfMinute) {
        return trimToMillis(moment, 0).withSecondOfMinute(secondOfMinute);
    }

    public static DateTime trimToMillis(DateTime moment) {
        return trimToMillis(moment, 0);

    }

    public static DateTime trimToMillis(DateTime moment, int millisOfSecond) {
        return moment.withMillisOfSecond(millisOfSecond);
        //return moment.withTimeAtStartOfDay().plusSeconds(moment.getSecondOfDay()).plusMillis(millisOfSecond);
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

    // region << Comparator >>

    @Getter(lazy = true)
    private static final StartComparator startComparator = new StartComparator();
    @Getter(lazy = true)
    private static final StartDescComparator startDescComparator = new StartDescComparator();
    @Getter(lazy = true)
    private static final EndComparator endComparator = new EndComparator();
    @Getter(lazy = true)
    private static final EndDescComparator endDescComparator = new EndDescComparator();
    @Getter(lazy = true)
    private static final DurationComparator durationComparator = new DurationComparator();
    @Getter(lazy = true)
    private static final DurationDescComparator durationDescComparator = new DurationDescComparator();

    /**
     * The type Start comparator.
     */
    public static class StartComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o1.getStart().compareTo(o2.getStart());
        }
    }

    /**
     * The type Start desc comparator.
     */
    public static class StartDescComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o2.getStart().compareTo(o1.getStart());
        }
    }

    /**
     * The type End comparator.
     */
    public static class EndComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o1.getEnd().compareTo(o2.getEnd());
        }
    }

    /**
     * The type End desc comparator.
     */
    public static class EndDescComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o2.getEnd().compareTo(o1.getEnd());
        }
    }

    /**
     * The type Duration comparator.
     */
    public static class DurationComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o1.getDuration().compareTo(o2.getDuration());
        }
    }

    /**
     * The type Duration desc comparator.
     */
    public static class DurationDescComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o2.getDuration().compareTo(o1.getDuration());
        }
    }

    // endregion

    // region << ForEach >>

    /**
     * 지정된 기간을 기간 단위별로 세분하여 컬렉션을 빌드합니다.
     */
    public static List<ITimePeriod> foreachPeriods(ITimePeriod period, PeriodUnit periodUnit) {
        switch (periodUnit) {
            case Year:
                return foreachYears(period);

            case Halfyear:
                return foreachHalfyears(period);

            case Quarter:
                return foreachQuarters(period);

            case Month:
                return foreachMonths(period);

            case Week:
                return foreachWeeks(period);

            case Day:
                return foreachDays(period);

            case Hour:
                return foreachHours(period);

            case Minute:
                return foreachMinutes(period);

            default:
                throw new IllegalArgumentException("지원하지 않는 PeriodKind입니다. PeriodUnit=" + periodUnit);
        }
    }

    /**
     * 지정된 기간을 년단위로 컬렉션을 만듭니다.
     */
    public static List<ITimePeriod> foreachYears(ITimePeriod period) {
        assert period != null;
        if (isTraceEnabled)
            log.trace("기간[{}]에 대해 Year 단위로 열거합니다...", period);

        List<ITimePeriod> years = Lists.newArrayList();
        if (period.isAnytime())
            return years;
        assertHasPeriod(period);

        if (Times.isSameYear(period.getStart(), period.getEnd())) {
            years.add(new TimeRange(period));
            return years;
        }

        years.add(new TimeRange(period.getStart(), Times.endTimeOfYear(period.getStart())));

        DateTime current = Times.startTimeOfYear(period.getStart()).plusYears(1);
        int endYear = period.getEnd().getYear();
        ITimeCalendar calendar = TimeCalendar.getDefault();

        while (current.getYear() < endYear) {
            years.add(Times.getYearRange(current, calendar));
            current = current.plusYears(1);
        }

        if (current.compareTo(period.getEnd()) < 0) {
            years.add(new TimeRange(Times.startTimeOfYear(current), period.getEnd()));
        }

        return years;
    }

    /**
     * 지정된 기간을 반기 단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachHalfyears(ITimePeriod period) {
        assert period != null;
        if (isTraceEnabled)
            log.trace("기간[{}]에 대해 Halfyear 단위로 열거합니다...", period);

        List<ITimePeriod> halfyears = Lists.newArrayList();
        if (period.isAnytime())
            return halfyears;

        assertHasPeriod(period);

        if (Times.isSameHalfyear(period.getStart(), period.getEnd())) {
            halfyears.add(new TimeRange(period));
            return halfyears;
        }

        DateTime current = Times.endTimeOfHalfyear(period.getStart());
        halfyears.add(new TimeRange(period.getStart(), current));

        int endHashCode = period.getEnd().getYear() * 10 + Times.halfyearOf(period.getEnd()).getValue();
        current = current.plusDays(1);
        ITimeCalendar calendar = TimeCalendar.getDefault();
        while (current.getYear() * 10 + Times.halfyearOf(current).getValue() < endHashCode) {
            halfyears.add(Times.getHalfyearRange(current, calendar));
            current = current.plusMonths(TimeSpec.MonthsPerHalfyear);
        }

        if (current.compareTo(period.getEnd()) < 0) {
            halfyears.add(new TimeRange(Times.startTimeOfHalfyear(current), period.getEnd()));
        }

        return halfyears;
    }

    /**
     * 지정된 기간을 분기단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachQuarters(ITimePeriod period) {
        assert period != null;
        if (isTraceEnabled)
            log.trace("기간[{}]에 대해 Quarter 단위로 열거합니다...", period);

        List<ITimePeriod> quarters = Lists.newArrayList();
        if (period.isAnytime())
            return quarters;

        assertHasPeriod(period);

        if (Times.isSameQuarter(period.getStart(), period.getEnd())) {
            quarters.add(new TimeRange(period));
            return quarters;
        }

        DateTime current = Times.endTimeOfQuarter(period.getStart());
        quarters.add(new TimeRange(period.getStart(), current));

        int endHashCode = period.getEnd().getYear() * 10 + Times.quarterOf(period.getEnd()).getValue();
        current = current.plusDays(1);
        ITimeCalendar calendar = TimeCalendar.getDefault();

        while (current.getYear() * 10 + Times.quarterOf(current).getValue() < endHashCode) {
            quarters.add(Times.getQuarterRange(current, calendar));
            current = current.plusMonths(TimeSpec.MonthsPerQuarter);
        }

        if (current.compareTo(period.getEnd()) < 0)
            quarters.add(new TimeRange(Times.startTimeOfQuarter(current), period.getEnd()));

        return quarters;
    }

    /**
     * 지정된 기간을 월(Month) 단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachMonths(ITimePeriod period) {
        assert period != null;
        if (isTraceEnabled)
            log.trace("기간[{}]에 대해 월(Month) 단위로 열거합니다...", period);

        List<ITimePeriod> months = Lists.newArrayList();
        if (period.isAnytime())
            return months;

        assertHasPeriod(period);

        if (Times.isSameMonth(period.getStart(), period.getEnd())) {
            months.add(new TimeRange(period));
            return months;
        }

        DateTime current = Times.endTimeOfMonth(period.getStart());
        months.add(new TimeRange(period.getStart(), current));

        DateTime monthEnd = Times.startTimeOfMonth(period.getEnd());
        current = current.plusDays(1);
        ITimeCalendar calendar = TimeCalendar.getDefault();

        while (current.compareTo(monthEnd) < 0) {
            months.add(Times.getMonthRange(current, calendar));
            current = current.plusMonths(1);
        }

        current = Times.startTimeOfMonth(current);
        if (current.compareTo(period.getEnd()) < 0)
            months.add(new TimeRange(current, period.getEnd()));

        return months;
    }

    /**
     * 지정된 기간을 주(Week) 단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachWeeks(ITimePeriod period) {
        assert period != null;
        if (isTraceEnabled)
            log.trace("기간[{}]에 대해 주(Week) 단위로 열거합니다...", period);

        List<ITimePeriod> weeks = Lists.newArrayList();
        if (period.isAnytime())
            return weeks;

        assertHasPeriod(period);

        if (Times.isSameWeek(period.getStart(), period.getEnd())) {
            weeks.add(new TimeRange(period));
            return weeks;
        }

        DateTime current = period.getStart();
        DateTime weekEnd = Times.endTimeOfWeek(current);
        if (weekEnd.compareTo(period.getEnd()) >= 0) {
            weeks.add(new TimeRange(current, period.getEnd()));
            return weeks;
        }

        weeks.add(new TimeRange(current, weekEnd));
        current = weekEnd.plusWeeks(1);
        ITimeCalendar calendar = TimeCalendar.getDefault();

        while (current.compareTo(period.getEnd()) < 0) {
            weeks.add(Times.getWeekRange(current, calendar));
            current = current.plusWeeks(1);
        }

        current = Times.startTimeOfWeek(current);
        if (current.compareTo(period.getEnd()) < 0) {
            weeks.add(new TimeRange(current, period.getEnd()));
        }
        return weeks;
    }

    /**
     * 지정한 기간을 일(Day)단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachDays(ITimePeriod period) {
        shouldNotBeNull(period, "period");
        if (isTraceEnabled) log.trace("기간[{}]에 대해 일(Day) 단위로 열거합니다...", period);

        List<ITimePeriod> days = Lists.newArrayList();
        if (period.isAnytime())
            return days;

        assertHasPeriod(period);

        if (Times.isSameDay(period.getStart(), period.getEnd())) {
            days.add(new TimeRange(period));
            return days;
        }

        days.add(new TimeRange(period.getStart(), Times.endTimeOfDay(period.getStart())));

        DateTime endDay = period.getEnd().withTimeAtStartOfDay();
        DateTime current = period.getStart().withTimeAtStartOfDay().plusDays(1);
        ITimeCalendar calendar = TimeCalendar.getDefault();

        while (current.compareTo(endDay) < 0) {
            days.add(Times.getDayRange(current, calendar));
            current = current.plusDays(1);
        }
        if (period.getEnd().getMillisOfDay() > 0)
            days.add(new TimeRange(endDay.withTimeAtStartOfDay(), period.getEnd()));

        return days;
    }

    /**
     * 지정한 기간을 시(Hour) 단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachHours(ITimePeriod period) {
        shouldNotBeNull(period, "period");

        if (isTraceEnabled) log.trace("기간[{}]에 대해 시간(Hour) 단위로 열거합니다...", period);

        List<ITimePeriod> hours = Lists.newArrayList();
        if (period.isAnytime())
            return hours;

        assertHasPeriod(period);

        if (Times.isSameHour(period.getStart(), period.getEnd())) {
            hours.add(new TimeRange(period));
            return hours;
        }

        hours.add(new TimeRange(period.getStart(), Times.endTimeOfHour(period.getStart())));

        DateTime endHour = period.getEnd();
        DateTime current = Times.trimToHour(period.getStart(), period.getStart().getHourOfDay() + 1);
        ITimeCalendar calendar = TimeCalendar.getDefault();

        DateTime maxHour = endHour.minusHours(1);
        while (current.compareTo(maxHour) <= 0) {
            hours.add(Times.getHourRange(current, calendar));
            current = current.plusHours(1);
        }

        if (endHour.minusHours(endHour.getHourOfDay()).getMillisOfDay() > 0) {
            hours.add(new TimeRange(Times.startTimeOfHour(endHour), endHour));
        }

        return hours;
    }

    /**
     * 지정한 기간을 분(Minute) 단위로 열거합니다.
     */
    public static List<ITimePeriod> foreachMinutes(ITimePeriod period) {
        shouldNotBeNull(period, "period");
        if (isTraceEnabled) log.trace("기간[{}]에 대해 분(Minute) 단위로 열거합니다...", period);

        List<ITimePeriod> minutes = Lists.newArrayList();
        if (period.isAnytime())
            return minutes;

        assertHasPeriod(period);

        if (Times.isSameMinute(period.getStart(), period.getEnd())) {
            minutes.add(new TimeRange(period));
            return minutes;
        }

        minutes.add(new TimeRange(period.getStart(), Times.endTimeOfMinute(period.getStart())));

        DateTime endMinute = period.getEnd();
        DateTime current = Times.trimToMinute(period.getStart(), period.getStart().getMinuteOfHour() + 1);
        ITimeCalendar calendar = TimeCalendar.getDefault();

        DateTime maxMinute = endMinute.minusMinutes(1);
        while (current.compareTo(maxMinute) <= 0) {
            minutes.add(Times.getMinuteRange(current, calendar));
            current = current.plusMinutes(1);
        }

        if (endMinute.minusMinutes(endMinute.getMinuteOfHour()).getMillisOfDay() > 0) {
            minutes.add(new TimeRange(Times.startTimeOfMinute(endMinute), endMinute));
        }

        return minutes;
    }

    private static void assertHasPeriod(ITimePeriod period) {
        assert period != null && period.hasPeriod() : "기간이 설정되지 않았습니다. period=" + period;
    }

    /**
     * 기간을 특정 단위로 열거한 값을 이용하여 특정 코드를 수행하여 결과값을 반환합니다.
     */
    public static <T> List<T> runPeriods(ITimePeriod period, PeriodUnit periodUnit, Function1<ITimePeriod, T> runner) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(runner, "runner");
        shouldBe(period.hasPeriod(), "period는 기간을 가져야합니다. period=%s", period);

        if (isDebugEnabled)
            log.debug("기간[{}]을 [{}] 단위로 열거하여, 메소드르 실행시켜 결과를 반환합니다.", period, periodUnit);

        List<T> results = Lists.newArrayList();
        for (ITimePeriod item : foreachPeriods(period, periodUnit)) {
            results.add(runner.execute(item));
        }

        return results;
    }

    /**
     * 기간을 특정 단위로 열거한 값을 이용하여 특정 코드를 병렬로 수행하여 결과값을 반환합니다.
     */
    public static <T> List<T> runPeriodsAsParallel(ITimePeriod period, PeriodUnit periodUnit, Function1<ITimePeriod, T> runner) {
        shouldNotBeNull(period, "period");
        shouldNotBeNull(runner, "runner");
        shouldBe(period.hasPeriod(), "period는 기간을 가져야합니다. period=%s", period);

        if (log.isDebugEnabled())
            log.debug("기간[{}]을 [{}] 단위로 열거하여, 병렬로 메소드르 실행시켜 결과를 반환합니다.", period, periodUnit);

        return Parallels.runEach(foreachPeriods(period, periodUnit), runner);
    }

    // endregion << ForEach >>
}
