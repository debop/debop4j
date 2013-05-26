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

import lombok.Getter;
import lombok.Setter;

/**
 * 칼렌다 기간 조회를 수행할 때의 범위를 나타냅니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오후 10:03
 */
public class CalendarPeriodCollectorContext implements ICalendarVisitorContext {

    private static final long serialVersionUID = -673206065311487129L;

    @Getter
    @Setter
    public CollectKind scope;

    public enum CollectKind {
        Year(0),
        Month(1),
        Day(2),
        Hour(3),
        Minute(4);

        @Getter private final int value;

        CollectKind(int value) {
            this.value = value;
        }

        public static CollectKind valueOf(int value) {
            switch (value) {
                case 0:
                    return Year;
                case 1:
                    return Month;
                case 2:
                    return Day;
                case 3:
                    return Hour;
                case 4:
                    return Minute;
                default:
                    throw new IllegalArgumentException("not supported value. value=" + value);
            }
        }
    }
}
