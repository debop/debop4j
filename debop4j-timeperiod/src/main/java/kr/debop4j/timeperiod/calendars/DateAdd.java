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

import kr.debop4j.core.Tuple2;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.timeline.TimeGapCalculator;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kr.debop4j.core.Guard.shouldBe;

/**
 * 특정 시각과 기간(Duration)을 이용하여 상대 시각을 구합니다. (중간에 제외할 요일이나 일자를 고려합니다)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오후 11:39
 */
public class DateAdd extends ValueObjectBase {

    private static final Logger log = LoggerFactory.getLogger(DateAdd.class);
    private static final boolean isTraceEnable = log.isTraceEnabled();
    private static final boolean isDebugEnable = log.isDebugEnabled();

    @Getter private final ITimePeriodCollection includePeriods = new TimePeriodCollection();
    @Getter private final ITimePeriodCollection excludePeriods = new TimePeriodCollection();

    /** 기본 생성자 */
    public DateAdd() { }

    /** start 시각으로부터 offset 기간이 지난 시각을 계산합니다. */
    public DateTime add(DateTime start, Duration offset) {
        return add(start, offset, SeekBoundaryMode.Next);
    }

    /** start 시각으로부터 offset 기간이 지난 시각을 계산합니다. */
    public DateTime add(DateTime start, Duration offset, SeekBoundaryMode seekBoundary) {
        if (isTraceEnable)
            log.trace("Add. start=[{}] + offset=[{}]의 시각을 계산합니다. seekBoundaryMode=[{}]", start, offset, seekBoundary);

        if (getIncludePeriods().size() == 0 && getExcludePeriods().size() == 0)
            return start.plus(offset);


        Tuple2<DateTime, Duration> results = offset.compareTo(Duration.ZERO) < 0
                ? calculateEnd(start, Durations.negate(offset), SeekDirection.Backward, seekBoundary)
                : calculateEnd(start, offset, SeekDirection.Forward, seekBoundary);

        DateTime end = (results != null) ? results.getV1() : null;
        Duration remaining = (results != null) ? results.getV2() : null;

        if (isDebugEnable)
            log.debug("Add. start=[{}] + offset=[{}] 의 결과 end=[{}], remaining=[{}]입니다. seekBoundaryMode=[{}]",
                      start, offset, end, remaining, seekBoundary);

        return end;
    }

    /** start 시각으로부터 offset 기간을 뺀 (즉 이전의) 시각을 계산합니다. */
    public DateTime subtract(DateTime start, Duration offset) {
        return subtract(start, offset, SeekBoundaryMode.Next);
    }

    /** start 시각으로부터 offset 기간을 뺀 (즉 이전의) 시각을 계산합니다. */
    public DateTime subtract(DateTime start, Duration offset, SeekBoundaryMode seekBoundary) {
        if (isTraceEnable)
            log.trace("Subtract. start=[{}] - offset=[{}]의 시각을 계산합니다. seekBoundaryMode=[{}]", start, offset, seekBoundary);

        Tuple2<DateTime, Duration> results = offset.compareTo(Duration.ZERO) < 0
                ? calculateEnd(start, Durations.negate(offset), SeekDirection.Forward, seekBoundary)
                : calculateEnd(start, offset, SeekDirection.Backward, seekBoundary);

        DateTime end = (results != null) ? results.getV1() : null;
        Duration remaining = (results != null) ? results.getV2() : null;

        if (isDebugEnable)
            log.debug("Subtract. start=[{}] - offset=[{}] 의 결과 end=[{}], remaining=[{}]입니다. seekBoundaryMode=[{}]",
                      start, offset, end, remaining, seekBoundary);

        return end;
    }


    /**
     * start 시각으로부터 offset 만큼 떨어진 시각을 구합니다.
     *
     * @param start        시작 시각
     * @param offset       기간
     * @param seekDir      탐색 방향
     * @param seekBoundary 경계 값 포함 여부
     * @return 계산된 시각, 짜투리 시
     */
    protected Tuple2<DateTime, Duration> calculateEnd(DateTime start, Duration offset, SeekDirection seekDir, SeekBoundaryMode seekBoundary) {
        if (isTraceEnable)
            log.trace("기준시각으로부터 오프셋만큼 떨어진 시각을 구합니다... start=[{}], offset=[{}], seekDir=[{}], seekBoundary=[{}]",
                      start, offset, seekDir, seekBoundary);
        shouldBe(offset.compareTo(Duration.ZERO) >= 0, "offset값은 0 이상이어야 합니다. offset=[%d]", offset.getMillis());

        Duration remaining = offset;
        DateTime end;

        // search periods
        ITimePeriodCollection searchPeriods = new TimePeriodCollection(this.includePeriods);
        if (searchPeriods.size() == 0)
            searchPeriods.add(TimeRange.Anytime);

        // available periods
        ITimePeriodCollection availablePeriods = new TimePeriodCollection();
        if (excludePeriods.size() == 0) {
            availablePeriods.addAll(searchPeriods);
        } else {
            if (isTraceEnable) log.trace("예외 기간을 제외합니다.");
            TimeGapCalculator<TimeRange> gapCalculator = new TimeGapCalculator<>();
            for (ITimePeriod p : searchPeriods) {
                if (excludePeriods.hasOverlapPeriods(p)) {
                    if (isTraceEnable) log.trace("예외 기간에 속하지 않는 부분만을 추려냅니다");
                    for (ITimePeriod gap : gapCalculator.getGaps(excludePeriods, searchPeriods))
                        availablePeriods.add(gap);
                } else {
                    availablePeriods.add(p);
                }
            }
        }

        if (availablePeriods.size() == 0) {
            if (isTraceEnable) log.trace("유효한 period 가 없다면 중단합니다.");
            return Tuple2.create(null, remaining);
        }

        if (isTraceEnable) log.trace("유효기간 중 중복된 부분을 제거하기 위해 기간들을 결합니다...");
        TimePeriodCombiner periodCombiner = new TimePeriodCombiner<TimeRange>();
        availablePeriods = periodCombiner.combinePeriods(availablePeriods);

        if (isTraceEnable) log.trace("첫 시작을 찾습니다.");
        Tuple2<ITimePeriod, DateTime> result =
                (seekDir == SeekDirection.Forward)
                        ? findNextPeriod(start, availablePeriods)
                        : findPrevPeriod(start, availablePeriods);
        ITimePeriod startPeriod = result.getV1();
        DateTime seekMoment = result.getV2();

        // 첫 시작 기간이 없다면 중단합니다.
        if (startPeriod == null) return null;
        // offset 값이 0 이라면, 바로 다음 값이므로 seekMoment 를 반환합니다.
        if (offset.isEqual(Duration.ZERO)) return Tuple2.create(seekMoment, remaining);

        if (seekDir == SeekDirection.Forward) {

            for (int i = availablePeriods.indexOf(startPeriod); i < availablePeriods.size(); i++) {
                ITimePeriod gap = availablePeriods.get(i);
                Duration gapRemaining = new Duration(seekMoment, gap.getEnd());

                if (isTraceEnable)
                    log.trace("Seek forward. gap=[{}], gapRemaining=[{}], remaining=[{}], seekMoment=[{}]", gap, gapRemaining, remaining, seekMoment);

                boolean isTargetPeriod = (seekBoundary == SeekBoundaryMode.Fill)
                        ? gapRemaining.compareTo(remaining) >= 0
                        : gapRemaining.compareTo(remaining) > 0;

                if (isTargetPeriod) {
                    end = seekMoment.plus(remaining);
                    remaining = null;
                    return Tuple2.create(end, remaining);
                }

                remaining = remaining.minus(gapRemaining);
                if (i == availablePeriods.size() - 1)
                    return Tuple2.create(null, remaining);

                seekMoment = availablePeriods.get(i + 1).getStart(); // next period
            }
        } else {
            for (int i = availablePeriods.indexOf(startPeriod); i >= 0; i--) {
                ITimePeriod gap = availablePeriods.get(i);
                Duration gapRemaining = new Duration(gap.getStart(), seekMoment);

                if (isTraceEnable)
                    log.trace("Seek backward. gap=[{}], gapRemaining=[{}], remaining=[{}], seekMoment=[{}]", gap, gapRemaining, remaining, seekMoment);

                boolean isTargetPeriod = (seekBoundary == SeekBoundaryMode.Fill)
                        ? gapRemaining.compareTo(remaining) >= 0
                        : gapRemaining.compareTo(remaining) > 0;

                if (isTargetPeriod) {
                    end = seekMoment.minus(remaining);
                    remaining = null;
                    return Tuple2.create(end, remaining);
                }
                remaining = remaining.minus(gapRemaining);
                if (i == 0) return Tuple2.create(null, remaining);

                seekMoment = availablePeriods.get(i - 1).getEnd();
            }
        }

        return Tuple2.create(null, remaining);
    }


    /**
     * start가 periods의 기간 중에 가장 가까운 기간에 속해 앴으면 그 값을 반환하고, 아니면 start와 가장 근접한 후행 period를 찾는다.
     *
     * @param start   검색 일자
     * @param periods 대상 기간들
     * @return period와 후행 기간의 시작 일자
     */
    private static Tuple2<ITimePeriod, DateTime> findNextPeriod(DateTime start, Iterable<? extends ITimePeriod> periods) {
        if (isTraceEnable)
            log.trace("시작시각의 이후 기간을 찾습니다... start=[{}], periods=[{}]", start, StringTool.listToString(periods));

        ITimePeriod nearest = null;
        DateTime moment = start;
        Duration difference = TimeSpec.MaxDuration;

        for (ITimePeriod period : periods) {

            // 기간이 start 이전이라면 (before)
            if (period.getEnd().compareTo(start) < 0)
                continue;

            // start가 기간에 속한다면...
            if (period.hasInside(start)) {
                nearest = period;
                moment = start;
                break;
            }
            // 근처 값이 아니라면 포기
            Duration periodToMoment = new Duration(start, period.getStart());
            if (periodToMoment.compareTo(difference) >= 0)
                continue;

            difference = periodToMoment;
            nearest = period;
            moment = period.getStart();
        }

        if (isTraceEnable)
            log.trace("시작시각의 이후 기간을 찾았습니다. start=[{}], moment=[{}], neearest=[{}]", start, moment, nearest);

        return Tuple2.create(nearest, moment);
    }

    /**
     * start가 periods 기간 중에 가장 가까운 기간에 속해 있으면 그 값을 반환하고, 아니면 start와 가장 근접한 선행 period를 찾는다.
     *
     * @param start   검색 일자
     * @param periods 대상 기간들
     * @return period와 선행 기간의 완료 일자
     */
    private static Tuple2<ITimePeriod, DateTime> findPrevPeriod(DateTime start, Iterable<? extends ITimePeriod> periods) {
        if (isTraceEnable)
            log.trace("시작시각의 이전 기간을 찾습니다... start=[{}], periods=[{}]", start, StringTool.listToString(periods));

        ITimePeriod nearest = null;
        DateTime moment = start;
        Duration difference = TimeSpec.MaxDuration;

        for (ITimePeriod period : periods) {

            // 기간이 start 이후이라면 (after)
            if (period.getStart().compareTo(start) > 0)
                continue;

            // start가 기간에 속한다면...
            if (period.hasInside(start)) {
                nearest = period;
                moment = start;
                break;
            }
            // 근처 값이 아니라면 포기
            Duration periodToMoment = new Duration(start, period.getEnd());
            if (periodToMoment.compareTo(difference) >= 0)
                continue;

            difference = periodToMoment;
            nearest = period;
            moment = period.getEnd();
        }

        if (isTraceEnable)
            log.trace("시작시각의 이전 기간을 찾았습니다. start=[{}], moment=[{}], neearest=[{}]", start, moment, nearest);

        return Tuple2.create(nearest, moment);
    }

    private static final long serialVersionUID = 2352433294158169198L;
}
