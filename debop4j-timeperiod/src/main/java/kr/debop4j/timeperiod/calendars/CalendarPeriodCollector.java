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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.timerange.*;
import lombok.Getter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * 칼렌다 기준으로 특정 기간(limits)에서 필터(filter)에 해당하는 기간을 추출합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오후 10:04
 */
public class CalendarPeriodCollector extends CalendarVisitor<CalendarPeriodCollectorFilter, CalendarPeriodCollectorContext> {

    private static final Logger log = LoggerFactory.getLogger(CalendarPeriodCollector.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    public CalendarPeriodCollector(CalendarPeriodCollectorFilter filter, ITimePeriod limits) {
        super(filter, limits);
    }

    public CalendarPeriodCollector(CalendarPeriodCollectorFilter filter, ITimePeriod limits, SeekDirection seekDir) {
        super(filter, limits, seekDir);
    }

    public CalendarPeriodCollector(CalendarPeriodCollectorFilter filter, ITimePeriod limits, ITimeCalendar calendar) {
        super(filter, limits, calendar);
    }

    public CalendarPeriodCollector(CalendarPeriodCollectorFilter filter, ITimePeriod limits, SeekDirection seekDir, ITimeCalendar calendar) {
        super(filter, limits, seekDir, calendar);
    }

    @Getter
    private final ITimePeriodCollection periods = new TimePeriodCollection();

    /** 필터에 해당하는 Year 단위의 기간({@link kr.debop4j.timeperiod.timerange.YearRange})를 수집합니다. */
    public void collectYears() {
        if (isTraceEnabled) log.trace("collect years...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(CalendarPeriodCollectorContext.CollectKind.Year);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Month 단위의 기간({@link kr.debop4j.timeperiod.timerange.MonthRange})를 수집합니다. */
    public void collectMonths() {
        if (isTraceEnabled) log.trace("collect months...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(CalendarPeriodCollectorContext.CollectKind.Month);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Day 단위의 기간({@link kr.debop4j.timeperiod.timerange.DayRange})를 수집합니다. */
    public void collectDays() {
        if (isTraceEnabled) log.trace("collect days...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(CalendarPeriodCollectorContext.CollectKind.Day);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Hour 단위의 기간({@link kr.debop4j.timeperiod.timerange.HourRange})를 수집합니다. */
    public void collectHours() {
        if (isTraceEnabled) log.trace("collect hours...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(CalendarPeriodCollectorContext.CollectKind.Hour);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Minute 단위의 기간({@link kr.debop4j.timeperiod.timerange.MinuteRange})를 수집합니다. */
    public void collectMinutes() {
        if (isTraceEnabled) log.trace("collect minutes...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(CalendarPeriodCollectorContext.CollectKind.Minute);
        startPeriodVisit(context);
    }

    @Override
    protected boolean enterYears(YearRangeCollection yearRangeCollection, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > CalendarPeriodCollectorContext.CollectKind.Year.getValue();
    }

    @Override
    protected boolean enterMonths(YearRange yearRange, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > CalendarPeriodCollectorContext.CollectKind.Month.getValue();
    }

    @Override
    protected boolean enterDays(MonthRange month, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > CalendarPeriodCollectorContext.CollectKind.Day.getValue();
    }

    @Override
    protected boolean enterHours(DayRange day, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > CalendarPeriodCollectorContext.CollectKind.Hour.getValue();
    }

    @Override
    protected boolean enterMinutes(HourRange hour, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > CalendarPeriodCollectorContext.CollectKind.Minute.getValue();
    }

    @Override
    protected boolean onVisitYears(YearRangeCollection years, CalendarPeriodCollectorContext context) {
        if (isTraceEnabled) log.trace("visit years... years=[{}]", years);

        if (context.getScope() != CalendarPeriodCollectorContext.CollectKind.Year)
            return true;    // continue

        for (YearRange year : years.getYears()) {
            if (isMatchingYear(year, context) && checkLimits(year)) {
                periods.add(year);
            }
        }
        return false;  // abort
    }

    @Override
    protected boolean onVisitYear(YearRange year, final CalendarPeriodCollectorContext context) {
        if (isTraceEnabled) log.trace("visit year... year=[{}]", year.getYear());

        if (context.getScope() != CalendarPeriodCollectorContext.CollectKind.Month)
            return true;

        if (getFilter().getCollectingMonths().size() == 0) {
            for (MonthRange month : year.getMonths()) {
                if (isMatchingMonth(month, context) && checkLimits(month)) {
                    periods.add(month);
                }
            }
        } else {
            for (MonthRangeInYear m : getFilter().getCollectingMonths()) {
                if (m.isSingleMonth()) {
                    MonthRange month = new MonthRange(year.getYear(), m.getStartMonthOfYear(), year.getTimeCalendar());
                    if (isMatchingMonth(month, context) && checkLimits(month)) {
                        periods.add(month);
                    }
                } else {
                    MonthRangeCollection months =
                            new MonthRangeCollection(year.getYear(),
                                                     m.getStartMonthOfYear(),
                                                     m.getEndMonthOfYear() - m.getStartMonthOfYear(),
                                                     year.getTimeCalendar());
                    boolean isMatching = Iterables.all(months.getMonths(), new Predicate<MonthRange>() {
                        @Override
                        public boolean apply(@Nullable MonthRange input) {
                            return isMatchingMonth(input, context);
                        }
                    });
                    if (isMatching && checkLimits(months))
                        periods.addAll(months.getMonths());
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onVisitMonth(MonthRange month, final CalendarPeriodCollectorContext context) {
        if (isTraceEnabled)
            log.trace("visit month... month=[{}]", month);

        if (context.getScope() != CalendarPeriodCollectorContext.CollectKind.Day)
            return true;

        if (getFilter().getCollectingDays().size() == 0) {
            for (DayRange day : month.getDays()) {
                if (isMatchingDay(day, context) && checkLimits(day)) {
                    periods.add(day);
                }
            }
        } else {
            for (DayRangeInMonth day : getFilter().getCollectingDays()) {
                if (day.isSingleDay()) {
                    DayRange dayRange = new DayRange(month.getYear(), month.getMonthOfYear(), day.getStartDayOfMonth(), month.getTimeCalendar());
                    if (isMatchingDay(dayRange, context) && checkLimits(dayRange)) {
                        periods.add(dayRange);
                    }
                } else {
                    DayRangeCollection days =
                            new DayRangeCollection(month.getYear(),
                                                   month.getMonthOfYear(),
                                                   day.getStartDayOfMonth(),
                                                   day.getEndDayOfMonth() - day.getStartDayOfMonth(),
                                                   month.getTimeCalendar());

                    boolean isMatching = Iterables.all(days.getDays(), new Predicate<DayRange>() {
                        @Override
                        public boolean apply(@Nullable DayRange input) {
                            return isMatchingDay(input, context);
                        }
                    });
                    if (isMatching && checkLimits(days))
                        periods.addAll(days.getDays());
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onVisitDay(DayRange day, CalendarPeriodCollectorContext context) {
        if (isTraceEnabled) log.trace("visit day... day=[{}]", day);

        if (context.getScope().getValue() != CalendarPeriodCollectorContext.CollectKind.Hour.getValue())
            return true;

        if (getFilter().getCollectingHours().size() == 0) {
            for (HourRange hour : day.getHours()) {
                if (isMatchingHour(hour, context) && checkLimits(hour)) {
                    periods.add(hour);
                }
            }
        } else if (isMatchingDay(day, context)) {
            for (HourRangeInDay hour : getFilter().getCollectingHours()) {
                DateTime startTime = hour.getStart().getDateTime(day.getStart());
                DateTime endTime = hour.getEnd().getDateTime(day.getStart());
                CalendarTimeRange hours = new CalendarTimeRange(startTime, endTime, day.getTimeCalendar());

                if (checkExcludePeriods(hours) && checkLimits(hours)) {
                    periods.add(hours);
                }
            }
        }
        return false;
    }
}
