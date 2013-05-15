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

package kr.debop4j.timeperiod;

import kr.debop4j.core.Guard;
import kr.debop4j.timeperiod.timeline.TimeLine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.timeperiod.TimePeriodCombiner
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오전 2:22
 */
@Slf4j
public class TimePeriodCombiner<T extends ITimePeriod> {

    public TimePeriodCombiner() {
        this(null);
    }

    public TimePeriodCombiner(ITimePeriodMapper mapper) {
        this.mapper = mapper;
    }

    @Getter
    private final ITimePeriodMapper mapper;

    public ITimePeriodCollection combinePeriods(ITimePeriod... periods) {
        return new TimeLine(new TimePeriodCollection(periods), mapper).combinePeriods();
    }

    public ITimePeriodCollection combinePeriods(ITimePeriodContainer periods) {
        Guard.shouldNotBeNull(periods, "periods");
        return new TimeLine(periods, mapper).combinePeriods();
    }
}
