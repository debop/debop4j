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

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * 문화권에 따른 날짜 표현, 날짜 계산 등을 제공하는 Calendar 입니다. (ISO 8601, Korean 등)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 6:42
 */
public interface ITimeCalendar extends ITimePeriodMapper {

    /**
     * 문화권 정보 (문화권에 따라 달력에 대한 규칙 및 명칭이 달라집니다.)
     */
    Locale getLocale();

    /**
     * 시작 오프셋 (시작일자가 1월 1일이 아닌 경우)
     */
    Duration getStartOffset();

    /**
     * 종료 오프셋
     */
    Duration getEndOffset();

    /**
     * 한 주의 시작 요일 (한국, 미국: Sunday, ISO-8601: Monday)
     */
    DayOfWeek getFirstDayOfWeek();

    /**
     * 지정된 일자의 년
     */
    int getYear(DateTime time);

    /**
     * 지정된 일자의 월
     */
    int getMonthOfYear(DateTime time);

    /**
     * 지정된 시각의 시간
     */
    int getHourOfDay(DateTime time);

    /**
     * 지정된 시각의 분
     */
    int getMinuteOfHour(DateTime time);

    /**
     * 지정된 날짜의 월 몇번째 일인지
     */
    int getDayOfMonth(DateTime time);

    /**
     * 지정된 날짜의 요일
     */
    DayOfWeek getDayOfWeek(DateTime time);

    /**
     * 지정된 년,월의 날짜수
     */
    int getDaysInMonth(int year, int month);

    /**
     * 지정된 일자의 주차(Week of Year)를 반환합니다.
     */
    int getWeekOfYear(DateTime time);

    /**
     * 지정된 년, 주차에 해당하는 주의 첫번째 일자를 반환한다. (예: 2011년 3주차의 첫번째 일자는?)
     */
    DateTime getStartOfYearWeek(int year, int weekOfYear);
}
