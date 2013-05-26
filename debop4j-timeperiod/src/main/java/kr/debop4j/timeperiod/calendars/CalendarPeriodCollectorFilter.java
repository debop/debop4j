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

package kr.debop4j.timeperiod.calendars;

import com.google.common.collect.Lists;
import kr.debop4j.timeperiod.DayHourRange;
import kr.debop4j.timeperiod.DayRangeInMonth;
import kr.debop4j.timeperiod.HourRangeInDay;
import kr.debop4j.timeperiod.MonthRangeInYear;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * CalendarPeriodCollectorFilter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오후 9:56
 */
@Slf4j
public class CalendarPeriodCollectorFilter extends CalendarVisitorFilter implements ICalendarPeriodCollectionFilter {

    private static final long serialVersionUID = -8493624843659994378L;

    @Getter private final List<MonthRangeInYear> collectingMonths = Lists.newArrayList();
    @Getter private final List<DayRangeInMonth> collectingDays = Lists.newArrayList();
    @Getter private final List<HourRangeInDay> collectingHours = Lists.newArrayList();
    @Getter private final List<DayHourRange> collectingDayHours = Lists.newArrayList();


    @Override
    public synchronized void clear() {
        super.clear();
        collectingMonths.clear();
        collectingDays.clear();
        collectingHours.clear();
        collectingDayHours.clear();
    }
}
