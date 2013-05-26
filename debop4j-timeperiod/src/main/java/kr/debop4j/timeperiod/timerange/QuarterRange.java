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
import kr.debop4j.timeperiod.Quarter;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.YearAndQuarter;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.test.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * 한 분기를 나타내는 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 10:09
 */
@Slf4j
public class QuarterRange extends QuarterTimeRange {

    // region << Constructor >>

    public QuarterRange() {
        this(new TimeCalendar());
    }

    public QuarterRange(ITimeCalendar calendar) {
        this(ClockProxy.getClock().now().withTimeAtStartOfDay(), calendar);
    }

    public QuarterRange(DateTime moment) {
        this(moment, new TimeCalendar());
    }

    public QuarterRange(DateTime moment, ITimeCalendar calendar) {
        this(Times.getYearOf(calendar.getYear(moment), calendar.getMonthOfYear(moment)),
             Times.getQuarterOfMonth(moment.getMonthOfYear()), calendar);
    }

    public QuarterRange(int startYear, Quarter startQuarter) {
        super(startYear, startQuarter, 1);
    }

    public QuarterRange(int startYear, Quarter startQuarter, ITimeCalendar calendar) {
        super(startYear, startQuarter, 1, calendar);
    }

    // endregion

    public int getYeaer() {
        return getStartYear();
    }

    public Quarter getQuarter() {
        return getStartQuarter();
    }

    public QuarterRange previousQuarter() {
        return addQuarters(-1);
    }

    public QuarterRange nextQuarter() {
        return addQuarters(1);
    }

    public QuarterRange addQuarters(int quarters) {
        YearAndQuarter yq = Times.addQuarter(getStartYear(), getStartQuarter(), quarters);
        return new QuarterRange(yq.getYear(), yq.getQuarter(), getTimeCalendar());
    }

    private static final long serialVersionUID = -5373404703149628573L;
}
