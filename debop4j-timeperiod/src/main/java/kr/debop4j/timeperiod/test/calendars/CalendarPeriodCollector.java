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

package kr.debop4j.timeperiod.test.calendars;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.timerange.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

import static kr.debop4j.timeperiod.test.calendars.CalendarPeriodCollectorContext.CollectKind.*;

/**
 * 칼렌다 기준으로 특정 기간(limits)에서 필터(filter)에 해당하는 기간을 추출합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오후 10:04
 */
@Slf4j
public class CalendarPeriodCollector extends CalendarVisitor<CalendarPeriodCollectorFilter, CalendarPeriodCollectorContext> {

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
        if (log.isTraceEnabled()) log.trace("collect years...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(Year);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Month 단위의 기간({@link kr.debop4j.timeperiod.timerange.MonthRange})를 수집합니다. */
    public void collectMonths() {
        if (log.isTraceEnabled()) log.trace("collect months...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(Month);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Day 단위의 기간({@link kr.debop4j.timeperiod.timerange.DayRange})를 수집합니다. */
    public void collectDays() {
        if (log.isTraceEnabled()) log.trace("collect days...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(Day);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Hour 단위의 기간({@link kr.debop4j.timeperiod.timerange.HourRange})를 수집합니다. */
    public void collectHours() {
        if (log.isTraceEnabled()) log.trace("collect hours...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(Hour);
        startPeriodVisit(context);
    }

    /** 필터에 해당하는 Minute 단위의 기간({@link kr.debop4j.timeperiod.timerange.MinuteRange})를 수집합니다. */
    public void collectMinutes() {
        if (log.isTraceEnabled()) log.trace("collect minutes...");

        CalendarPeriodCollectorContext context = new CalendarPeriodCollectorContext();
        context.setScope(Minute);
        startPeriodVisit(context);
    }

    @Override
    protected boolean enterYears(YearRangeCollection yearRangeCollection, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > Year.getValue();
    }

    @Override
    protected boolean enterMonths(YearRange yearRange, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > Month.getValue();
    }

    @Override
    protected boolean enterDays(MonthRange month, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > Day.getValue();
    }

    @Override
    protected boolean enterHours(DayRange day, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > Hour.getValue();
    }

    @Override
    protected boolean enterMinutes(HourRange hour, CalendarPeriodCollectorContext context) {
        return context.getScope().getValue() > Minute.getValue();
    }

    @Override
    protected boolean onVisitYears(YearRangeCollection years, CalendarPeriodCollectorContext context) {
        if (log.isTraceEnabled())
            log.trace("visit years... years=[{}]", years);

        if (context.getScope() != Year)
            return true;    // continue

        for (YearRange year : years.getYears()) {
            if (isMatchingYear(year, context) && checkLimits(year)) {
                getPeriods().add(year);
            }
        }
        return false;  // abort
    }

    @Override
    protected boolean onVisitYear(YearRange year, final CalendarPeriodCollectorContext context) {
        if (log.isTraceEnabled())
            log.trace("visit year... year=[{}]", year);

        if (context.getScope() != Month)
            return true;

        if (getFilter().getCollectingMonths().size() == 0) {
            for (MonthRange month : year.getMonths()) {
                if (isMatchingMonth(month, context) && checkLimits(month)) {
                    getPeriods().add(month);
                }
            }
        } else {
            for (MonthRangeInYear m : getFilter().getCollectingMonths()) {
                if (m.isSingleMonth()) {
                    MonthRange month = new MonthRange(year.getYear(),
                                                      m.getStartMonthOfYear(),
                                                      year.getTimeCalendar());
                    if (isMatchingMonth(month, context) && checkLimits(month))
                        getPeriods().add(month);
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
                        getPeriods().add(months);
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onVisitMonth(MonthRange month, final CalendarPeriodCollectorContext context) {
        if (log.isTraceEnabled())
            log.trace("visit month... month=[{}]", month);

        if (context.getScope() != Day)
            return true;

        if (getFilter().getCollectingDays().size() == 0) {
            for (DayRange day : month.getDays()) {
                if (isMatchingDay(day, context) && checkLimits(day)) {
                    getPeriods().add(day);
                }
            }
        } else {
            for (DayRangeInMonth day : getFilter().getCollectingDays()) {
                if (day.isSingleDay()) {
                    DayRange dayRange = new DayRange(month.getYear(),
                                                     month.getMonthOfYear(),
                                                     day.getStartDayOfMonth(),
                                                     month.getTimeCalendar());
                    if (isMatchingDay(dayRange, context) && checkLimits(dayRange)) {
                        getPeriods().add(dayRange);
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
                        getPeriods().add(days);
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onVisitDay(DayRange day, CalendarPeriodCollectorContext context) {
        if (log.isTraceEnabled())
            log.trace("visit day... day=[{}]", day);

        if (context.getScope() != Hour)
            return true;

        if (getFilter().getCollectingDays().size() == 0) {
            for (HourRange hour : day.getHours()) {
                if (isMatchingHour(hour, context) && checkLimits(hour)) {
                    getPeriods().add(hour);
                }
            }
        } else if (isMatchingDay(day, context)) {
            for (HourRangeInDay hour : getFilter().getCollectingHours()) {
                DateTime startTime = hour.getStartHourOfDay().getDateTime(day.getStart());
                DateTime endTime = hour.getEndHourOfDay().getDateTime(day.getStart());
                CalendarTimeRange hours = new CalendarTimeRange(startTime, endTime, day.getTimeCalendar());

                if (checkExcludePeriods(hours) && checkLimits(hours)) {
                    getPeriods().add(hours);
                }
            }
        }
        return false;
    }


    //
    // 분(Minute) 단위까지 탐색하는 것은 부하만 초래합니다. DayRange 안에 getMillisOfDay()가 있으므로 굳이 할 필요 없습니다.
    //

    @Override
    protected boolean onVisitHour(HourRange hour, CalendarPeriodCollectorContext context) {
        return super.onVisitHour(hour, context);
    }
}
