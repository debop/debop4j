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

package kr.debop4j.timeperiod.calendars;

import com.google.common.collect.Iterables;
import kr.debop4j.timeperiod.DayOfWeek;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.SeekDirection;
import kr.debop4j.timeperiod.timerange.*;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * 특정 기간에 대한 필터링 정보를 기반으로 기간들을 필터링 할 수 있도록 특정 기간을 탐색하는 Visitor입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오전 3:19
 */
public abstract class CalendarVisitor<F extends ICalendarVisitorFilter, C extends ICalendarVisitorContext> {

    private static final Logger log = LoggerFactory.getLogger(CalendarVisitor.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Getter private final F filter;
    @Getter private final ITimePeriod limits;
    @Getter private final SeekDirection seekDirection;
    @Getter private final ITimeCalendar calendar;

    public CalendarVisitor(F filter, ITimePeriod limits) {
        this(filter, limits, SeekDirection.Forward, null);
    }

    public CalendarVisitor(F filter, ITimePeriod limits, SeekDirection seekDir) {
        this(filter, limits, seekDir, null);
    }

    public CalendarVisitor(F filter, ITimePeriod limits, ITimeCalendar calendar) {
        this(filter, limits, SeekDirection.Forward, calendar);
    }

    public CalendarVisitor(F filter, ITimePeriod limits, SeekDirection seekDir, ITimeCalendar calendar) {
        shouldNotBeNull(filter, "filter");
        shouldNotBeNull(limits, "limits");

        this.filter = filter;
        this.limits = limits;
        this.seekDirection = seekDir;
        this.calendar = calendar;
    }

    protected final void startPeriodVisit(C context) {
        startPeriodVisit(this.limits, context);
    }

    protected final void startPeriodVisit(ITimePeriod period, C context) {
        if (isDebugEnabled)
            log.debug("기간에 대한 탐색을 시작합니다... period=[{}], context=[{}]", period, context);
        shouldNotBeNull(period, "period");

        if (period.isMoment()) return;

        onVisitStart();

        YearRangeCollection years = (calendar != null)
                ? new YearRangeCollection(period.getStart().getYear(), period.getEnd().getYear() - period.getStart().getYear() + 1, calendar)
                : new YearRangeCollection(period.getStart().getYear(), period.getEnd().getYear() - period.getStart().getYear() + 1, calendar);

        if (onVisitYears(years, context) && enterYears(years, context)) {
            List<YearRange> yearsToVisit = years.getYears();

            if (seekDirection == SeekDirection.Backward)
                Collections.sort(yearsToVisit, Times.getEndComparator());

            for (YearRange year : yearsToVisit) {
                if (isTraceEnabled) log.trace("year를 탐색합니다... year=[{}]", year.getYear());

                if (!year.overlapsWith(period) || !onVisitYear(year, context))
                    continue;
                if (!enterMonths(year, context))
                    continue;

                List<MonthRange> monthsToVisit = year.getMonths();
                if (seekDirection == SeekDirection.Backward)
                    Collections.sort(monthsToVisit, Times.getEndComparator());

                // TODO: Parallels.runEach(); 를 이용해 병렬로 수행하는 것 테스트 할 것
                for (MonthRange month : monthsToVisit) {
                    if (isTraceEnabled) log.trace("month를 탐색합니다... month=[{}]", month);

                    if (!month.overlapsWith(period) || !onVisitMonth(month, context))
                        continue;
                    if (!enterDays(month, context))
                        continue;

                    List<DayRange> daysToVisit = month.getDays();

                    if (seekDirection == SeekDirection.Backward)
                        Collections.sort(daysToVisit, Times.getEndComparator());

                    for (DayRange day : daysToVisit) {
                        if (isTraceEnabled) log.trace("day를 탐색합니다... day=[{}]", day);

                        if (!day.overlapsWith(period))
                            continue;
                        if (!onVisitDay(day, context))
                            continue;
                        if (!enterHours(day, context))
                            continue;

                        List<HourRange> hoursToVisit = day.getHours();

                        if (seekDirection == SeekDirection.Backward)
                            Collections.sort(hoursToVisit, Times.getEndComparator());

                        for (HourRange hour : hoursToVisit) {
                            if (log.isTraceEnabled()) log.trace("hour를 탐색합니다... hour=[{}]", hour);

                            if (!hour.overlapsWith(period) || !onVisitHour(hour, context))
                                continue;

                            enterMinutes(hour, context);
                        }
                    }
                }
            }
        }

        onVisitEnd();

        if (isDebugEnabled)
            log.debug("기간에 대한 탐색을 완료했습니다!!! period=[{}], context=[{}]", period, context);
    }

    protected final YearRange startYearVisit(YearRange year, C context, SeekDirection direction) {
        shouldNotBeNull(year, "year");
        if (isTraceEnabled)
            log.trace("Year 단위로 탐색합니다. year=[{}], context=[{}], direction=[{}]", year, context, direction);

        YearRange lastVisited = null;

        onVisitStart();

        DateTime minStart = TimeSpec.MinPeriodTime;
        DateTime maxEnd = TimeSpec.MaxPeriodTime.minusYears(1);
        int offset = direction.getValue();

        while (year.getStart().compareTo(minStart) > 0 && year.getEnd().compareTo(maxEnd) < 0) {
            if (!onVisitYear(year, context)) {
                lastVisited = year;
                break;
            }
            year = year.addYears(offset);
        }

        onVisitEnd();
        if (isTraceEnabled) log.trace("마지막 탐색 Month. lastVisited=[{}]", lastVisited);

        return lastVisited;
    }

    protected final MonthRange startMonthVisit(MonthRange month, C context, SeekDirection direction) {
        shouldNotBeNull(month, "month");
        if (isTraceEnabled)
            log.trace("Month 단위로 탐색합니다. month=[{}], context=[{}], direction=[{}]", month, context, direction);

        MonthRange lastVisited = null;

        onVisitStart();

        DateTime minStart = TimeSpec.MinPeriodTime;
        DateTime maxEnd = TimeSpec.MaxPeriodTime.minusYears(1);
        int offset = direction.getValue();

        while (month.getStart().compareTo(minStart) > 0 && month.getEnd().compareTo(maxEnd) < 0) {
            if (!onVisitMonth(month, context)) {
                lastVisited = month;
                break;
            }
            month = month.addMonths(offset);
        }

        onVisitEnd();
        if (isTraceEnabled) log.trace("마지막 탐색 Month. lastVisited=[{}]", lastVisited);

        return lastVisited;
    }

    protected final DayRange startDayVisit(DayRange day, C context, SeekDirection direction) {
        shouldNotBeNull(day, "day");
        if (isTraceEnabled)
            log.trace("Day 단위로 탐색합니다. day=[{}], context=[{}], direction=[{}]", day, context, direction);

        DayRange lastVisited = null;

        onVisitStart();

        DateTime minStart = TimeSpec.MinPeriodTime;
        DateTime maxEnd = TimeSpec.MaxPeriodTime.minusYears(1);
        int offset = direction.getValue();

        while (day.getStart().compareTo(minStart) > 0 && day.getEnd().compareTo(maxEnd) < 0) {
            if (!onVisitDay(day, context)) {
                lastVisited = day;
                break;
            }
            day = day.addDays(offset);
        }

        onVisitEnd();
        if (isTraceEnabled) log.trace("마지막 탐색 Day. lastVisited=[{}]", lastVisited);

        return lastVisited;
    }

    protected final HourRange startHourVisit(HourRange hour, C context, SeekDirection direction) {
        shouldNotBeNull(hour, "hour");
        if (isTraceEnabled)
            log.trace("Hour 단위로 탐색합니다. hour=[{}], context=[{}], direction=[{}]", hour, context, direction);

        HourRange lastVisited = null;

        onVisitStart();

        DateTime minStart = TimeSpec.MinPeriodTime;
        DateTime maxEnd = TimeSpec.MaxPeriodTime.minusYears(1);
        int offset = direction.getValue();

        while (hour.getStart().compareTo(minStart) > 0 && hour.getEnd().compareTo(maxEnd) < 0) {
            if (!onVisitHour(hour, context)) {
                lastVisited = hour;
                break;
            }
            hour = hour.addHours(offset);
        }

        onVisitEnd();
        if (isTraceEnabled) log.trace("마지막 탐색 hour. lastVisited=[{}]", lastVisited);

        return lastVisited;
    }

    protected void onVisitStart() {
        if (isTraceEnabled)
            log.trace("Calendar 탐색을 시작합니다...");
    }

    protected boolean checkLimits(ITimePeriod target) {
        return getLimits().hasInside(target);
    }

    protected boolean checkExcludePeriods(ITimePeriod target) {
        if (!this.filter.getExcludePeriods().containsPeriod(target))
            return true;

        return Iterables.isEmpty(this.filter.getExcludePeriods().overlapPeriods(target));
    }

    protected boolean enterYears(YearRangeCollection yearRangeCollection, C context) {
        return true;
    }

    protected boolean enterMonths(YearRange yearRange, C context) {
        return true;
    }

    protected boolean enterDays(MonthRange month, C context) {
        return true;
    }

    protected boolean enterHours(DayRange day, C context) {
        return true;
    }

    protected boolean enterMinutes(HourRange hour, C context) {
        return true;
    }

    protected boolean onVisitYears(YearRangeCollection years, C context) {
        return true;
    }

    protected boolean onVisitYear(YearRange year, C context) {
        return true;
    }

    protected boolean onVisitMonth(MonthRange month, C context) {
        return true;
    }

    protected boolean onVisitDay(DayRange day, C context) {
        return true;
    }

    protected boolean onVisitHour(HourRange hour, C context) {
        return true;
    }

    protected boolean onVisitMinute(MinuteRange minute, C context) {
        return true;
    }

    protected boolean isMatchingYear(YearRange year, C context) {

        if (filter.getYears().size() > 0 && !filter.getYears().contains(year.getYear()))
            return false;

        return checkExcludePeriods(year);
    }

    protected boolean isMatchingMonth(MonthRange month, C context) {

        if (filter.getYears().size() > 0 && !filter.getYears().contains(month.getYear()))
            return false;
        if (filter.getMonthOfYears().size() > 0 && !filter.getMonthOfYears().contains(month.getMonthOfYear()))
            return false;

        return checkExcludePeriods(month);
    }

    protected boolean isMatchingDay(DayRange day, C context) {
        if (filter.getYears().size() > 0 && !filter.getYears().contains(day.getYear()))
            return false;
        if (filter.getMonthOfYears().size() > 0 && !filter.getMonthOfYears().contains(day.getMonthOfYear()))
            return false;
        if (filter.getDayOfMonths().size() > 0 && !filter.getDayOfMonths().contains(day.getDayOfMonth()))
            return false;
        if (filter.getWeekDays().size() > 0 && !filter.getWeekDays().contains(day.getDayOfWeek()))
            return false;
        return checkExcludePeriods(day);
    }

    protected boolean isMatchingHour(HourRange hour, C context) {
        if (filter.getYears().size() > 0 && !filter.getYears().contains(hour.getYear()))
            return false;
        if (filter.getMonthOfYears().size() > 0 && !filter.getMonthOfYears().contains(hour.getMonthOfYear()))
            return false;
        if (filter.getDayOfMonths().size() > 0 && !filter.getDayOfMonths().contains(hour.getDayOfMonth()))
            return false;
        if (filter.getHourOfDays().size() > 0 && !filter.getHourOfDays().contains(hour.getHourOfDay()))
            return false;
        if (filter.getWeekDays().size() > 0 && !filter.getWeekDays().contains(DayOfWeek.valueOf(hour.getStart().getDayOfWeek())))
            return false;

        return checkExcludePeriods(hour);
    }

    protected boolean isMatchingMinute(MinuteRange minute, C context) {
        if (filter.getYears().size() > 0 && !filter.getYears().contains(minute.getYear()))
            return false;
        if (filter.getMonthOfYears().size() > 0 && !filter.getMonthOfYears().contains(minute.getMonthOfYear()))
            return false;
        if (filter.getDayOfMonths().size() > 0 && !filter.getDayOfMonths().contains(minute.getDayOfMonth()))
            return false;
        if (filter.getHourOfDays().size() > 0 && !filter.getHourOfDays().contains(minute.getHourOfDay()))
            return false;
        if (filter.getMinuteOfHours().size() > 0 && !filter.getMinuteOfHours().contains(minute.getMinuteOfHour()))
            return false;

        return checkExcludePeriods(minute);
    }

    protected void onVisitEnd() {
        if (isTraceEnabled)
            log.trace("Calendar 탐색을 종료합니다.");
    }
}
