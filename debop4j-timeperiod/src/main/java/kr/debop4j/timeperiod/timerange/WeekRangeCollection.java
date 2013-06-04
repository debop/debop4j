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
import kr.debop4j.timeperiod.TimeCalendar;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 주 단위 {@link WeekRange} 의 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 7:12
 */
public class WeekRangeCollection extends WeekTimeRange {

    private static final long serialVersionUID = 1010532943814278022L;

    /**
     * Instantiates a new Week range collection.
     *
     * @param moment    the moment
     * @param weekCount the week count
     */
    public WeekRangeCollection(DateTime moment, int weekCount) {
        this(moment, weekCount, new TimeCalendar());
    }

    /**
     * Instantiates a new Week range collection.
     *
     * @param moment       the moment
     * @param weekCount    the week count
     * @param timeCalendar the time calendar
     */
    public WeekRangeCollection(DateTime moment, int weekCount, ITimeCalendar timeCalendar) {
        super(moment, weekCount, timeCalendar);
    }

    /**
     * Instantiates a new Week range collection.
     *
     * @param year       the year
     * @param weekOfYear the week of year
     * @param weekCount  the week count
     */
    public WeekRangeCollection(int year, int weekOfYear, int weekCount) {
        super(year, weekOfYear, weekCount);
    }

    /**
     * Instantiates a new Week range collection.
     *
     * @param year         the year
     * @param weekOfYear   the week of year
     * @param weekCount    the week count
     * @param timeCalendar the time calendar
     */
    public WeekRangeCollection(int year, int weekOfYear, int weekCount, ITimeCalendar timeCalendar) {
        super(year, weekOfYear, weekCount, timeCalendar);
    }

    /**
     * Gets weeks.
     *
     * @return the weeks
     */
    public List<WeekRange> getWeeks() {
        DateTime startWeek = getStart();
        List<WeekRange> weeks = Lists.newArrayListWithCapacity(getWeekCount());

        for (int i = 0; i < getWeekCount(); i++) {
            weeks.add(new WeekRange(startWeek.plusWeeks(i), getTimeCalendar()));
        }
        return weeks;
    }
}
