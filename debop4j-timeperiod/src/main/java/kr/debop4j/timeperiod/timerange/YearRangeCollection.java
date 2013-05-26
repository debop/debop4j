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
import kr.debop4j.timeperiod.tools.Times;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 년(Year)단위의 기간들의 컬렉션을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 14. 오전 12:02
 */
public class YearRangeCollection extends YearTimeRange {

    private static final long serialVersionUID = 6717411713272815855L;

    public YearRangeCollection(DateTime moment, int yearCount) {
        this(moment, yearCount, TimeCalendar.getDefault());
    }

    public YearRangeCollection(DateTime moment, int yearCount, ITimeCalendar calendar) {
        this(Times.getYearOf(moment, calendar), yearCount, calendar);
    }

    public YearRangeCollection(int year, int yearCount) {
        this(year, yearCount, TimeCalendar.getDefault());
    }

    public YearRangeCollection(int year, int yearCount, ITimeCalendar calendar) {
        super(year, yearCount, calendar);
    }

    /** Year 단위의 기간들을 열거합니다. */
    public List<YearRange> getYears() {
        int start = getStartYear();

        List<YearRange> years = Lists.newArrayListWithCapacity(getYearCount());
        for (int y = 0; y < getYearCount(); y++)
            years.add(new YearRange(start + y, getTimeCalendar()));

        return years;
    }
}
