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

package kr.debop4j.timeperiod;

import kr.debop4j.core.Local;

import java.util.Locale;

/**
 * 현재 Thread 하에서 TimePeriod 관련하여 제공할 정보를 제공합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:49
 */
public class PeriodContext {

    private static final String TIME_CALENDAR_KEY = PeriodContext.class.getName() + ".Current";

    /**
     * 현 Thread Context 하에서 설정된 TimeCalendar 관련 설정 정보
     */
    public static class Current {

        /**
         * 현재 Thread Context하에서 사용할 TimeCalendar입니다.
         */
        public static ITimeCalendar getTimeCalendar() {
            ITimeCalendar calendar = Local.get(TIME_CALENDAR_KEY, ITimeCalendar.class);
            if (calendar == null) {
                calendar = TimeCalendar.getDefault();
                Local.put(TIME_CALENDAR_KEY, calendar);
            }
            return calendar;
        }

        public static void setTimeCalendar(ITimeCalendar calendar) {
            Local.put(TIME_CALENDAR_KEY, calendar);
        }

        /**
         * 현 Thread context의 {@link Locale} 정보
         */
        public static Locale getLocale() {
            return getTimeCalendar().getLocale();
        }

        /**
         * 한 주의 첫번째 요일
         */
        public static DayOfWeek getFirstDayOfWeek() {
            return getTimeCalendar().getFirstDayOfWeek();
        }
    }
}
