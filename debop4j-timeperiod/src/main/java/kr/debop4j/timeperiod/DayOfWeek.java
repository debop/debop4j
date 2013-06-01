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

import lombok.Getter;

/**
 * 한주를 구성하는 요일을 나타냅니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:42
 */
public enum DayOfWeek {

    /** The Monday. */Monday(1),
    /** The ThuesDay. */ThuesDay(2),
    /** The WednesDay. */WednesDay(3),
    /** The ThursDay. */ThursDay(4),
    /** The FriDay. */FriDay(5),
    /** The Saturday. */Saturday(6),
    /** The Sunday. */Sunday(7);

    /** 요일의 Int형 값을 반환한다 */
    @Getter private final int value;

    DayOfWeek(int value) {
        this.value = value;
    }

    /**
     * Integer 값으로 요일을 반환한다.
     *
     * @param dayOfWeek the day of week
     * @return the day of week
     */
    public static DayOfWeek valueOf(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return Monday;
            case 2:
                return ThuesDay;
            case 3:
                return WednesDay;
            case 4:
                return ThursDay;
            case 5:
                return FriDay;
            case 6:
                return Saturday;
            case 7:
                return Sunday;
        }
        throw new IllegalArgumentException("요일에 해당하는 숫자가 아닙니다. (1~7), dayOfWeek=" + dayOfWeek);
    }
}
