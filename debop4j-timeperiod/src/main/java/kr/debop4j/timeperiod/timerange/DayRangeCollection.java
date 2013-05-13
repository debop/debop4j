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

import com.google.common.collect.Lists;
import kr.debop4j.timeperiod.ITimeCalendar;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 일 단위 기간({@link DayRange})의 컬렉션에 해당합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 2:18
 */
public class DayRangeCollection extends DayTimeRange {

    private static final long serialVersionUID = -6795227568039667626L;

    // region << Constructor >>

    public DayRangeCollection(DateTime moment, int dayCount) {
        super(moment, dayCount);
    }

    public DayRangeCollection(DateTime moment, int dayCount, ITimeCalendar calendar) {
        super(moment, dayCount, calendar);
    }

    public DayRangeCollection(int year, int monthOfYear, int dayOfMonth, int dayCount) {
        super(year, monthOfYear, dayOfMonth, dayCount);
    }

    public DayRangeCollection(int year, int monthOfYear, int dayOfMonth, int dayCount, ITimeCalendar calendar) {
        super(year, monthOfYear, dayOfMonth, dayCount, calendar);
    }

    // endregion

    public List<DayRange> getDays() {
        DateTime startDay = getStart();

        List<DayRange> days = Lists.newArrayListWithCapacity(getDayCount());
        for (int d = 0; d < getDayCount(); d++) {
            days.add(new DayRange(startDay.plusDays(d), getTimeCalendar()));
        }
        return days;
    }
}
