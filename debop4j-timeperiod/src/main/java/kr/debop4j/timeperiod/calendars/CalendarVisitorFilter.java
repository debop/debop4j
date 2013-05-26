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

import kr.debop4j.timeperiod.DayOfWeek;
import kr.debop4j.timeperiod.ITimePeriodCollection;
import kr.debop4j.timeperiod.TimePeriodCollection;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Calendar 탐색 시의 필터 정보 (예외 기간, 포함 일자 정보를 가진다)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오전 2:51
 */
@Slf4j
public class CalendarVisitorFilter implements ICalendarVisitorFilter {

    private static final long serialVersionUID = 3032428178497692848L;

    @Getter private final ITimePeriodCollection excludePeriods = new TimePeriodCollection();
    @Getter private final List<Integer> years = new ArrayList<Integer>();
    @Getter private final List<Integer> monthOfYears = new ArrayList<Integer>();
    @Getter private final List<Integer> dayOfMonths = new ArrayList<Integer>();
    @Getter private final List<Integer> hourOfDays = new ArrayList<Integer>();
    @Getter private final List<Integer> minuteOfHours = new ArrayList<Integer>();
    @Getter private final List<DayOfWeek> weekDays = new ArrayList<DayOfWeek>();


    @Override
    public void addWorkingWeekdays() {
        addWeekdays(TimeSpec.Weekdays);
    }

    @Override
    public void addWorkingWeekends() {
        addWeekdays(TimeSpec.Weekends);
    }

    @Override
    public void addWeekdays(DayOfWeek... dayOfWeeks) {
        Collections.addAll(this.weekDays, dayOfWeeks);
    }

    @Override
    public synchronized void clear() {
        years.clear();
        monthOfYears.clear();
        dayOfMonths.clear();
        hourOfDays.clear();
        minuteOfHours.clear();
        weekDays.clear();
    }

    @Override
    public String toString() {
        return Calendars.asString(this);
    }
}
