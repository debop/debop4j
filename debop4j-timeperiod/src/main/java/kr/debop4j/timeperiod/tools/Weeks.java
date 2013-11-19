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

import kr.debop4j.core.Guard;
import kr.debop4j.timeperiod.DayOfWeek;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.YearAndWeek;
import kr.debop4j.timeperiod.timerange.WeekRange;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * 주(Week) 관련 Utility Class
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 2:34
 */
public abstract class Weeks {

    private static final Logger log = LoggerFactory.getLogger(Weeks.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private Weeks() {}

    /**
     * Gets first day of week.
     *
     * @return the first day of week
     */
    public static DayOfWeek getFirstDayOfWeek() {
        return TimeSpec.FirstDayOfWeek;
    }

    /**
     * Gets first day of week.
     *
     * @param locale the locale
     * @return the first day of week
     */
    public static DayOfWeek getFirstDayOfWeek(Locale locale) {
        return TimeSpec.FirstDayOfWeek;
    }

    /**
     * 지정한 일자의 주차를 구합니다.
     *
     * @param moment the moment
     * @return the year and week
     */
    public static YearAndWeek getYearAndWeek(DateTime moment) {
        return getYearAndWeek(moment, 1);
    }

    /**
     * 지정한 일자의 주차를 구합니다.
     *
     * @param moment    the moment
     * @param baseMonth the base month
     * @return the year and week
     */
    public static YearAndWeek getYearAndWeek(DateTime moment, int baseMonth) {
        return new YearAndWeek(moment.getWeekyear(), moment.getWeekOfWeekyear());
    }

    /**
     * Gets year and week.
     *
     * @param moment       the moment
     * @param timeCalendar the time calendar
     * @return the year and week
     */
    public static YearAndWeek getYearAndWeek(DateTime moment, ITimeCalendar timeCalendar) {
        return new YearAndWeek(moment);
    }

    /**
     * 해당년도의 마지막 주차를 산정합니다.
     *
     * @param year the year
     * @return the end year and week
     */
    public static YearAndWeek getEndYearAndWeek(int year) {
        return getEndYearAndWeek(year, TimeCalendar.getDefault());
    }

    /**
     * 해당년도의 마지막 주차를 산정합니다.
     *
     * @param year         the year
     * @param timeCalendar the time calendar
     * @return the end year and week
     */
    public static YearAndWeek getEndYearAndWeek(int year, ITimeCalendar timeCalendar) {
        return new YearAndWeek(Times.asDate(year, 12, 28));
    }

    /**
     * Gets week range.
     *
     * @param yearAndWeek the year and week
     * @return the week range
     */
    public static WeekRange getWeekRange(YearAndWeek yearAndWeek) {
        return getWeekRange(yearAndWeek, TimeCalendar.getDefault());
    }

    /**
     * Gets week range.
     *
     * @param yearAndWeek  the year and week
     * @param timeCalendar the time calendar
     * @return the week range
     */
    public static WeekRange getWeekRange(YearAndWeek yearAndWeek, ITimeCalendar timeCalendar) {
        return new WeekRange(yearAndWeek.getYear(), yearAndWeek.getWeekOfYear(), timeCalendar);
    }

    /**
     * 해당년도의 시작 주의 기간
     *
     * @param year the year
     * @return the start week range of year
     */
    public static WeekRange getStartWeekRangeOfYear(int year) {
        return getStartWeekRangeOfYear(year, TimeCalendar.getDefault());
    }

    /**
     * 해당년도의 시작 주의 기간
     *
     * @param year         the year
     * @param timeCalendar the time calendar
     * @return the start week range of year
     */
    public static WeekRange getStartWeekRangeOfYear(int year, ITimeCalendar timeCalendar) {
        return getWeekRange(new YearAndWeek(year, 1), timeCalendar);
    }

    /**
     * 해당년도의 마지막 주의 기간
     *
     * @param year the year
     * @return the end week range of year
     */
    public static WeekRange getEndWeekRangeOfYear(int year) {
        return getEndWeekRangeOfYear(year, TimeCalendar.getDefault());
    }

    /**
     * 해당년도의 마지막 주의 기간
     *
     * @param year         the year
     * @param timeCalendar the time calendar
     * @return the end week range of year
     */
    public static WeekRange getEndWeekRangeOfYear(int year, ITimeCalendar timeCalendar) {
        YearAndWeek endyw = getEndYearAndWeek(year, timeCalendar);
        return getWeekRange(endyw, timeCalendar);
    }

    /**
     * Add week of years.
     *
     * @param year       the year
     * @param weekOfYear the week of year
     * @param weeks      the weeks
     * @return the year and week
     */
    public static YearAndWeek addWeekOfYears(int year, int weekOfYear, int weeks) {
        return addWeekOfYears(year, weekOfYear, weeks, TimeCalendar.getDefault());
    }

    /**
     * Add week of years.
     *
     * @param year         the year
     * @param weekOfYear   the week of year
     * @param weeks        the weeks
     * @param timeCalendar the time calendar
     * @return the year and week
     */
    public static YearAndWeek addWeekOfYears(int year, int weekOfYear, int weeks, ITimeCalendar timeCalendar) {
        return addWeekOfYears(new YearAndWeek(year, weekOfYear), weeks, timeCalendar);
    }

    /**
     * Add week of years.
     *
     * @param yearAndWeek the year and week
     * @param weeks       the weeks
     * @return the year and week
     */
    public static YearAndWeek addWeekOfYears(YearAndWeek yearAndWeek, int weeks) {
        return addWeekOfYears(yearAndWeek, weeks, TimeCalendar.getDefault());
    }

    /**
     * Add week of years.
     *
     * @param yearAndWeek  the year and week
     * @param weeks        the weeks
     * @param timeCalendar the time calendar
     * @return the year and week
     */
    public static YearAndWeek addWeekOfYears(YearAndWeek yearAndWeek, int weeks, ITimeCalendar timeCalendar) {
        Guard.shouldNotBeNull(yearAndWeek, "yearAndWeek");


        log.trace("주차 연산을 수행합니다. year=[{}], weekOfYear=[{}], weeks=[{}], timeCalendar=[{}]",
                  yearAndWeek.getYear(), yearAndWeek.getWeekOfYear(), weeks, timeCalendar);

        YearAndWeek result = new YearAndWeek(yearAndWeek.getYear(), yearAndWeek.getWeekOfYear());

        if (weeks == 0)
            return result;

        result = (weeks > 0)
                ? plusWeeks(yearAndWeek, weeks, timeCalendar)
                : minusWeeks(yearAndWeek, weeks, timeCalendar);


        log.trace("주차 연산을 수행했습니다. year=[{}], weekOfYear=[{}], weeks=[{}], result=[{}]",
                  yearAndWeek.getYear(), yearAndWeek.getWeekOfYear(), weeks, result);

        return result;
    }

    private static YearAndWeek plusWeeks(YearAndWeek yearAndWeek, int weeks, ITimeCalendar timeCalendar) {
        YearAndWeek result = new YearAndWeek(yearAndWeek);
        weeks += result.getWeekOfYear();

        if (weeks < getEndYearAndWeek(result.getYear(), timeCalendar).getWeekOfYear()) {
            result.setWeekOfYear(weeks);
            return result;
        }
        while (weeks >= 0) {
            YearAndWeek endWeek = getEndYearAndWeek(result.getYear(), timeCalendar);
            if (weeks <= endWeek.getWeekOfYear()) {
                result.setWeekOfYear(Math.max(weeks, 1));
                return result;
            }
            weeks -= endWeek.getWeekOfYear();
            result.setYear(result.getYear() + 1);
        }
        result.setWeekOfYear(Math.max(weeks, 1));
        return result;
    }

    private static YearAndWeek minusWeeks(YearAndWeek yearAndWeek, int weeks, ITimeCalendar timeCalendar) {
        YearAndWeek result = new YearAndWeek(yearAndWeek);

        weeks += result.getWeekOfYear();
        if (weeks == 0) {
            result.setYear(result.getYear() - 1);
            result.setWeekOfYear(getEndYearAndWeek(result.getYear(), timeCalendar).getWeekOfYear());
            return result;
        }
        if (weeks > 0) {
            result.setWeekOfYear(weeks);
            return result;
        }
        while (weeks <= 0) {
            result.setYear(result.getYear() - 1);
            YearAndWeek endWeek = getEndYearAndWeek(result.getYear(), timeCalendar);
            weeks += endWeek.getWeekOfYear();

            if (weeks > 0) {
                result.setWeekOfYear(Math.max(weeks, 1));
                return result;
            }
        }
        result.setWeekOfYear(Math.max(weeks, 1));
        return result;
    }
}
