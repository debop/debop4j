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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kr.debop4j.timeperiod.timeline.TimeLines
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오전 11:24
 */
public abstract class TimeLines {

    private static final Logger log = LoggerFactory.getLogger(TimeLines.class);

    private TimeLines() {}

    /** {@link ITimeLineMomentCollection}의 모든 기간의 합집합을 구합니다. */
    public static ITimePeriodCollection combinePeriods(final ITimeLineMomentCollection timeLineMoments) {
        if (log.isTraceEnabled())
            log.trace("ITimeLineMomentCollection에서 모든 기간의 합집합을 구합니다...");

        ITimePeriodCollection periods = new TimePeriodCollection();
        if (timeLineMoments.isEmpty())
            return periods;

        int momentsSize = timeLineMoments.size();

        int itemIndex = 0;

        while (itemIndex < momentsSize) {
            ITimeLineMoment periodStart = timeLineMoments.get(itemIndex);
            Guard.shouldBe(periodStart.getStartCount() > 0,
                           "periodStart.getStartCount() > 0 이어야 합니다. startCount=[%d]", periodStart.getStartCount());

            // search next period end
            // using balancing to handling overlapping periods
            int balance = periodStart.getStartCount();
            ITimeLineMoment periodEnd = null;
            while (itemIndex < momentsSize - 1 && balance > 0) {
                itemIndex++;
                periodEnd = timeLineMoments.get(itemIndex);
                balance += periodEnd.getStartCount();
                balance -= periodEnd.getEndCount();
            }
            Guard.shouldNotBeNull(periodEnd, "periodEnd");

            // touching
            if (periodEnd.getStartCount() > 0) {
                itemIndex++;
                continue;
            }

            if (itemIndex < momentsSize) {
                TimeRange period = new TimeRange();
                period.setup(periodStart.getMoment(), periodEnd.getMoment());

                if (log.isTraceEnabled())
                    log.trace("combine period를 추가합니다. period=[{}]", period);

                periods.add(period);
            }
            itemIndex++;
        }

        if (log.isDebugEnabled())
            log.debug("기간을 결합했습니다. periods=[{}]", StringTool.listToString(periods));

        return periods;
    }

    /** {@link ITimeLineMomentCollection}의 모든 기간의 교집합을 구합니다. */
    public static ITimePeriodCollection intersectPeriods(final ITimeLineMomentCollection timeLineMoments) {
        if (log.isTraceEnabled())
            log.trace("ITimeLineMomentCollection의 요소들의 모든 Period로부터 교집합에 해당하는 구간을 구합니다...");

        ITimePeriodCollection periods = new TimePeriodCollection();
        if (timeLineMoments.isEmpty())
            return periods;

        int intersectionStart = -1;
        int balance = 0;

        for (int i = 0; i < timeLineMoments.size(); i++) {
            ITimeLineMoment moment = timeLineMoments.get(i);

            int startCount = moment.getStartCount();
            int endCount = moment.getEndCount();

            balance += startCount;
            balance -= endCount;

            // intersection is starting by a period start
            if (startCount > 0 && balance > 1 && intersectionStart < 0) {
                intersectionStart = i;
                continue;
            }

            // intersection is starting by a period end
            if (endCount > 0 && balance <= 1 && intersectionStart >= 0) {
                ITimePeriod period = new TimeRange();
                period.setup(timeLineMoments.get(intersectionStart).getMoment(), moment.getMoment());

                if (log.isTraceEnabled())
                    log.trace("Intersect period를 추가합니다. period=[{}]", period);

                periods.add(period);
                intersectionStart = -1;
            }
        }
        if (log.isDebugEnabled())
            log.debug("ITimeLineMomentCollection으로부터 교집합에 해당하는 기간을 구했습니다. periods=[{}]", StringTool.listToString(periods));

        return periods;
    }

    /** {@link ITimeLineMomentCollection}이 가진 모든 {@link ITimePeriod}들의 Gap을 계산합니다. (여집합) */
    public static ITimePeriodCollection calculateGap(ITimeLineMomentCollection timeLineMoments, ITimePeriod range) {
        if (log.isTraceEnabled())
            log.trace("ITimeLineMomentCollection의 모든 ITimePeriod에 속하지 않는 Gap을 구합니다(여집합). range=[{}]", range);

        ITimePeriodCollection gaps = new TimePeriodCollection();

        if (timeLineMoments.isEmpty())
            return gaps;

        // find leading gap
        ITimeLineMoment periodStart = timeLineMoments.getMin();

        if (periodStart != null && range.getStart().compareTo(periodStart.getMoment()) < 0) {
            ITimePeriod startingGap = new TimeRange();
            startingGap.setup(range.getStart(), periodStart.getMoment());

            if (log.isTraceEnabled())
                log.trace("starting gap을 추가합니다... startingGap=[{}]", startingGap);

            gaps.add(startingGap);
        }

        // find intermediated gap
        int itemIndex = 0;
        while (itemIndex < timeLineMoments.size()) {
            ITimeLineMoment moment = timeLineMoments.get(itemIndex);
            Guard.shouldNotBeNull(moment, "moment");
            Guard.shouldBe(moment.getStartCount() > 0, "moment.getStartCount() 값이 0보다 커야합니다. moment=[%s]", moment);

            // search next gap start
            // use balancing to handle overlapping periods
            int balance = moment.getStartCount();
            ITimeLineMoment gapStart = null;

            while (itemIndex < timeLineMoments.size() - 1 && balance > 0) {
                itemIndex++;
                gapStart = timeLineMoments.get(itemIndex);
                balance += gapStart.getStartCount();
                balance -= gapStart.getEndCount();
            }
            Guard.shouldNotBeNull(gapStart, "gapStart");
            if (gapStart.getStartCount() > 0) {
                itemIndex++;
                continue;
            }

            // found a gap
            if (itemIndex < timeLineMoments.size() - 1) {
                ITimePeriod gap = new TimeRange();
                gap.setup(gapStart.getMoment(), timeLineMoments.get(itemIndex + 1).getMoment());

                if (log.isTraceEnabled())
                    log.trace("intermediated gap을 추가합니다. gap=[{}]", gap);

                gaps.add(gap);
            }
            itemIndex++;
        }

        // find ending gap
        ITimeLineMoment periodEnd = timeLineMoments.getMax();

        if (periodEnd != null && range.getEnd().compareTo(periodEnd.getMoment()) > 0) {
            ITimePeriod endingGap = new TimeRange();
            endingGap.setup(periodEnd.getMoment(), range.getEnd());

            if (log.isTraceEnabled())
                log.trace("ending gap을 추가합니다. endingGap=[{}]", endingGap);

            gaps.add(endingGap);
        }

        if (log.isDebugEnabled())
            log.debug("ITimeLineMomentCollection에서 gap을 계산했습니다. gaps=[{}]", StringTool.listToString(gaps));

        return gaps;
    }
}
