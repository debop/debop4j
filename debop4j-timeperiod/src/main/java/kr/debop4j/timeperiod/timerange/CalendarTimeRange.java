package kr.debop4j.timeperiod.timerange;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.tools.TimeTool;
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
public class CalendarTimeRange extends TimeRange implements ICalendarTimeRange, Comparable<CalendarTimeRange> {

    private static final long serialVersionUID = -422889827258277497L;

    public static TimeRange toCalendarTimeRange(ITimePeriod period, ITimePeriodMapper mapper) {
        assert period != null;
        if (mapper == null) mapper = new TimeCalendar();

        DateTime mappedStart = mapper.mapStart(period.getStart());
        DateTime mappedEnd = mapper.mapEnd(period.getEnd());

        TimeTool.assertValidPeriod(mappedStart, mappedEnd);
        TimeRange mapped = new TimeRange(mappedStart, mappedEnd);

        if (log.isDebugEnabled())
            log.debug("TimeCalendar 기준의 기간으로 매핑했습니다. period=[{}], mapper=[{}], mapped=[{}]", period, mapper, mapped);

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
        super(toCalendarTimeRange(period, timeCalendar));
        this.timeCalendar = timeCalendar;
    }

    // endregion

    @Getter
    private final ITimeCalendar timeCalendar;

    public int getStartYear() { return getStart().getYear(); }

    public int getStartMonthOfYear() { return getStart().getMonthOfYear(); }

    public int getStartDayOfMonth() { return getStart().getDayOfMonth(); }

    public int getStartHourOfDay() { return getStart().getHourOfDay(); }

    public int getStartMinuteOfHour() { return getStart().getMinuteOfHour(); }

    public int getEndYear() { return getStart().getYear(); }

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
        return TimeTool.trimToDay(getStart());
    }

    public DateTime getEndMonthStart() {
        return TimeTool.trimToDay(getEnd());
    }

    public DateTime getStartDayStart() {
        return TimeTool.trimToHour(getStart());
    }

    public DateTime getEndDayStart() {
        return TimeTool.trimToHour(getEnd());
    }

    public DateTime getStartHourStart() {
        return TimeTool.trimToMinute(getStart());
    }

    public DateTime getEndHourStart() {
        return TimeTool.trimToMinute(getEnd());
    }

    public DateTime getStartMinuteStart() {
        return TimeTool.trimToSecond(getStart());
    }

    public DateTime getEndMinuteStart() {
        return TimeTool.trimToSecond(getEnd());
    }

    public DateTime getStartSecondStart() {
        return TimeTool.trimToMillisecond(getStart());
    }

    public DateTime getEndSecondStart() {
        return TimeTool.trimToMillisecond(getEnd());
    }

    @Override
    public ITimePeriod copy(Duration offset) {
        return toCalendarTimeRange(super.copy(offset), timeCalendar);
    }

    @Override
    protected String format(ITimeFormatter formatter) {
        return formatter.getCalendarPeriod(formatter.getDateTime(getStart()),
                                           formatter.getDateTime(getEnd()),
                                           getDuration());
    }

    @Override
    public int compareTo(CalendarTimeRange o) {
        return getStart().compareTo(o.getStart());
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
