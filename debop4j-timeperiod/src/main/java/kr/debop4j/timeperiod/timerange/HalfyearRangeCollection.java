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
import kr.debop4j.timeperiod.Halfyear;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.YearAndHalfyear;
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 반기 단위 {@link HalfyearRange}의 기간의 컬렉션
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 11:13
 */
public class HalfyearRangeCollection extends HalfyearTimeRange {
    private static final long serialVersionUID = 425689128251222210L;

    public HalfyearRangeCollection(DateTime moment, int halfyearCount) {
        this(moment, halfyearCount, TimeCalendar.getDefault());
    }

    public HalfyearRangeCollection(DateTime moment, int halfyearCount, ITimeCalendar timeCalendar) {
        this(Times.getYearOf(timeCalendar.getYear(moment), timeCalendar.getMonthOfYear(moment)),
             Times.halfyearOf(timeCalendar.getMonthOfYear(moment)),
             halfyearCount,
             timeCalendar);
    }

    public HalfyearRangeCollection(int year, Halfyear halfyear, int halfyearCount) {
        this(year, halfyear, halfyearCount, TimeCalendar.getDefault());
    }

    public HalfyearRangeCollection(int year, Halfyear halfyear, int halfyearCount, ITimeCalendar timeCalendar) {
        super(year, halfyear, halfyearCount, timeCalendar);
    }


    public List<HalfyearRange> getHalfyears() {
        int halfyearCount = getHalfyearCount();
        List<HalfyearRange> halfyears = Lists.newArrayListWithCapacity(halfyearCount);

        for (int hy = 0; hy < halfyearCount; hy++) {
            YearAndHalfyear yhy = Times.addHalfyear(getStartYear(), getStartHalfyear(), hy);
            halfyears.add(new HalfyearRange(yhy.getYear(), yhy.getHalfyear(), getTimeCalendar()));
        }
        return halfyears;
    }
}
