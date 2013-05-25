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

package kr.debop4j.timeperiod.timerange;

import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.TimeCalendar;

/**
 * kr.debop4j.timeperiod.timerange.YearCalendarTimeRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 8:27
 */
public abstract class YearCalendarTimeRange extends CalendarTimeRange {

    private static final long serialVersionUID = -7922671338410846872L;

    public YearCalendarTimeRange(ITimePeriod period, ITimeCalendar calendar) {
        super(period, (calendar != null) ? calendar : TimeCalendar.getDefault());
    }

    /** TimeCalendar의 시작 월 */
    public int getYearBaseMonth() {
        return 1;
    }

    /** Calendar의 시작 년(Year) */
    public int getBaseYear() {
        return getStartYear();
    }
}
