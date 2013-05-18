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
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

import java.util.List;

/**
 * kr.debop4j.timeperiod.timerange.HourRangeCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오전 11:26
 */
public class HourRangeCollection extends HourTimeRange {

    private static final long serialVersionUID = 8973240176036662074L;

    public HourRangeCollection(DateTime moment, int hourCount) {
        super(moment, hourCount);
    }

    public HourRangeCollection(DateTime moment, int hourCount, ITimeCalendar calendar) {
        super(moment, hourCount, calendar);
    }

    public HourRangeCollection(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int hourCount) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, hourCount);
    }

    public HourRangeCollection(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int hourCount, ITimeCalendar calendar) {
        super(year, monthOfYear, dayOfMonth, hourOfDay, hourCount, calendar);
    }

    public List<HourRange> getHours() {
        DateTime startHour = Times.trimToHour(getStart(), getStartHourOfDay());

        List<HourRange> hours = Lists.newArrayListWithCapacity(getHourCount());
        for (int h = 0; h < getHourCount(); h++)
            hours.add(new HourRange(startHour.plusHours(h), getTimeCalendar()));

        return hours;
    }
}