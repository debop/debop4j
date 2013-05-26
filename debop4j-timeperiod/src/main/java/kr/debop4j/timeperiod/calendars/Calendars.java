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

import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;

/**
 * Calendars
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오전 2:59
 */
@Slf4j
public abstract class Calendars {

    private Calendars() {}

    public static String asString(ICalendarVisitorFilter filter) {
        if (filter == null)
            return "Filter is null";

        StringBuilder builder = new StringBuilder();
        builder.append("CalendarVisitorFilter#\n");
        builder.append("----------------------");
        builder.append("\n\t years=").append(StringTool.listToString(filter.getYears()));
        builder.append("\n\t monthOfYears=").append(StringTool.listToString(filter.getMonthOfYears()));
        builder.append("\n\t monthOfYears=").append(StringTool.listToString(filter.getMonthOfYears()));
        builder.append("\n\t dayOfMonths=").append(StringTool.listToString(filter.getDayOfMonths()));
        builder.append("\n\t hourOfDays=").append(StringTool.listToString(filter.getHourOfDays()));
        builder.append("\n\t minuteOfHours=").append(StringTool.listToString(filter.getMinuteOfHours()));
        builder.append("\n\t dayOfWeeks=").append(StringTool.listToString(filter.getWeekDays()));
        builder.append("\n\t exclude periods=").append(StringTool.listToString(filter.getExcludePeriods()));
        builder.append("----------------------");

        return builder.toString();
    }
}
