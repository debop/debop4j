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

package kr.debop4j.timeperiod.test.calendars;

import kr.debop4j.timeperiod.DayOfWeek;
import kr.debop4j.timeperiod.ITimePeriodCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Calendar 탐색 시의 필터 정보 (예외 기간, 포함 일자 정보를 가진다)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오전 1:37
 */
public interface ICalendarVisitorFilter extends Serializable {

    /** 탐색 시 제외해야 할 기간들 */
    ITimePeriodCollection getExcludePeriods();

    /** 포함 년도 */
    List<Integer> getYears();

    /** 포함 월 */
    List<Integer> getMonthOfYears();

    /** 포함 일 */
    List<Integer> getDayOfMonths();

    /** 포함 요일 */
    List<DayOfWeek> getWeekDays();

    /** 포함 시 (Hour) */
    List<Integer> getHourOfDays();

    /** 포함 분(Minutes) */
    List<Integer> getMinuteOfHours();

    /** 주중 (월~금) 을 Working Day로 추가합니다. */
    void addWorkingWeekdays();

    /** 주말 (토,일) 을 Working Day로 추가합니다. */
    void addWorkingWeekends();

    /** 지정한 요일들을 탐색 필터에 포함시킨다. */
    void addWeekdays(DayOfWeek... dayOfWeeks);

    /** 탐색 필터 및 예외 필터에 등록된 모든 내용을 삭제합니다. */
    void clear();
}
