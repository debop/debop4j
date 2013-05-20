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
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

import static kr.debop4j.core.Guard.*;

/**
 * 문화권에 따른 날짜 표현, 날짜 계산 등을 제공하는 Calendar 입니다. (ISO 8601, Korean 등)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:56
 */
@Slf4j
public class TimeCalendar implements ITimeCalendar {

    public static final Duration DefaultStartOffset = TimeSpec.NoDuration;
    public static final Duration DefaultEndOffset = TimeSpec.MinNegativeDuration;

    // region << Static Methods >>

    public static TimeCalendar getDefault() {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar getDefault(Locale locale) {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar getDefault(int yearBaseMonth) {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar getEmptyOffset() {
        throw new NotImplementedException("구현 중");
    }

    // endregion

    @Getter private final Locale locale;
    @Getter private final Duration startOffset;
    @Getter private final Duration endOffset;
    @Getter private final DayOfWeek firstDayOfWeek;


    public TimeCalendar() {
        this(new TimeCalendarConfig());
    }

    public TimeCalendar(TimeCalendarConfig config) {
        shouldNotBeNull(config, "config");
        if (config.getStartOffset() != null)
            shouldBe(config.getStartOffset().compareTo(Duration.ZERO) >= 0, "startOffset should be positive or zero");
        if (config.getEndOffset() != null)
            shouldBe(config.getEndOffset().compareTo(Duration.ZERO) >= 0, "endOffset should be positive or zero.");

        this.locale = firstNotNull(config.getLocale(), Locale.getDefault());
        this.startOffset = firstNotNull(config.getStartOffset(), DefaultStartOffset);
        this.endOffset = firstNotNull(config.getEndOffset(), DefaultEndOffset);
        this.firstDayOfWeek = config.getFirstDayOfWeek();
    }

    @Override
    public int getYear(DateTime time) {
        return Times.getYearOf(time, this);
    }

    @Override
    public int getMonthOfYear(DateTime time) {
        return time.getMonthOfYear();
    }

    @Override
    public int getHourOfDay(DateTime time) {
        return time.getHourOfDay();
    }

    @Override
    public int getMinuteOfHour(DateTime time) {
        return time.getMinuteOfHour();
    }

    @Override
    public int getDayOfMonth(DateTime time) {
        return time.getDayOfMonth();
    }

    @Override
    public DayOfWeek getDayOfWeek(DateTime time) {
        return DayOfWeek.valueOf(time.getDayOfWeek());
    }

    @Override
    public int getDaysInMonth(int year, int month) {
        return Times.getDaysInMonth(year, month);
    }

    @Override
    public int getWeekOfYear(DateTime time) {
        return Times.getWeekOfYear(time).getWeekOfYear();
    }

    @Override
    public DateTime getStartOfYearWeek(int year, int weekOfYear) {
        return Times.getStartOfYearWeek(year, weekOfYear);
    }

    @Override
    public DateTime mapStart(DateTime moment) {
        shouldNotBeNull(moment, "moment");
        return moment.compareTo(TimeSpec.MinPeriodTime) > 0 ? moment.plus(startOffset) : moment;
    }

    @Override
    public DateTime mapEnd(DateTime moment) {
        shouldNotBeNull(moment, "moment");
        return moment.compareTo(TimeSpec.MaxPeriodTime) < 0 ? moment.plus(endOffset) : moment;
    }

    @Override
    public DateTime unmapStart(DateTime moment) {
        shouldNotBeNull(moment, "moment");
        return moment.compareTo(TimeSpec.MinPeriodTime) > 0 ? moment.minus(startOffset) : moment;
    }

    @Override
    public DateTime unmapEnd(DateTime moment) {
        shouldNotBeNull(moment, "moment");
        return moment.compareTo(TimeSpec.MaxPeriodTime) < 0 ? moment.minus(endOffset) : moment;
    }

    private static final long serialVersionUID = -8731693901249037388L;
}
