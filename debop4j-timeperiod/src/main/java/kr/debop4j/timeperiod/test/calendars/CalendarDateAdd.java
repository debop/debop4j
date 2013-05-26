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

import kr.debop4j.core.Guard;
import kr.debop4j.core.NotSupportException;
import kr.debop4j.core.Pair;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.test.tools.Durations;
import kr.debop4j.timeperiod.test.tools.TimeSpec;
import kr.debop4j.timeperiod.timeline.TimeGapCalculator;
import kr.debop4j.timeperiod.timerange.WeekRange;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.debop4j.core.Guard.shouldBe;
import static kr.debop4j.core.Guard.shouldNotBeNull;
import static org.joda.time.Duration.ZERO;

/**
 * 특정 Calendar 기준으로 특정 시각과 기간(Duration)을 이용하여 상대 시각을 구합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오후 9:36
 */
@Slf4j
public class CalendarDateAdd extends DateAdd {

    @Getter private final ITimeCalendar timeCalendar;

    /** 일하는 요일 */
    @Getter private final List<DayOfWeek> weekDays = new ArrayList<>();

    /** 하루중 일하는 시간들 (오전, 오후, 야간으로 나뉠 수 있으므로 */
    @Getter private final List<HourRangeInDay> workingHours = new ArrayList<>();

    /** 특정요일의 일하는 시간을 표현 (툐요일 반일 근무 등) */
    @Getter private final List<DayHourRange> workingDayHours = new ArrayList<>();

    public CalendarDateAdd() {
        this(TimeCalendar.getDefault());
    }

    public CalendarDateAdd(ITimeCalendar calendar) {

        shouldNotBeNull(calendar, "calendar");
        shouldBe(calendar.getStartOffset().isEqual(ZERO), "Calendar.StartOffset은 Duration.ZERO 이어야 합니다.");
        shouldBe(calendar.getEndOffset().isEqual(ZERO), "Calendar.EndOffset은 Duration.ZERO 이어야 합니다.");

        this.timeCalendar = calendar;
    }

    @Override
    public ITimePeriodCollection getIncludePeriods() {
        throw new NotSupportException("IncludePeriods는 지원하지 않습니다.");
    }

    /** 주중 (월-금)을 working day로 추가합니다. */
    public void addWorkingDays() {
        addWeekDays(TimeSpec.Weekdays);
    }

    /** 주말 (토-일)을 working day로 추가합니다. */
    public void addWeekendDays() {
        addWeekDays(TimeSpec.Weekends);
    }

    private void addWeekDays(DayOfWeek... dayOfWeeks) {
        Collections.addAll(weekDays, dayOfWeeks);
    }

    @Override
    public DateTime add(DateTime start, Duration offset, SeekBoundaryMode seekBoundary) {
        if (log.isTraceEnabled())
            log.trace("add. start [{}] + offset [{}]의 시각을 계산합니다... seekBoundary=[{}]", start, offset, seekBoundary);

        if (getWeekDays().size() == 0 && getExcludePeriods().size() == 0 && getWorkingHours().size() == 0)
            return start.plus(offset);

        Pair<DateTime, Duration> endTuple = (offset.compareTo(ZERO) < 0)
                ? calculateEnd(start, Durations.negate(offset), SeekDirection.Backward, seekBoundary)
                : calculateEnd(start, offset, SeekDirection.Forward, seekBoundary);

        DateTime end = endTuple.getV1();

        if (log.isTraceEnabled())
            log.trace("add. start [{}] + offset [{}] => end=[{}] seekBoundary=[{}]", start, offset, end, seekBoundary);

        return end;
    }

    @Override
    public DateTime subtract(DateTime start, Duration offset, SeekBoundaryMode seekBoundary) {
        if (log.isTraceEnabled())
            log.trace("subtract. start [{}] - offset [{}]의 시각을 계산합니다... seekBoundary=[{}]", start, offset, seekBoundary);

        if (getWeekDays().size() == 0 && getExcludePeriods().size() == 0 && getWorkingHours().size() == 0)
            return start.minus(offset);

        Pair<DateTime, Duration> endTuple = (offset.compareTo(ZERO) < 0)
                ? calculateEnd(start, Durations.negate(offset), SeekDirection.Forward, seekBoundary)
                : calculateEnd(start, offset, SeekDirection.Backward, seekBoundary);

        DateTime end = endTuple.getV1();

        if (log.isTraceEnabled())
            log.trace("subtract. start [{}] - offset [{}] => end=[{}] seekBoundary=[{}]", start, offset, end, seekBoundary);

        return end;
    }

    /**
     * 기준 시각으로부터 offset 만큼 떨어진 시각을 구합니다.
     *
     * @param start        시작 시각
     * @param offset       기간
     * @param seekDir      탐색 방향
     * @param seekBoundary 경계 값 포함 여부
     * @return 기준 시각으로터 오프셋만큼 떨어진 시각, 짜투리 시
     */
    @Override
    protected Pair<DateTime, Duration> calculateEnd(DateTime start, Duration offset, SeekDirection seekDir, SeekBoundaryMode seekBoundary) {
        if (log.isTraceEnabled())
            log.trace("기준시각으로부터 offset 기간만큼 떨어진 시각을 구합니다... start=[{}], offset=[{}], seekDir=[{}], seekBoundary=[{}]",
                      start, offset, seekDir, seekBoundary);
        Guard.shouldBe(offset.compareTo(ZERO) >= 0, "offset 값은 0 이상 이어야 합니다. offset=[%d]", offset.getMillis());

        DateTime end = null;
        DateTime moment = start;
        Duration remaining = offset;

        WeekRange week = new WeekRange(start, getTimeCalendar());

        while (week != null) {
            super.getIncludePeriods().clear();
            super.getIncludePeriods().addAll(getAvailableWeekPeriods(week));

            if (log.isTraceEnabled())
                log.trace("가능한 기간=[{}]", StringTool.listToString(super.getIncludePeriods()));

            Pair<DateTime, Duration> result = super.calculateEnd(moment, remaining, seekDir, seekBoundary);
            end = result.getV1();
            remaining = result.getV2();

            if (end != null || remaining == null)
                break;

            if (seekDir == SeekDirection.Forward) {
                week = findNextWeek(week);
                if (week != null)
                    moment = week.getStart();
            } else {
                week = findPreviousWeek(week);
                if (week != null)
                    moment = week.getEnd();
            }
        }

        if (log.isTraceEnabled())
            log.trace("기준시각으로부터 offset 기간만큼 떨어진 시각을 구했습니다. start=[{}], offset=[{}], seekDir=[{}], seekBoundary=[{}], end=[{}], remaining=[{}]",
                      start, offset, seekDir, seekBoundary, end, remaining);

        return Pair.create(end, remaining);
    }

    /**
     * current 기준으로 예외 기간 등을 고려한 후행의 가장 근접한 WeekRange를 구합니다.
     *
     * @param current 기준 주(Week)
     * @return 다음
     */
    private WeekRange findNextWeek(WeekRange current) {
        if (log.isTraceEnabled())
            log.trace("현 week[{}]의 이후 week 기간을 구합니다...", current);

        WeekRange next = null;

        if (getExcludePeriods().size() == 0) {
            next = current.nextWeek();
        } else {
            TimeRange limits = new TimeRange(current.getEnd().plusMillis(1), TimeSpec.MaxPeriodTime);
            TimeGapCalculator<TimeRange> gapCalculator = new TimeGapCalculator<>(getTimeCalendar());
            ITimePeriodCollection remainingPeriods = gapCalculator.getGaps(getExcludePeriods(), limits);

            if (remainingPeriods.size() > 0)
                next = new WeekRange(remainingPeriods.get(0).getEnd(), getTimeCalendar());
        }
        if (log.isTraceEnabled())
            log.trace("현 week[{}]의 이후 week 는 [{}] ", current, next);

        return next;
    }

    /**
     * current 기준으로 예외 기간 등을 고려한 선행의 가장 근접한 WeekRange를 구합니다.
     *
     * @param current 기준 주(Week)
     * @return 선행
     */
    private WeekRange findPreviousWeek(WeekRange current) {
        if (log.isTraceEnabled())
            log.trace("현 week[{}]의 이전 week 기간을 구합니다...", current);

        WeekRange previous = null;

        if (getExcludePeriods().size() == 0) {
            previous = current.previousWeek();
        } else {
            TimeRange limits = new TimeRange(TimeSpec.MinPeriodTime, current.getStart().plusMillis(-1));
            TimeGapCalculator<TimeRange> gapCalculator = new TimeGapCalculator<>(getTimeCalendar());
            ITimePeriodCollection remainingPeriods = gapCalculator.getGaps(getExcludePeriods(), limits);

            if (remainingPeriods.size() > 0)
                previous = new WeekRange(remainingPeriods.get(remainingPeriods.size() - 1).getEnd(),
                                         getTimeCalendar());
        }
        if (log.isTraceEnabled())
            log.trace("현 week[{}]의 이전 week 는 [{}] ", current, previous);

        return previous;
    }

    /**
     * 지정한 기간 내에서 예외 기간등을 제외한 기간들을 HourRange 컬렉션으로 단위로 반환합니다.
     *
     * @param period 전체 기간
     * @return 제외할 기간을 뺀 기간들
     */
    private Iterable<ITimePeriod> getAvailableWeekPeriods(ITimePeriod period) {
        shouldNotBeNull(period, "period");

        if (weekDays.size() == 0 && workingHours.size() == 0 && workingDayHours.size() == 0) {
            TimePeriodCollection result = new TimePeriodCollection();
            result.add(period);
            return result;
        }

        CalendarPeriodCollectorFilter filter = new CalendarPeriodCollectorFilter();
        filter.getWeekDays().addAll(weekDays);
        filter.getCollectingHours().addAll(workingHours);
        filter.getCollectingDayHours().addAll(workingDayHours);

        CalendarPeriodCollector weekCollector = new CalendarPeriodCollector(filter, period, SeekDirection.Forward, getTimeCalendar());
        weekCollector.collectHours();

        return weekCollector.getPeriods();
    }

    private static final long serialVersionUID = -2499923637191503226L;
}
