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

package kr.debop4j.timeperiod.timeline;

import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.timeperiod.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.Collection;

/**
 * 여러 {@link ITimePeriod}들을 시간의 흐름별로 펼쳐서 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 12:01
 */
@Slf4j
public class TimeLine<T extends ITimePeriod> implements ITimeLine {

    /**
     * Instantiates a new Time line.
     *
     * @param periods the periods
     */
    public TimeLine(ITimePeriodContainer periods) {
        this(periods, null, null);
    }

    /**
     * Instantiates a new Time line.
     *
     * @param periods      the periods
     * @param periodMapper the period mapper
     */
    public TimeLine(ITimePeriodContainer periods, ITimePeriodMapper periodMapper) {
        this(periods, null, periodMapper);
    }

    /**
     * Instantiates a new Time line.
     *
     * @param periods      the periods
     * @param limits       the limits
     * @param periodMapper the period mapper
     */
    public TimeLine(ITimePeriodContainer periods, ITimePeriod limits, ITimePeriodMapper periodMapper) {
        Guard.shouldNotBeNull(periods, "periods");

        this.periods = periods;
        this.limits = (limits != null) ? new TimeRange(limits) : new TimeRange(periods);
        this.periodMapper = periodMapper;
    }

    @Getter private final ITimePeriodContainer periods;
    @Getter private final ITimePeriod limits;
    @Getter private final ITimePeriodMapper periodMapper;

    /**
     * Periods의 기간들의 합집합에 해당하는 기간들을 반환합니다.
     */
    @Override
    public ITimePeriodCollection combinePeriods() {
        if (periods.size() == 0)
            return new TimePeriodCollection();

        ITimeLineMomentCollection moments = getTimeLineMoments();
        if (moments == null || moments.size() == 0)
            return new TimePeriodCollection(new TimeRange(this.periods));

        return TimeLines.combinePeriods(moments);
    }

    /**
     * Periods의 기간들의 교집합에 해당하는 기간들을 반환합니다.
     */
    @Override
    public ITimePeriodCollection intersectPeriods() {
        if (periods.size() == 0)
            return new TimePeriodCollection();

        ITimeLineMomentCollection moments = getTimeLineMoments();
        if (moments == null || moments.size() == 0)
            return new TimePeriodCollection();

        return TimeLines.intersectPeriods(moments);
    }

    /**
     * Periods의 기간들의 여집합에 해당하는 기간들을 반환합니다.
     */
    @Override
    public ITimePeriodCollection calculateGaps() {
        ITimePeriodCollection gapPeriods = new TimePeriodCollection();

        for (ITimePeriod period : periods) {
            if (limits.intersectsWith(period)) {
                gapPeriods.add(new TimeRange(period));
            }
        }
        ITimeLineMomentCollection moments = getTimeLineMoments(gapPeriods);
        if (moments == null || moments.size() == 0)
            return new TimePeriodCollection(limits);

        ITimePeriod range = new TimeRange(mapPeriodStart(getLimits().getStart()),
                                          mapPeriodEnd(getLimits().getEnd()));

        return TimeLines.calculateGap(moments, range);
    }

    /**
     * 기간 컬렉션으로부터 ITimeLineMoment 컬렉션을 빌드합니다
     */
    private ITimeLineMomentCollection getTimeLineMoments() {
        return getTimeLineMoments(this.periods);
    }

    /**
     * 기간 컬렉션으로부터 ITimeLineMoment 컬렉션을 빌드합니다
     */
    private ITimeLineMomentCollection getTimeLineMoments(final Collection<? extends ITimePeriod> momentPeriods) {
        if (log.isTraceEnabled())
            log.trace("기간 컬렉션으로부터 ITimeLineMoment 컬렉션을 빌드합니다...");

        ITimeLineMomentCollection moments = new TimeLineMomentCollection();
        if (momentPeriods == null || momentPeriods.size() == 0)
            return moments;

        // setup gap set with all start/end points
        //
        final ITimePeriodCollection intersections = new TimePeriodCollection();

        for (ITimePeriod mp : momentPeriods) {
            if (!mp.isMoment()) {
                ITimePeriod intersection = limits.getIntersection(mp);
                if (intersection != null && !intersection.isMoment()) {
                    if (periodMapper != null) {
                        intersection.setup(mapPeriodStart(intersection.getStart()),
                                           mapPeriodEnd(intersection.getEnd()));
                    }
                    intersections.add(intersection);
                }
            }
        }
        moments.addAll(intersections);

        if (log.isTraceEnabled())
            log.trace("기간 컬렉션으로부터 ITimeLineMoment 컬렉션을 빌드했습니다. moments=[{}]", StringTool.listToString(moments));

        return moments;
    }

    private DateTime mapPeriodStart(final DateTime moment) {
        return (periodMapper != null) ? periodMapper.unmapStart(moment) : moment;
    }

    private DateTime mapPeriodEnd(final DateTime moment) {
        return (periodMapper != null) ? periodMapper.unmapEnd(moment) : moment;
    }

    private static final long serialVersionUID = 8784228432548497611L;
}
