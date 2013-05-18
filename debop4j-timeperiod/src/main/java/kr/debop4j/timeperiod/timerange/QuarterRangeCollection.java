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
import kr.debop4j.timeperiod.QuarterKind;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.YearAndQuarter;
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

import java.util.List;

/**
 * kr.debop4j.timeperiod.timerange.QuarterRangeCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 10:19
 */
public class QuarterRangeCollection extends QuarterTimeRange {

    private static final long serialVersionUID = -1191375103809489196L;

    public QuarterRangeCollection(DateTime moment, int quarterCount) {
        this(moment, quarterCount, new TimeCalendar());
    }

    public QuarterRangeCollection(DateTime moment, int quarterCount, ITimeCalendar timeCalendar) {
        this(Times.getYearOf(timeCalendar.getYear(moment), timeCalendar.getMonthOfYear(moment)),
             Times.quarterOf(timeCalendar.getMonthOfYear(moment)),
             quarterCount,
             timeCalendar);
    }

    public QuarterRangeCollection(int year, QuarterKind quarter, int quarterCount) {
        this(year, quarter, quarterCount, new TimeCalendar());
    }

    public QuarterRangeCollection(int year, QuarterKind quarter, int quarterCount, ITimeCalendar timeCalendar) {
        super(year, quarter, quarterCount, timeCalendar);
    }


    public List<QuarterRange> getQuarters() {
        int baseYear = getStartYear();
        QuarterKind baseQuarter = getStartQuarter();

        List<QuarterRange> quarters = Lists.newArrayListWithCapacity(getQuarterCount());
        for (int q = 0; q < getQuarterCount(); q++) {
            YearAndQuarter yq = Times.addQuarter(getStartYear(), getStartQuarter(), q);
            quarters.add(new QuarterRange(yq.getYear(), yq.getQuarter(), getTimeCalendar()));
        }
        return quarters;
    }
}
