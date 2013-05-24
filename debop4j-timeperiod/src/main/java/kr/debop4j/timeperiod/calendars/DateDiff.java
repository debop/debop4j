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

import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.Quarter;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;
import java.util.Objects;

/**
 * 특정한 두 시각 사이에 {@link ITimeCalendar}, 예외 기간등을 고려한 기간을 계산합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오후 5:16
 */
@Slf4j
public class DateDiff extends ValueObjectBase implements Serializable {

    @Getter private final DateTime start;
    @Getter private final DateTime end;
    @Getter private final Duration difference;
    @Getter private final ITimeCalendar timeCalendar;

    @Getter(lazy = true) private final int years = calcYears();
    @Getter(lazy = true) private final int quarters = calcQuarters();
    @Getter(lazy = true) private final int months = calcMonths();
    @Getter(lazy = true) private final int weeks = calcWeeks();
    @Getter(lazy = true) private final int days = (int) Math.round(roundEx(getDifference().getStandardDays()));
    @Getter(lazy = true) private final int hours = (int) Math.round(roundEx(getDifference().getStandardHours()));
    @Getter(lazy = true) private final int minutes = (int) Math.round(roundEx(getDifference().getStandardMinutes()));
    @Getter(lazy = true) private final int seconds = (int) Math.round(roundEx(getDifference().getStandardSeconds()));

    @Getter(lazy = true) private final int elapsedYears = getYears();
    @Getter(lazy = true) private final int elapsedQuarters = getQuarters();
    @Getter(lazy = true) private final int elapsedMonths = getMonths() - getElapsedYears() * TimeSpec.MonthsPerYear;

    @Getter(lazy = true)
    private final DateTime elapsedStartDays = getStart().plusYears(getElapsedYears()).plusMonths(getElapsedMonths());
    @Getter(lazy = true)
    private final int elapsedDays = (int) new Duration(getElapsedStartDays(), getEnd()).getStandardDays();

    @Getter(lazy = true)
    private final DateTime elapsedStartHours = getStart().plusYears(getElapsedYears())
            .plusMonths(getElapsedMonths()).plusDays(getElapsedDays());
    @Getter(lazy = true)
    private final int elapsedHours = (int) new Duration(getElapsedStartHours(), getEnd()).getStandardHours();

    @Getter(lazy = true)
    private final DateTime elapsedStartMinutes = getStart().plusYears(getElapsedYears())
            .plusMonths(getElapsedMonths()).plusDays(getElapsedDays()).plusHours(getElapsedHours());
    @Getter(lazy = true)
    private final int elapsedMinutes = (int) new Duration(getElapsedStartMinutes(), getEnd()).getStandardMinutes();

    @Getter(lazy = true)
    private final DateTime elapsedStartSeconds = getStart().plusYears(getElapsedYears())
            .plusMonths(getElapsedMonths()).plusDays(getElapsedDays())
            .plusHours(getElapsedHours()).plusMinutes(getElapsedMinutes());
    @Getter(lazy = true)
    private final int elapsedSeconds = (int) new Duration(getElapsedStartSeconds(), getEnd()).getStandardSeconds();


    public DateDiff(DateTime moment) {
        this(moment, ClockProxy.getClock().now());
    }

    public DateDiff(DateTime moment, ITimeCalendar timeCalendar) {
        this(moment, ClockProxy.getClock().now(), timeCalendar);
    }

    public DateDiff(DateTime start, DateTime end) {
        this(start, end, TimeCalendar.getDefault());
    }

    public DateDiff(DateTime start, DateTime end, ITimeCalendar timeCalendar) {

        this.start = start;
        this.end = end;
        this.difference = new Duration(start, end);
        this.timeCalendar = (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault();
    }

    public boolean isEmpty() {
        return difference.isEqual(Duration.ZERO);
    }

    public int getStartYear() {
        return timeCalendar.getYear(getStart());
    }

    public int getEndYear() {
        return timeCalendar.getYear(getEnd());
    }

    public int getStartMonthOfYear() {
        return timeCalendar.getMonthOfYear(getStart());
    }

    public int getEndMonthOfYear() {
        return timeCalendar.getMonthOfYear(getEnd());
    }


    private int calcYears() {
        if (log.isTraceEnabled()) log.trace("calc years...");
        if (Objects.equals(start, end)) return 0;

        int compareDay = Math.min(end.getDayOfMonth(), timeCalendar.getDaysInMonth(getStartYear(), getEndMonthOfYear()));
        DateTime compareDate = new DateTime(getStartYear(), getEndMonthOfYear(), compareDay, 0, 0).plusMillis(end.getMillisOfDay());

        if (end.compareTo(start) > 0) {
            if (compareDate.compareTo(start) < 0)
                compareDate = compareDate.plusYears(1);
        } else if (compareDate.compareTo(start) > 0) {
            compareDate = compareDate.plusYears(-1);
        }

        return getEndYear() - timeCalendar.getYear(compareDate);
    }

    private int calcQuarters() {
        if (log.isTraceEnabled()) log.trace("calc quarters...");
        if (Objects.equals(start, end)) return 0;

        int year1 = Times.getYearOf(getStartYear(), getStartMonthOfYear());
        Quarter quarter1 = Times.getQuarterOfMonth(getStartMonthOfYear());

        int year2 = Times.getYearOf(getEndYear(), getEndMonthOfYear());
        Quarter quarter2 = Times.getQuarterOfMonth(getEndMonthOfYear());

        return (year2 * TimeSpec.QuartersPerYear + quarter2.getValue())
                - (year1 * TimeSpec.QuartersPerYear + quarter1.getValue());
    }

    private int calcMonths() {
        if (log.isTraceEnabled()) log.trace("calc months...");
        if (Objects.equals(start, end)) return 0;

        int compareDay = Math.min(end.getDayOfMonth(), timeCalendar.getDaysInMonth(getStartYear(), getStartMonthOfYear()));
        DateTime compareDate = new DateTime(getStartYear(), getStartMonthOfYear(), compareDay, 0, 0).plus(end.getMillisOfDay());

        if (end.compareTo(start) > 0) {
            if (compareDate.compareTo(start) < 0)
                compareDate = compareDate.plusMonths(1);
        } else if (compareDate.compareTo(start) > 0) {
            compareDate = compareDate.plusMonths(-1);
        }
        return (getEndYear() * TimeSpec.MonthsPerYear + getEndMonthOfYear())
                - (timeCalendar.getYear(compareDate) * TimeSpec.MonthsPerYear + timeCalendar.getMonthOfYear(compareDate));
    }

    private int calcWeeks() {
        if (log.isTraceEnabled()) log.trace("calc weeks...");
        if (Objects.equals(start, end)) return 0;

        DateTime week1 = Times.getStartOfWeek(start, timeCalendar.getFirstDayOfWeek());
        DateTime week2 = Times.getStartOfWeek(end, timeCalendar.getFirstDayOfWeek());

        if (Objects.equals(week1, week2)) return 0;

        return (int) (new Duration(week1, week2).getStandardDays() / TimeSpec.DaysPerWeek);
    }

    @Override
    public int hashCode() {
        return HashTool.compute(start, end, difference, timeCalendar);
    }

    @Override
    protected com.google.common.base.Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("start", start)
                .add("end", end)
                .add("difference", difference)
                .add("timeCalendar", timeCalendar);
    }

    private static double roundEx(double number) {
        return (number >= 0.0) ? Math.round(number) : -Math.round(-number);
    }

    private static final long serialVersionUID = 3415272759108830763L;
}
