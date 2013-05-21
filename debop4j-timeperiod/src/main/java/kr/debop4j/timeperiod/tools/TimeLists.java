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
import kr.debop4j.timeperiod.PeriodUnit;
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

    /** 지정된 기간을 기간 단위별로 세분하여 컬렉션을 빌드합니다. */
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

    /** 지정된 기간을 년단위로 컬렉션을 만듭니다. */
    public static List<ITimePeriod> foreachYears(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
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
        while (current.getYear() < endYear) {
            years.add(Times.getYearRange(current, null));
            current = current.plusYears(1);
        }

        if (current.compareTo(period.getEnd()) < 0) {
            years.add(new TimeRange(Times.startTimeOfYear(current), period.getEnd()));
        }

        return years;
    }

    /** 지정된 기간을 반기 단위로 열거합니다. */
    public static List<ITimePeriod> foreachHalfyears(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
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
        while (current.getYear() * 10 + Times.halfyearOf(current).getValue() < endHashCode) {
            halfyears.add(Times.getHalfyearRange(current, null));
            current = current.plusMonths(TimeSpec.MonthsPerHalfyear);
        }

        if (current.compareTo(period.getEnd()) < 0) {
            halfyears.add(new TimeRange(Times.startTimeOfHalfyear(current), period.getEnd()));
        }

        return halfyears;
    }

    /** 지정된 기간을 분기단위로 열거합니다. */
    public static List<ITimePeriod> foreachQuarters(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
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
        while (current.getYear() * 10 + Times.quarterOf(current).getValue() < endHashCode) {
            quarters.add(Times.getQuarterRange(current, null));
            current = current.plusMonths(TimeSpec.MonthsPerQuarter);
        }

        if (current.compareTo(period.getEnd()) < 0)
            quarters.add(new TimeRange(Times.startTimeOfQuarter(current), period.getEnd()));

        return quarters;
    }

    /** 지정된 기간을 월(Month) 단위로 열거합니다. */
    public static List<ITimePeriod> foreachMonths(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
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
        while (current.compareTo(monthEnd) < 0) {
            months.add(Times.getMonthRange(current, null));
            current = current.plusMonths(1);
        }

        current = Times.startTimeOfMonth(current);
        if (current.compareTo(period.getEnd()) < 0)
            months.add(new TimeRange(current, period.getEnd()));

        return months;
    }

    /** 지정된 기간을 주(Week) 단위로 열거합니다. */
    public static List<ITimePeriod> foreachWeeks(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
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
        current = current.plusWeeks(1);
        while (current.compareTo(period.getEnd()) < 0) {
            weeks.add(Times.getWeekRange(current, null));
            current = current.plusWeeks(1);
        }

        current = Times.startTimeOfWeek(current.minusWeeks(1));
        if (current.compareTo(period.getEnd()) < 0) {
            weeks.add(new TimeRange(current, period.getEnd()));
        }
        return weeks;
    }

    /** 지정한 기간을 일(Day)단위로 열거합니다. */
    public static List<ITimePeriod> foreachDays(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 일(Day) 단위로 열거합니다...", period);

        List<ITimePeriod> days = Lists.newArrayList();
        if (period.isAnytime())
            return days;

        assertHasPeriod(period);

        if (Times.isSameDay(period.getStart(), period.getEnd())) {
            days.add(new TimeRange(period));
            return days;
        }

        days.add(new TimeRange(period.getStart(), Times.endTimeOfDay(period.getStart())));

        DateTime endDay = period.getEnd();
        DateTime current = period.getStart().withTimeAtStartOfDay().plusDays(1);

        while (current.compareTo(endDay) < 0) {
            days.add(Times.getDayRange(current, null));
            current = current.plusDays(1);
        }
        if (endDay.getMillisOfDay() > 0)
            days.add(new TimeRange(endDay.withTimeAtStartOfDay(), endDay));

        return days;
    }

    /** 지정한 기간을 시(Hour) 단위로 열거합니다. */
    public static List<ITimePeriod> foreachHours(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 시간(Hour) 단위로 열거합니다...", period);

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

        while (current.compareTo(endHour) < 0) {
            hours.add(Times.getHourRange(current, null));
            current = current.plusHours(1);
        }
        if (endHour.minusHours(endHour.getHourOfDay()).getMillisOfDay() > 0) {
            hours.add(new TimeRange(Times.startTimeOfHour(endHour), endHour));
        }

        return hours;
    }

    /** 지정한 기간을 분(Minute) 단위로 열거합니다. */
    public static List<ITimePeriod> foreachMinutes(ITimePeriod period) {
        assert period != null;
        if (log.isTraceEnabled())
            log.trace("기간[{}]에 대해 분(Minute) 단위로 열거합니다...", period);

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

        while (current.compareTo(endMinute) < 0) {
            minutes.add(Times.getMinuteRange(current, null));
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

    /** 기간을 특정 단위로 열거한 값을 이용하여 특정 코드를 수행하여 결과값을 반환합니다. */
    public static <T> List<T> runPeriods(ITimePeriod period, PeriodUnit periodUnit, Function1<ITimePeriod, T> runner) {
        Guard.shouldNotBeNull(period, "period");
        Guard.shouldNotBeNull(runner, "runner");
        Guard.shouldBe(period.hasPeriod(), "period는 기간을 가져야합니다. period=%s", period);

        if (log.isDebugEnabled())
            log.debug("기간[{}]을 [{}] 단위로 열거하여, 메소드르 실행시켜 결과를 반환합니다.", period, periodUnit);

        List<T> results = Lists.newArrayList();
        for (ITimePeriod item : foreachPeriods(period, periodUnit)) {
            results.add(runner.execute(item));
        }

        return results;
    }

    /** 기간을 특정 단위로 열거한 값을 이용하여 특정 코드를 병렬로 수행하여 결과값을 반환합니다. */
    public static <T> List<T> runPeriodsAsParallel(ITimePeriod period, PeriodUnit periodUnit, Function1<ITimePeriod, T> runner) {
        Guard.shouldNotBeNull(period, "period");
        Guard.shouldNotBeNull(runner, "runner");
        Guard.shouldBe(period.hasPeriod(), "period는 기간을 가져야합니다. period=%s", period);

        if (log.isDebugEnabled())
            log.debug("기간[{}]을 [{}] 단위로 열거하여, 병렬로 메소드르 실행시켜 결과를 반환합니다.", period, periodUnit);

        return Parallels.runEach(foreachPeriods(period, periodUnit), runner);
    }

}
