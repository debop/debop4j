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
 * 기간의 종류를 나타냅니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 12:02
 */
public enum PeriodUnit {
    /** 년 */
    Year("Year"),
    /** 반기 */
    Halfyear("Halfyear"),
    /** 분기 */
    Quarter("Quarter"),
    /** 월 */
    Month("Month"),
    /** 주 */
    Week("Week"),
    /** 일 */
    Day("Day"),
    /** 시 */
    Hour("Hour"),
    /** 분 */
    Minute("Minute"),
    /** 초 */
    Second("Second"),
    /** 밀리초 */
    Millisecond("Millisecond");

    @Getter private final String periodKind;

    PeriodUnit(String periodKind) {
        this.periodKind = periodKind;
    }
}
