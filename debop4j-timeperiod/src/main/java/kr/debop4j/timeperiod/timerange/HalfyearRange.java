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

import kr.debop4j.timeperiod.HalfyearKind;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.YearAndHalfyear;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.tools.TimeTool;
import org.joda.time.DateTime;

/**
 * 반기를 기간으로 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 11:12
 */
public class HalfyearRange extends HalfyearTimeRange {

    private static final long serialVersionUID = -2193932067151170186L;

    // region << Constructor >>

    public HalfyearRange() {
        this(new TimeCalendar());
    }

    public HalfyearRange(ITimeCalendar calendar) {
        this(ClockProxy.getClock().now().withTimeAtStartOfDay(), calendar);
    }

    public HalfyearRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    public HalfyearRange(DateTime moment, ITimeCalendar calendar) {
        this(TimeTool.getYearOf(calendar.getYear(moment), calendar.getMonthOfYear(moment)),
             TimeTool.getHalfyearOfMonth(moment.getMonthOfYear()), calendar);
    }

    public HalfyearRange(int startYear, HalfyearKind startHalfyear) {
        super(startYear, startHalfyear, 1, new TimeCalendar());
    }

    public HalfyearRange(int startYear, HalfyearKind startHalfyear, ITimeCalendar calendar) {
        super(startYear, startHalfyear, 1, calendar);
    }

    // endregion

    public int getYear() {
        return getStartYear();
    }

    public HalfyearKind getHalfyear() {
        return getStartHalfyear();
    }

    public String getHalfyearName() {
        return getTimeCalendar().getHalfyearName(getHalfyear());
    }

    public String getHalfyearOfYearName() {
        return getTimeCalendar().getHalfyearOfYearName(getYear(), getHalfyear());
    }

    public HalfyearRange previousHalfyear() {
        return addHalfyears(-1);
    }

    public HalfyearRange nextHalfyear() {
        return addHalfyears(1);
    }

    public HalfyearRange addHalfyears(int count) {
        YearAndHalfyear yhy = TimeTool.addHalfyear(getYear(), getHalfyear(), count);
        return new HalfyearRange(yhy.getYear(), yhy.getHalfyear(), getTimeCalendar());
    }
}