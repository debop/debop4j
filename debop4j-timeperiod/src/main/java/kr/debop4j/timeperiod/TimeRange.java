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

import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 기간을 표현하는 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:05
 */
@Slf4j
public class TimeRange extends TimePeriodBase implements ITimeRange {

    private static final long serialVersionUID = -5665345604375538630L;

    /**
     * The constant Anytime.
     */
    public static final TimeRange Anytime = new TimeRange(true);

    /**
     * To time block.
     *
     * @param range the range
     * @return the i time block
     */
    public static ITimeBlock toTimeBlock(ITimeRange range) {
        return new TimeBlock(range.getStart(), range.getEnd());
    }

    /**
     * To time interval.
     *
     * @param range the range
     * @return the i time interval
     */
    public static ITimeInterval toTimeInterval(ITimeRange range) {
        return new TimeInterval(range.getStart(), range.getEnd());
    }

    // region << Constructor >>

    /**
     * Instantiates a new Time range.
     */
    public TimeRange() {}

    /**
     * Instantiates a new Time range.
     *
     * @param readonly the readonly
     */
    public TimeRange(boolean readonly) {
        super(readonly);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param moment the moment
     */
    public TimeRange(DateTime moment) {
        super(moment);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param start the start
     * @param end   the end
     */
    public TimeRange(DateTime start, DateTime end) {
        super(start, end);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param start    the start
     * @param end      the end
     * @param readonly the readonly
     */
    public TimeRange(DateTime start, DateTime end, boolean readonly) {
        super(start, end, readonly);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param start    the start
     * @param duration the duration
     */
    public TimeRange(DateTime start, Duration duration) {
        super(start, duration);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param start    the start
     * @param duration the duration
     * @param readonly the readonly
     */
    public TimeRange(DateTime start, Duration duration, boolean readonly) {
        super(start, duration, readonly);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param source the source
     */
    public TimeRange(ITimePeriod source) {
        super(source);
    }

    /**
     * Instantiates a new Time range.
     *
     * @param source   the source
     * @param readonly the readonly
     */
    public TimeRange(ITimePeriod source, boolean readonly) {
        super(source, readonly);
    }

    // endregion

    @Override
    public void setStart(DateTime start) {
        assertMutable();
        assert start.compareTo(this.end) <= 0 : "시작시각이 완료시각보다 클 수 없습니다.";
        this.start = start;
    }

    @Override
    public void setEnd(DateTime end) {
        assertMutable();
        assert end.compareTo(this.start) >= 0 : "완료시각이 시작시각보다 작을 수 없습니다.";
        this.end = end;
    }

    @Override
    public TimeRange copy() {
        return (TimeRange) super.copy();
    }

    @Override
    public TimeRange copy(Duration offset) {
        return (TimeRange) super.copy(offset);
    }

    @Override
    public void expandStartTo(DateTime moment) {
        assertMutable();
        if (start.compareTo(moment) > 0)
            this.start = moment;
    }

    @Override
    public void expandEndTo(DateTime moment) {
        assertMutable();
        if (end.compareTo(moment) < 0)
            this.end = moment;
    }

    @Override
    public void expandTo(DateTime moment) {
        assertMutable();
        expandStartTo(moment);
        expandEndTo(moment);
    }

    @Override
    public void expandTo(ITimePeriod period) {
        assertMutable();

        if (period.hasStart())
            expandStartTo(period.getStart());
        if (period.hasEnd())
            expandEndTo(period.getEnd());
    }

    @Override
    public void shrinkStartTo(DateTime moment) {
        assertMutable();
        if (hasInside(moment) && start.compareTo(moment) < 0)
            start = moment;
    }

    @Override
    public void shrinkEndTo(DateTime moment) {
        assertMutable();
        if (hasInside(moment) && end.compareTo(moment) > 0)
            end = moment;
    }

    public void shrinkTo(DateTime moment) {
        assertMutable();
        shrinkStartTo(moment);
        shrinkEndTo(moment);
    }

    @Override
    public void shrinkTo(ITimePeriod period) {
        assert period != null;
        assertMutable();

        if (period.hasStart())
            shrinkStartTo(period.getStart());
        if (period.hasEnd())
            shrinkEndTo(period.getEnd());
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        assert other != null;
        return Times.getIntersectionRange(this, other);
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        assert other != null;
        return Times.getUnionRange(this, other);
    }
}
