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
import kr.debop4j.timeperiod.tools.Times;
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

    /**
     * To calendar time range.
     *
     * @param period the period
     * @param mapper the mapper
     * @return the time range
     */
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

    /**
     * Instantiates a new Calendar time range.
     *
     * @param start the start
     * @param end   the end
     */
    public CalendarTimeRange(DateTime start, DateTime end) {
        this(start, end, new TimeCalendar());
    }

    /**
     * Instantiates a new Calendar time range.
     *
     * @param start        the start
     * @param end          the end
     * @param timeCalendar the time calendar
     */
    public CalendarTimeRange(DateTime start, DateTime end, ITimeCalendar timeCalendar) {
        this(new TimeRange(start, end), timeCalendar);
    }

    /**
     * Instantiates a new Calendar time range.
     *
     * @param start    the start
     * @param duration the duration
     */
    public CalendarTimeRange(DateTime start, Duration duration) {
        this(start, duration, new TimeCalendar());
    }

    /**
     * Instantiates a new Calendar time range.
     *
     * @param start        the start
     * @param duration     the duration
     * @param timeCalendar the time calendar
     */
    public CalendarTimeRange(DateTime start, Duration duration, ITimeCalendar timeCalendar) {
        this(new TimeRange(start, duration), timeCalendar);
    }

    /**
     * Instantiates a new Calendar time range.
     *
     * @param period the period
     */
    public CalendarTimeRange(ITimePeriod period) {
        this(period, new TimeCalendar());
    }

    /**
     * Instantiates a new Calendar time range.
     *
     * @param period       the period
     * @param timeCalendar the time calendar
     */
    public CalendarTimeRange(ITimePeriod period, ITimeCalendar timeCalendar) {
        super(toCalendarTimeRange(period, timeCalendar), true);
        this.timeCalendar = (timeCalendar != null) ? timeCalendar : TimeCalendar.getDefault();
    }

    // endregion

    @Getter
    private final ITimeCalendar timeCalendar;

    /**
     * Gets start year.
     *
     * @return the start year
     */
    public int getStartYear() { return getStart().getYear(); }

    /**
     * Gets start month of year.
     *
     * @return the start month of year
     */
    public int getStartMonthOfYear() { return getStart().getMonthOfYear(); }

    /**
     * Gets start day of month.
     *
     * @return the start day of month
     */
    public int getStartDayOfMonth() { return getStart().getDayOfMonth(); }

    /**
     * Gets start hour of day.
     *
     * @return the start hour of day
     */
    public int getStartHourOfDay() { return getStart().getHourOfDay(); }

    /**
     * Gets start minute of hour.
     *
     * @return the start minute of hour
     */
    public int getStartMinuteOfHour() { return getStart().getMinuteOfHour(); }

    /**
     * Gets end year.
     *
     * @return the end year
     */
    public int getEndYear() { return getEnd().getYear(); }

    /**
     * Gets end month of year.
     *
     * @return the end month of year
     */
    public int getEndMonthOfYear() { return getEnd().getMonthOfYear(); }

    /**
     * Gets end day of month.
     *
     * @return the end day of month
     */
    public int getEndDayOfMonth() { return getEnd().getDayOfMonth(); }

    /**
     * Gets end hour of day.
     *
     * @return the end hour of day
     */
    public int getEndHourOfDay() { return getEnd().getHourOfDay(); }

    /**
     * Gets end minute of hour.
     *
     * @return the end minute of hour
     */
    public int getEndMinuteOfHour() { return getEnd().getMinuteOfHour(); }

    /**
     * Gets mapped start.
     *
     * @return the mapped start
     */
    public DateTime getMappedStart() {
        return timeCalendar.mapStart(getStart());
    }

    /**
     * Gets mapped end.
     *
     * @return the mapped end
     */
    public DateTime getMappedEnd() {
        return timeCalendar.mapEnd(getEnd());
    }

    /**
     * Gets unmapped start.
     *
     * @return the unmapped start
     */
    public DateTime getUnmappedStart() {
        return timeCalendar.unmapStart(getStart());
    }

    /**
     * Gets unmapped end.
     *
     * @return the unmapped end
     */
    public DateTime getUnmappedEnd() {
        return timeCalendar.unmapEnd(getEnd());
    }

    /**
     * Gets start month start.
     *
     * @return the start month start
     */
    public DateTime getStartMonthStart() {
        return Times.trimToDay(getStart());
    }

    /**
     * Gets end month start.
     *
     * @return the end month start
     */
    public DateTime getEndMonthStart() {
        return Times.trimToDay(getEnd());
    }

    /**
     * Gets start day start.
     *
     * @return the start day start
     */
    public DateTime getStartDayStart() {
        return Times.trimToHour(getStart());
    }

    /**
     * Gets end day start.
     *
     * @return the end day start
     */
    public DateTime getEndDayStart() {
        return Times.trimToHour(getEnd());
    }

    /**
     * Gets start hour start.
     *
     * @return the start hour start
     */
    public DateTime getStartHourStart() {
        return Times.trimToMinute(getStart());
    }

    /**
     * Gets end hour start.
     *
     * @return the end hour start
     */
    public DateTime getEndHourStart() {
        return Times.trimToMinute(getEnd());
    }

    /**
     * Gets start minute start.
     *
     * @return the start minute start
     */
    public DateTime getStartMinuteStart() {
        return Times.trimToSecond(getStart());
    }

    /**
     * Gets end minute start.
     *
     * @return the end minute start
     */
    public DateTime getEndMinuteStart() {
        return Times.trimToSecond(getEnd());
    }

    /**
     * Gets start second start.
     *
     * @return the start second start
     */
    public DateTime getStartSecondStart() {
        return Times.trimToMillis(getStart());
    }

    /**
     * Gets end second start.
     *
     * @return the end second start
     */
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
        return super.buildStringHelper(); //.add("timeCalendar", timeCalendar);
    }
}
