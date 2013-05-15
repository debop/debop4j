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
import kr.debop4j.timeperiod.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.timeperiod.timeline.ITimeLine
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 12:01
 */
@Slf4j
public class TimeLine<T extends ITimePeriod> implements ITimeLine {

    private static final long serialVersionUID = 8784228432548497611L;

    public TimeLine(ITimePeriodContainer periods) {
        this(periods, null, null);
    }

    public TimeLine(ITimePeriodContainer periods, ITimePeriodMapper periodMapper) {
        this(periods, null, periodMapper);
    }

    public TimeLine(ITimePeriodContainer periods, ITimePeriod limits, ITimePeriodMapper periodMapper) {
        Guard.shouldNotBeNull(periods, "periods");

        this.periods = periods;
        this.limits = (limits != null) ? new TimeRange(limits) : new TimeRange(periods);
        this.periodMapper = periodMapper;
    }

    @Getter private final ITimePeriodContainer periods;
    @Getter private final ITimePeriod limits;
    @Getter private final ITimePeriodMapper periodMapper;

    @Override
    public ITimePeriodContainer getPeriods() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriod getLimits() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodMapper getPeriodMapper() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodCollection combinePeriods() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodCollection intersectPeriods() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ITimePeriodCollection calculateGaps() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
