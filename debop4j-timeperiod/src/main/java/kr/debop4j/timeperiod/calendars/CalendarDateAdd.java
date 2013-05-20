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

import kr.debop4j.timeperiod.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 특정 Calendar 기준으로 특정 시각과 기간(Duration)을 이용하여 상대 시각을 구합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오후 9:36
 */
@Slf4j
public class CalendarDateAdd extends DateAdd {

    @Getter private final List<DayOfWeek> weekDays = new ArrayList<>();
    @Getter private final List<HourRangeInDay> workingHours = new ArrayList<>();
    @Getter private final List<DayHourRange> workingDayHours = new ArrayList<>();

    public CalendarDateAdd() {
        this(TimeCalendar.create());
    }

    public CalendarDateAdd(ITimeCalendar calendar) {

    }


    private static final long serialVersionUID = -2499923637191503226L;
}
