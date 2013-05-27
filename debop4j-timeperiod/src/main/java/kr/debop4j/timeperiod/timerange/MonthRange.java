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
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

/**
 * 1개월의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 8:16
 */
public class MonthRange extends MonthTimeRange {

    // region << Constructor >>

    /** 생성자 */
    public MonthRange() {
        this(new TimeCalendar());
    }

    /** 생성자 */
    public MonthRange(ITimeCalendar calendar) {
        this(ClockProxy.getClock().today(), calendar);
    }

    /** 생성자 */
    public MonthRange(DateTime moment) {
        super(moment, 1);
    }

    /** 생성자 */
    public MonthRange(DateTime moment, ITimeCalendar calendar) {
        super(moment, 1, calendar);
    }

    /** 생성자 */
    public MonthRange(int year, int monthOfYear) {
        super(year, monthOfYear, 1);
    }

    /** 생성자 */
    public MonthRange(int year, int monthOfYear, ITimeCalendar calendar) {
        super(year, monthOfYear, 1, calendar);
    }

    // endregion

    public int getYear() { return getStartYear(); }

    public int getMonthOfYear() { return getStartMonthOfYear(); }

    public int getDaysInMonth() {
        return Times.getDaysInMonth(getStartYear(), getStartMonthOfYear());
    }

    public MonthRange previousMonth() {
        return addMonths(-1);
    }

    public MonthRange nextMonth() {
        return addMonths(1);
    }

    public MonthRange addMonths(int months) {
        return new MonthRange(Times.startTimeOfMonth(getStart()).plusMonths(months), getTimeCalendar());
    }

    private static final long serialVersionUID = 6337203416072219224L;
}
