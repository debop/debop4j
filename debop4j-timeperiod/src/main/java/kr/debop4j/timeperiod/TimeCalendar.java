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

import kr.debop4j.core.NotImplementedException;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * 문화권에 따른 날짜 표현, 날짜 계산 등을 제공하는 Calendar 입니다. (ISO 8601, Korean 등)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:56
 */
@Slf4j
public class TimeCalendar implements ITimeCalendar {
    private static final long serialVersionUID = -8731693901249037388L;

    public static final Duration DefaultStartOffset = TimeSpec.NoDuration;
    public static final Duration DefaultEndOffset = TimeSpec.MinNegativeDuration;

    public static TimeCalendar getDefault() {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar getDefault(Locale locale) {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar getDefault(int yearBaseMonth) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public Locale getLocale() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Duration getStartOffset() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Duration getEndOffset() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DayOfWeek getFirstDayOfWeek() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getYear(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMonthOfYear(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getHourOfDay(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMinuteOfHour(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDayOfMonth(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DayOfWeek getDayOfWeek(DateTime time) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDaysInMonth(int year, int month) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getYearName(int year) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHalfyearName(HalfyearKind halfyear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHalfyearOfYearName(int year, HalfyearKind halfyear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getQuarterName(QuarterKind quarter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getQuarterOfYearName(int year, QuarterKind quarter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMonthName(int month) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMonthOfYearName(int year, int month) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getWeekOfYearName(int year, int weekOfYear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDayName(DayOfWeek dayOfWeek) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getWeekOfYear(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime getStartOfYearWeek(int year, int weekOfYear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime mapStart(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime mapEnd(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime unmapStart(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime unmapEnd(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
