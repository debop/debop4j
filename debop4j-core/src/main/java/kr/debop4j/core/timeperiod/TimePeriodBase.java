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

package kr.debop4j.core.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * debop4j.timeperiod.TimePeriodBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 11.
 */
@Slf4j
@Getter
@Setter
public abstract class TimePeriodBase extends ValueObjectBase implements ITimePeriod {

    private static final long serialVersionUID = -2693028430637125747L;

    protected TimePeriodBase() {}

    public TimePeriodBase(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }


    private DateTime start;

    private DateTime end;

    @Override
    public int hashCode() {
        return HashTool.compute(start, end);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("start", start)
                .add("stop", end);
    }
}
