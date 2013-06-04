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
 * 월 단위 {@link MonthRange} 의 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 8:16
 */
public class MonthRangeCollection extends MonthTimeRange {

    private static final long serialVersionUID = -3955343194292107018L;

    /**
     * Instantiates a new Month range collection.
     *
     * @param moment     the moment
     * @param monthCount the month count
     */
    public MonthRangeCollection(DateTime moment, int monthCount) {
        super(moment, monthCount);
    }

    /**
     * Instantiates a new Month range collection.
     *
     * @param moment     the moment
     * @param monthCount the month count
     * @param calendar   the calendar
     */
    public MonthRangeCollection(DateTime moment, int monthCount, ITimeCalendar calendar) {
        super(moment, monthCount, calendar);
    }

    /**
     * Instantiates a new Month range collection.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @param monthCount  the month count
     */
    public MonthRangeCollection(int year, int monthOfYear, int monthCount) {
        super(year, monthOfYear, monthCount);
    }

    /**
     * Instantiates a new Month range collection.
     *
     * @param year        the year
     * @param monthOfYear the month of year
     * @param monthCount  the month count
     * @param calendar    the calendar
     */
    public MonthRangeCollection(int year, int monthOfYear, int monthCount, ITimeCalendar calendar) {
        super(year, monthOfYear, monthCount, calendar);
    }

    /**
     * Gets months.
     *
     * @return the months
     */
    public List<MonthRange> getMonths() {
        DateTime startTime = this.getStart();
        List<MonthRange> months = Lists.newArrayListWithCapacity(getMonthCount());

        for (int m = 0; m < getMonthCount(); m++) {
            months.add(new MonthRange(startTime.plusMonths(m), getTimeCalendar()));
        }
        return months;
    }
}
