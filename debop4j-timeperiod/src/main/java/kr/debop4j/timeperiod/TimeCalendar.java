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

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.test.tools.TimeSpec;
import kr.debop4j.timeperiod.test.tools.Times;
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
public class TimeCalendar extends ValueObjectBase implements ITimeCalendar {

    public static final Duration DefaultStartOffset = TimeSpec.EmptyDuration;
    public static final Duration DefaultEndOffset = TimeSpec.MinNegativeDuration;

    // region << Static Methods >>

    /** 기본 {@link TimeCalendar} 를 반환합니다. */
    public static TimeCalendar getDefault() {
        return getDefault(Locale.getDefault());
    }

    /** 기본 {@link TimeCalendar} 를 반환합니다. */
    public static TimeCalendar getDefault(Locale locale) {
        TimeCalendarConfig config = new TimeCalendarConfig(locale);
        config.setStartOffset(DefaultStartOffset);
        config.setEndOffset(DefaultEndOffset);

        return new TimeCalendar(config);
    }

    /** Offset이 없는 {@link TimeCalendar}를 반환합니다. */
    public static TimeCalendar getEmptyOffset() {
        return getEmptyOffset(Locale.getDefault());
    }

    /** Offset이 없는 {@link TimeCalendar}를 반환합니다. */
    public static TimeCalendar getEmptyOffset(Locale locale) {
        TimeCalendarConfig config = new TimeCalendarConfig(locale);
        config.setStartOffset(TimeSpec.EmptyDuration);
        config.setEndOffset(TimeSpec.EmptyDuration);

        return new TimeCalendar(config);
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
            shouldBePositiveOrZeroNumber(config.getStartOffset().getMillis(), "startOffset");
        if (config.getEndOffset() != null)
            shouldBeNegativeOrZeroNumber(config.getEndOffset().getMillis(), "endOffset");

        this.locale = firstNotNull(config.getLocale(), Locale.getDefault());
        this.startOffset = firstNotNull(config.getStartOffset(), DefaultStartOffset);
        this.endOffset = firstNotNull(config.getEndOffset(), DefaultEndOffset);
        this.firstDayOfWeek = config.getFirstDayOfWeek();
    }

    @Override
    public int getYear(DateTime time) {
        return time.getYear();
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

    @Override
    public int hashCode() {
        return HashTool.compute(locale, startOffset, endOffset, firstDayOfWeek);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("locale", locale)
                .add("startOffset", startOffset)
                .add("endOffset", endOffset)
                .add("firstDayOfWeek", firstDayOfWeek);
    }

    private static final long serialVersionUID = -8731693901249037388L;
}
