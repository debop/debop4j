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

import java.util.EnumSet;

/**
 * 기간에 대한 여러가지 복합체를 만들어 냅니다. {@link PeriodUnit} 와는 달리 Bit 연산을 수행합니다.<br/>
 * Gantt Chart 등에서 기간을 축 (Axis) 로 나타낼때, 여러 단위로 지정할 때 사용합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 10:52
 */
public class PeriodFlag {

    /** None */
    public static final EnumSet<Flag> None = EnumSet.of(Flag.None);
    /** 년 */
    public static final EnumSet<Flag> Year = EnumSet.of(Flag.Year);
    /** 반기 */
    public static final EnumSet<Flag> Halfyear = EnumSet.of(Flag.Halfyear);
    /** 분기 */
    public static final EnumSet<Flag> Quarter = EnumSet.of(Flag.Quarter);
    /** 월 */
    public static final EnumSet<Flag> Month = EnumSet.of(Flag.Month);
    /** 주 */
    public static final EnumSet<Flag> Week = EnumSet.of(Flag.Week);
    /** 일 */
    public static final EnumSet<Flag> Day = EnumSet.of(Flag.Day);
    /** 시 */
    public static final EnumSet<Flag> Hour = EnumSet.of(Flag.Hour);

    /** 년/월 */
    public static final EnumSet<Flag> YearMonth = EnumSet.of(Flag.Year, Flag.Month);
    /** 년/월/일 */
    public static final EnumSet<Flag> YearMonthDay = EnumSet.of(Flag.Year, Flag.Month, Flag.Day);
    /** 년/월/일/시 */
    public static final EnumSet<Flag> YearMonthDayHour = EnumSet.of(Flag.Year, Flag.Month, Flag.Day, Flag.Hour);

    /** 년/분기 */
    public static final EnumSet<Flag> YearQuarter = EnumSet.of(Flag.Year, Flag.Quarter);
    /** 년/분기/월 */
    public static final EnumSet<Flag> YearQuarterMonth = EnumSet.of(Flag.Year, Flag.Quarter, Flag.Month);
    /** 년/분기/월/일 */
    public static final EnumSet<Flag> YearQuarterMonthDay = EnumSet.of(Flag.Year, Flag.Quarter, Flag.Month, Flag.Day);

    /** 년/주 */
    public static final EnumSet<Flag> YearWeek = EnumSet.of(Flag.Year, Flag.Week);
    /** 년/주/일 */
    public static final EnumSet<Flag> YearWeekDay = EnumSet.of(Flag.Year, Flag.Week, Flag.Day);
    /** 년/주/일/시 */
    public static final EnumSet<Flag> YearWeekDayHour = EnumSet.of(Flag.Year, Flag.Week, Flag.Day, Flag.Hour);

    /** 월/일 */
    public static final EnumSet<Flag> MonthDay = EnumSet.of(Flag.Month, Flag.Day);
    /** 월/일/시 */
    public static final EnumSet<Flag> MonthDayHour = EnumSet.of(Flag.Month, Flag.Day, Flag.Hour);

    public enum Flag {

        None(0x00),
        Year(0x01),
        Halfyear(0x02),
        Quarter(0x04),
        Month(0x08),
        Week(0x10),
        Day(0x20),
        Hour(0x40);

        private final int flag;

        Flag(int flag) {
            this.flag = flag;
        }
    }
}
