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

package kr.debop4j.timeperiod.timerange;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.test.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * kr.debop4j.timeperiod.timerange.ICalendarITimeRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 12:01
 */
@Slf4j
public class CalendarTimeRange extends TimeRange implements ICalendarTimeRange {

    private static final long serialVersionUID = -422889827258277497L;

    public static TimeRange toCalendarTimeRange(ITimePeriod period, ITimePeriodMapper mapper) {
        assert period != null;
        if (mapper == null) mapper = TimeCalendar.getDefault();

        DateTime mappedStart = mapper.mapStart(period.getStart());
        DateTime mappedEnd = mapper.mapEnd(period.getEnd());

        Times.assertValidPeriod(mappedStart, mappedEnd);
        TimeRange mapped = new TimeRange(mappedStart, mappedEnd);

//        if (log.isTraceEnabled())
//            log.trace("TimeCalendar 기준의 기간으로 매핑했습니다. period=[{}], mapper=[{}], mapped=[{}]", period, mapper, mapped);
        return mapped;
    }

    // region << Constructor >>

    public CalendarTimeRange(DateTime start, DateTime end) {
        this(start, end, new TimeCalendar());
    }

    public CalendarTimeRange(DateTime start, DateTime end, ITimeCalendar timeCalendar) {
        this(new TimeRange(start, end), timeCalendar);
    }

    public CalendarTimeRange(DateTime start, Duration duration) {
        this(start, duration, new TimeCalendar());
    }

    public CalendarTimeRange(DateTime start, Duration duration, ITimeCalendar timeCalendar) {
        this(new TimeRange(start, duration), timeCalendar);
    }

    public CalendarTimeRange(ITimePeriod period) {
        this(period, new TimeCalendar());
    }

    public CalendarTimeRange(ITimePeriod period, ITimeCalendar timeCalendar) {
        super(toCalendarTimeRange(period, timeCalendar), true);
        this.timeCalendar = (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault();
    }

    // endregion

    @Getter
    private final ITimeCalendar timeCalendar;

    public int getStartYear() { return getStart().getYear(); }

    public int getStartMonthOfYear() { return getStart().getMonthOfYear(); }

    public int getStartDayOfMonth() { return getStart().getDayOfMonth(); }

    public int getStartHourOfDay() { return getStart().getHourOfDay(); }

    public int getStartMinuteOfHour() { return getStart().getMinuteOfHour(); }

    public int getEndYear() { return getEnd().getYear(); }

    public int getEndMonthOfYear() { return getEnd().getMonthOfYear(); }

    public int getEndDayOfMonth() { return getEnd().getDayOfMonth(); }

    public int getEndHourOfDay() { return getEnd().getHourOfDay(); }

    public int getEndMinuteOfHour() { return getEnd().getMinuteOfHour(); }

    public DateTime getMappedStart() {
        return timeCalendar.mapStart(getStart());
    }

    public DateTime getMappedEnd() {
        return timeCalendar.mapEnd(getEnd());
    }

    public DateTime getUnmappedStart() {
        return timeCalendar.unmapStart(getStart());
    }

    public DateTime getUnmappedEnd() {
        return timeCalendar.unmapEnd(getEnd());
    }

    public DateTime getStartMonthStart() {
        return Times.trimToDay(getStart());
    }

    public DateTime getEndMonthStart() {
        return Times.trimToDay(getEnd());
    }

    public DateTime getStartDayStart() {
        return Times.trimToHour(getStart());
    }

    public DateTime getEndDayStart() {
        return Times.trimToHour(getEnd());
    }

    public DateTime getStartHourStart() {
        return Times.trimToMinute(getStart());
    }

    public DateTime getEndHourStart() {
        return Times.trimToMinute(getEnd());
    }

    public DateTime getStartMinuteStart() {
        return Times.trimToSecond(getStart());
    }

    public DateTime getEndMinuteStart() {
        return Times.trimToSecond(getEnd());
    }

    public DateTime getStartSecondStart() {
        return Times.trimToMillis(getStart());
    }

    public DateTime getEndSecondStart() {
        return Times.trimToMillis(getEnd());
    }

    @Override
    public TimeRange copy(Duration offset) {
        return toCalendarTimeRange(super.copy(offset), timeCalendar);
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), timeCalendar);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("timeCalendar", timeCalendar);
    }
}
