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

package kr.debop4j.timeperiod.calendars.seeker;

import kr.debop4j.core.Guard;
import kr.debop4j.timeperiod.calendars.ICalendarVisitorContext;
import kr.debop4j.timeperiod.timerange.DayRange;
import lombok.Getter;

import java.io.Serializable;

/**
 * DaySeekerContext
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오후 8:47
 */
public class DaySeekerContext implements ICalendarVisitorContext, Serializable {

    @Getter private final DayRange startDay;
    @Getter private final int dayCount;

    @Getter private int remainingDays;
    @Getter private DayRange foundDay;


    public DaySeekerContext(DayRange startDay, int dayCount) {
        Guard.shouldNotBeNull(startDay, "startDay");

        this.startDay = startDay;
        this.dayCount = Math.abs(dayCount);
        remainingDays = this.dayCount;
    }

    public boolean isFinished() {
        return remainingDays == 0;
    }

    public void processDay(DayRange day) {
        if (isFinished())
            return;

        remainingDays--;

        if (isFinished())
            foundDay = day;
    }

    private static final long serialVersionUID = 6019187174698832520L;
}
