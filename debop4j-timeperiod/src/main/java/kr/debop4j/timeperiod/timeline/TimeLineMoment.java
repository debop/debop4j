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

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.ITimeLineMoment;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.ITimePeriodCollection;
import kr.debop4j.timeperiod.TimePeriodCollection;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.timeline.TimeLineMoment
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오전 11:25
 */
@Slf4j
public class TimeLineMoment extends ValueObjectBase implements ITimeLineMoment {

    private static final long serialVersionUID = 8524596139661439627L;

    private final DateTime moment;
    private final ITimePeriodCollection periods = new TimePeriodCollection();

    /**
     * Instantiates a new Time line moment.
     *
     * @param moment the moment
     */
    public TimeLineMoment(DateTime moment) {
        this.moment = moment;
    }

    @Override
    public DateTime getMoment() {
        return this.moment;
    }

    @Override
    public ITimePeriodCollection getPeriods() {
        return periods;
    }

    @Override
    public int getStartCount() {
        int count = 0;
        for (ITimePeriod period : periods) {
            if (period.getStart().equals(getMoment()))
                count++;
        }
        return count;
    }

    @Override
    public int getEndCount() {
        int count = 0;
        for (ITimePeriod period : periods) {
            if (period.getEnd().equals(getMoment()))
                count++;
        }
        return count;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(moment);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("moment", moment)
                .add("startCount", getStartCount())
                .add("endCount", getEndCount());
    }
}
