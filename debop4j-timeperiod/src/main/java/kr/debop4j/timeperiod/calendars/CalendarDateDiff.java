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

import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.timeline.TimeGapCalculator;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static kr.debop4j.core.Guard.shouldBe;
import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.timeperiod.tools.Durations.Zero;
import static kr.debop4j.timeperiod.tools.Durations.negate;
import static kr.debop4j.timeperiod.tools.Times.startTimeOfDay;
import static org.joda.time.Duration.ZERO;

/**
 * 특정 {@link ITimeCalendar} 기준으로 특정 일자간의 기간({@link org.joda.time.Duration} 을 구합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 21. 오전 9:29
 */
@Slf4j
public class CalendarDateDiff {

    private final CalendarPeriodCollectorFilter collectorFilter = new CalendarPeriodCollectorFilter();
    @Getter private final ITimeCalendar calendar;

    public CalendarDateDiff() {
        this(TimeCalendar.getEmptyOffset());
    }

    public CalendarDateDiff(ITimeCalendar calendar) {
        shouldNotBeNull(calendar, "calendar");
        shouldBe(calendar.getStartOffset().isEqual(ZERO), "Calendar.StartOffset은 Duration.ZERO 이어야 합니다.");
        shouldBe(calendar.getEndOffset().isEqual(ZERO), "Calendar.EndOffset은 Duration.ZERO 이어야 합니다.");

        this.calendar = calendar;
    }

    public List<DayOfWeek> getWeekDays() {
        return collectorFilter.getWeekDays();
    }

    public List<HourRangeInDay> getWorkingHours() {
        return this.collectorFilter.getCollectingHours();
    }

    public List<DayHourRange> getWorkingDayHours() {
        return this.collectorFilter.getCollectingDayHours();
    }

    /**
     * 주중 (월-금)을 working day로 추가합니다.
     */
    public void addWorkingDays() {
        addWeekDays(TimeSpec.Weekdays);
    }

    /**
     * 주말 (토-일)을 working day로 추가합니다.
     */
    public void addWeekendDays() {
        addWeekDays(TimeSpec.Weekends);
    }

    private void addWeekDays(DayOfWeek... dayOfWeeks) {
        if (getWeekDays() != null)
            Collections.addAll(getWeekDays(), dayOfWeeks);
    }

    /**
     * 지정된 시각부터 현재 시각까지의 기간을 계산합니다.
     *
     * @param moment 시작 시각
     * @return 시작 시각 ~ 완료 시각 사이의 Working Hours
     */
    public Duration difference(DateTime moment) {
        return difference(moment, Times.now());
    }

    /**
     * 시작 시각 ~ 완료 시각 사이의 기간의 Working Time을 계산합니다.
     *
     * @param fromTime 시작 시각
     * @param toTime   완료 시각
     * @return 시작 시각 ~ 완료 시각 사이의 Working Hours
     */
    public Duration difference(DateTime fromTime, DateTime toTime) {
        if (CalendarDateDiff.log.isTraceEnabled())
            CalendarDateDiff.log.trace("fromTime[{}] ~ toTime[{}]의 Working Time을 구합니다.", fromTime, toTime);

        if (Objects.equals(fromTime, toTime))
            return Zero;

        boolean filterIsEmpty = this.getWeekDays().size() == 0 &&
                this.getWorkingHours().size() == 0 &&
                this.getWorkingDayHours().size() == 0;

        if (filterIsEmpty) {
            return new DateDiff(fromTime, toTime, getCalendar()).getDifference();
        }


        TimeRange differenceRange = new TimeRange(fromTime, toTime);
        CalendarPeriodCollector collector =
                new CalendarPeriodCollector(this.collectorFilter,
                                            new TimeRange(startTimeOfDay(differenceRange.getStart()),
                                                          startTimeOfDay(differenceRange.getEnd()).plusDays(1)),
                                            SeekDirection.Forward,
                                            getCalendar());
        // Gap을 계산합니다.
        TimeGapCalculator<TimeRange> gapCalculator = new TimeGapCalculator<>(getCalendar());
        ITimePeriodCollection gaps = gapCalculator.getGaps(collector.getPeriods(), differenceRange);
        Duration difference = Zero;
        for (ITimePeriod gap : gaps) {
            difference.plus(gap.getDuration());
        }

        if (CalendarDateDiff.log.isTraceEnabled())
            CalendarDateDiff.log.trace("fromTime[{}] ~ toTime[{}]의 Working Time을 구했습니다. differece=[{}]", fromTime, toTime, difference);

        return (fromTime.compareTo(toTime) <= 0) ? difference : negate(difference);
    }
}
