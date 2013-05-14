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

import com.google.common.collect.Lists;
import kr.debop4j.core.Function1;
import kr.debop4j.core.Guard;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.PeriodKind;
import kr.debop4j.timeperiod.TimeRange;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 기간에 대한 컬렉션을 나타냅니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 14. 오후 9:28
 */
@Slf4j
public abstract class TimeLists {

    private TimeLists() {}

    public static List<ITimePeriod> foreachPeriods(ITimePeriod period, PeriodKind periodKind) {
        switch (periodKind) {
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
                throw new IllegalArgumentException("지원하지 않는 PeriodKind입니다. PeriodKind=" + periodKind);
        }
    }

    /**
     * 지정된 기간을 년단위로 컬렉션을 만듭니다.
     *
     * @param period
     * @return
     */
    public static List<ITimePeriod> foreachYears(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 Year 단위로 열거합니다...", period);

        List<ITimePeriod> years = Lists.newArrayList();
        if (period.isAnytime())
            return years;
        assertHasPeriod(period);

        if (TimeTool.isSameYear(period.getStart(), period.getEnd())) {
            years.add(new TimeRange(period));
            return years;
        }

        years.add(new TimeRange(period.getStart(), TimeTool.endTimeOfYear(period.getStart())));

        DateTime current = TimeTool.startTimeOfYear(period.getStart()).plusYears(1);
        int endYear = period.getEnd().getYear();
        while (current.getYear() < endYear) {
            years.add(TimeTool.getYearRange(current));
            current = current.plusYears(1);
        }

        if (current.compareTo(period.getEnd()) < 0) {
            years.add(new TimeRange(TimeTool.startTimeOfYear(current), period.getEnd()));
        }

        return years;
    }

    /**
     * 지정된 기간을 반기 단위로 열거합니다.
     *
     * @param period
     * @return
     */
    public static List<ITimePeriod> foreachHalfyears(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 Halfyear 단위로 열거합니다...", period);

        List<ITimePeriod> halfyears = Lists.newArrayList();
        if (period.isAnytime())
            return halfyears;

        assertHasPeriod(period);

        if (TimeTool.isSameHalfyear(period.getStart(), period.getEnd())) {
            halfyears.add(new TimeRange(period));
            return halfyears;
        }

        DateTime current = TimeTool.endTimeOfHalfyear(period.getStart());
        halfyears.add(new TimeRange(period.getStart(), current));

        int endHashCode = period.getEnd().getYear() * 10 + TimeTool.halfyearOf(period.getEnd()).getValue();
        current = current.plusDays(1);
        while (current.getYear() * 10 + TimeTool.halfyearOf(current).getValue() < endHashCode) {
            halfyears.add(TimeTool.getHalfyearRange(current));
            current = current.plusMonths(TimeSpec.MonthsPerHalfyear);
        }

        if (current.compareTo(period.getEnd()) < 0) {
            halfyears.add(new TimeRange(TimeTool.startTimeOfHalfyear(current), period.getEnd()));
        }

        return halfyears;
    }

    /**
     * 지정된 기간을 분기단위로 열거합니다.
     *
     * @param period
     * @return
     */
    public static List<ITimePeriod> foreachQuarters(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 Quarter 단위로 열거합니다...", period);

        List<ITimePeriod> quarters = Lists.newArrayList();
        if (period.isAnytime())
            return quarters;

        assertHasPeriod(period);

        if (TimeTool.isSameQuarter(period.getStart(), period.getEnd())) {
            quarters.add(new TimeRange(period));
            return quarters;
        }

        DateTime current = TimeTool.endTimeOfQuarter(period.getStart());
        quarters.add(new TimeRange(period.getStart(), current));

        int endHashCode = period.getEnd().getYear() * 10 + TimeTool.quarterOf(period.getEnd()).getValue();
        current = current.plusDays(1);
        while (current.getYear() * 10 + TimeTool.quarterOf(current).getValue() < endHashCode) {
            quarters.add(TimeTool.getQuarterRange(current));
            current = current.plusMonths(TimeSpec.MonthsPerQuarter);
        }

        if (current.compareTo(period.getEnd()) < 0)
            quarters.add(new TimeRange(TimeTool.startTimeOfQuarter(current), period.getEnd()));

        return quarters;
    }

    /**
     * 지정된 기간을 월단위로 열거합니다.
     *
     * @param period
     * @return
     */
    public static List<ITimePeriod> foreachMonths(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 월(Month) 단위로 열거합니다...", period);

        List<ITimePeriod> months = Lists.newArrayList();
        if (period.isAnytime())
            return months;

        assertHasPeriod(period);

        if (TimeTool.isSameMonth(period.getStart(), period.getEnd())) {
            months.add(new TimeRange(period));
            return months;
        }

        DateTime current = TimeTool.endTimeOfMonth(period.getStart());
        months.add(new TimeRange(period.getStart(), current));

        DateTime monthEnd = TimeTool.startTimeOfMonth(period.getEnd());
        current = current.plusDays(1);
        while (current.compareTo(monthEnd) < 0) {
            months.add(TimeTool.getMonthRange(current));
            current = current.plusMonths(1);
        }

        current = TimeTool.startTimeOfMonth(current);
        if (current.compareTo(period.getEnd()) < 0)
            months.add(new TimeRange(current, period.getEnd()));

        return months;
    }

    public static List<ITimePeriod> foreachWeeks(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 주(Week) 단위로 열거합니다...", period);

        List<ITimePeriod> weeks = Lists.newArrayList();
        if (period.isAnytime())
            return weeks;

        assertHasPeriod(period);

        if (TimeTool.isSameWeek(period.getStart(), period.getEnd())) {
            weeks.add(new TimeRange(period));
            return weeks;
        }

        DateTime current = period.getStart();
        DateTime weekEnd = TimeTool.endTimeOfWeek(current);
        if (weekEnd.compareTo(period.getEnd()) >= 0) {
            weeks.add(new TimeRange(current, period.getEnd()));
            return weeks;
        }
        weeks.add(new TimeRange(current, weekEnd));
        current = current.plusWeeks(1);
        while (current.compareTo(period.getEnd()) < 0) {
            weeks.add(TimeTool.getWeekRange(current));
            current = current.plusWeeks(1);
        }

        current = TimeTool.startTimeOfWeek(current.minusWeeks(1));
        if (current.compareTo(period.getEnd()) < 0) {
            weeks.add(new TimeRange(current, period.getEnd()));
        }
        return weeks;
    }

    /**
     * 지정한 기간을 일(Day)단위로 열거합니다.
     *
     * @param period
     * @return
     */
    public static List<ITimePeriod> foreachDays(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 일(Day) 단위로 열거합니다...", period);

        List<ITimePeriod> days = Lists.newArrayList();
        if (period.isAnytime())
            return days;

        assertHasPeriod(period);

        if (TimeTool.isSameDay(period.getStart(), period.getEnd())) {
            days.add(new TimeRange(period));
            return days;
        }

        days.add(new TimeRange(period.getStart(), TimeTool.endTimeOfDay(period.getStart())));

        DateTime endDay = period.getEnd();
        DateTime current = period.getStart().withTimeAtStartOfDay().plusDays(1);

        while (current.compareTo(endDay) < 0) {
            days.add(TimeTool.getDayRange(current));
            current = current.plusDays(1);
        }
        if (endDay.getMillisOfDay() > 0)
            days.add(new TimeRange(endDay.withTimeAtStartOfDay(), endDay));

        return days;
    }

    public static List<ITimePeriod> foreachHours(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 시간(Hour) 단위로 열거합니다...", period);

        List<ITimePeriod> hours = Lists.newArrayList();
        if (period.isAnytime())
            return hours;

        assertHasPeriod(period);

        if (TimeTool.isSameHour(period.getStart(), period.getEnd())) {
            hours.add(new TimeRange(period));
            return hours;
        }

        hours.add(new TimeRange(period.getStart(), TimeTool.endTimeOfHour(period.getStart())));

        DateTime endHour = period.getEnd();
        DateTime current = TimeTool.trimToHour(period.getStart().getHourOfDay() + 1);

        while (current.compareTo(endHour) < 0) {
            hours.add(TimeTool.getHourRange(current));
            current = current.plusHours(1);
        }
        if (endHour.minusHours(endHour.getHourOfDay()).getMillisOfDay() > 0) {
            hours.add(new TimeRange(TimeTool.startTimeOfHour(endHour), endHour));
        }

        return hours;
    }

    public static List<ITimePeriod> foreachMinutes(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 분(Minute) 단위로 열거합니다...", period);

        List<ITimePeriod> minutes = Lists.newArrayList();
        if (period.isAnytime())
            return minutes;

        assertHasPeriod(period);

        if (TimeTool.isSameMinute(period.getStart(), period.getEnd())) {
            minutes.add(new TimeRange(period));
            return minutes;
        }

        minutes.add(new TimeRange(period.getStart(), TimeTool.endTimeOfMinute(period.getStart())));

        DateTime endMinute = period.getEnd();
        DateTime current = TimeTool.trimToMinute(period.getStart().getMinuteOfHour() + 1);

        while (current.compareTo(endMinute) < 0) {
            minutes.add(TimeTool.getMinuteRange(current));
            current = current.plusMinutes(1);
        }
        if (endMinute.minusMinutes(endMinute.getMinuteOfHour()).getMillisOfDay() > 0) {
            minutes.add(new TimeRange(TimeTool.startTimeOfMinute(endMinute), endMinute));
        }

        return minutes;
    }

    private static void assertHasPeriod(ITimePeriod period) {
        assert period != null && period.hasPeriod() : "기간이 설정되지 않았습니다. period=" + period;
    }


    public static <T> List<T> runPeriods(ITimePeriod period, PeriodKind periodKind, Function1<ITimePeriod, T> runner) {
        Guard.shouldNotBeNull(period, "period");
        Guard.shouldNotBeNull(runner, "runner");
        Guard.shouldBe(period.hasPeriod(), "period는 기간을 가져야합니다. period=%s", period);

        if (log.isDebugEnabled())
            log.debug("기간[{}]을 [{}] 단위로 열거하여, 메소드르 실행시켜 결과를 반환합니다.", period, periodKind);

        List<T> results = Lists.newArrayList();
        for (ITimePeriod item : foreachPeriods(period, periodKind)) {
            results.add(runner.execute(item));
        }

        return results;
    }

    public static <T> List<T> runPeriodsAsParallel(ITimePeriod period, PeriodKind periodKind, Function1<ITimePeriod, T> runner) {
        Guard.shouldNotBeNull(period, "period");
        Guard.shouldNotBeNull(runner, "runner");
        Guard.shouldBe(period.hasPeriod(), "period는 기간을 가져야합니다. period=%s", period);

        if (log.isDebugEnabled())
            log.debug("기간[{}]을 [{}] 단위로 열거하여, 병렬로 메소드르 실행시켜 결과를 반환합니다.", period, periodKind);

        return Parallels.runEach(foreachPeriods(period, periodKind), runner);
    }

}
