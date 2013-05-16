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
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.ITimePeriodCollection;
import kr.debop4j.timeperiod.ITimePeriodContainer;
import kr.debop4j.timeperiod.ITimePeriodMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.timeperiod.timeline.TimeGapCalculator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오후 4:12
 */
@Slf4j
public class TimeGapCalculator<T extends ITimePeriod> {

    private final ITimePeriodMapper periodMapper;

    public TimeGapCalculator() {
        this(null);
    }

    public TimeGapCalculator(ITimePeriodMapper mapper) {
        this.periodMapper = mapper;
    }

    /** TimeLine 들의 빈 공간 (Gap) 들을 계산합니다. */
    public ITimePeriodCollection getGaps(ITimePeriodContainer excludePeriods) {
        return getGaps(excludePeriods, null);
    }

    /** TimeLine 들의 빈 공간 (Gap) 들을 계산합니다. */
    public ITimePeriodCollection getGaps(ITimePeriodContainer excludePeriods, ITimePeriod limits) {
        Guard.shouldNotBeNull(excludePeriods, "excludePeriods");

        if (log.isDebugEnabled())
            log.debug("Period들의 Gap을 계산합니다... excludePeriods=[{}], limits=[{}]", excludePeriods, limits);

        TimeLine timeLine = new TimeLine<T>(excludePeriods, limits, periodMapper);
        return timeLine.calculateGaps();
    }
}
