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

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import static kr.debop4j.core.Guard.firstNotNull;

/**
 * kr.debop4j.timeperiod.TimeInterval
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 9:06
 */
@Slf4j
public class TimeInterval extends TimePeriodBase implements ITimeInterval {

    private static final long serialVersionUID = 5941695883167563118L;

    public static TimeInterval Anytime = new TimeInterval(TimeSpec.MinPeriodTime,
                                                          TimeSpec.MaxPeriodTime,
                                                          IntervalEdge.Closed,
                                                          IntervalEdge.Closed,
                                                          false,
                                                          true);

    public static TimeRange toRange(ITimeInterval interval) {
        if (interval == null)
            return null;

        return interval.isAnytime()
                ? TimeRange.Anytime
                : new TimeRange(interval.getStartInterval(), interval.getEndInterval(), interval.isReadonly());
    }

    public static TimeBlock toBlock(ITimeInterval interval) {
        if (interval == null)
            return null;

        return interval.isAnytime()
                ? TimeBlock.Anytime
                : new TimeBlock(interval.getStartInterval(), interval.getEndInterval(), interval.isReadonly());
    }


    private IntervalEdge startEdge;

    private IntervalEdge endEdge;

    private boolean intervalEnabled;

    // region << Constructor >>

    public TimeInterval() {
        this(TimeSpec.MinPeriodTime, TimeSpec.MaxPeriodTime);
    }

    public TimeInterval(DateTime moment) {
        this(moment, moment);
    }

    public TimeInterval(DateTime moment, boolean readonly) {
        this(moment, moment, readonly);
    }


    public TimeInterval(DateTime moment, IntervalEdge startEdge, IntervalEdge endEdge) {
        this(moment, moment, startEdge, endEdge, true, false);
    }

    public TimeInterval(DateTime moment, IntervalEdge startEdge, IntervalEdge endEdge, boolean intervalEnabled, boolean readonly) {
        this(moment, moment, startEdge, endEdge, intervalEnabled, readonly);
    }

    public TimeInterval(DateTime start, DateTime end) {
        this(start, end, IntervalEdge.Closed, IntervalEdge.Closed, true, false);
    }

    public TimeInterval(DateTime start, DateTime end, boolean readonly) {
        this(start, end, IntervalEdge.Closed, IntervalEdge.Closed, true, readonly);
    }


    public TimeInterval(DateTime start, DateTime end, IntervalEdge startEdge, IntervalEdge endEdge) {
        this(start, end, startEdge, endEdge, true, false);
    }

    public TimeInterval(DateTime start, DateTime end, IntervalEdge startEdge, IntervalEdge endEdge, boolean intervalEnabled, boolean readonly) {
        super(start, end, readonly);

        this.startEdge = startEdge;
        this.endEdge = endEdge;
        this.intervalEnabled = intervalEnabled;
    }

    // endregion

    @Override
    public boolean isStartOpen() {
        return startEdge == IntervalEdge.Open;
    }

    @Override
    public boolean isEndOpen() {
        return endEdge == IntervalEdge.Open;
    }

    @Override
    public boolean isOpen() {
        return isStartOpen() && isEndOpen();
    }

    @Override
    public boolean isStartClosed() {
        return startEdge == IntervalEdge.Closed;
    }

    @Override
    public boolean isEndClosed() {
        return endEdge == IntervalEdge.Closed;
    }

    @Override
    public boolean isClosed() {
        return isStartClosed() && isEndClosed();
    }

    @Override
    public boolean isEmpty() {
        return isMoment() && !isClosed();
    }

    @Override
    public boolean isDegenerate() {
        return isMoment() && isClosed();
    }

    @Override
    public boolean isIntervalEnabled() {
        return this.intervalEnabled;
    }

    @Override
    public boolean hasStart() {
        return !this.start.equals(TimeSpec.MinPeriodTime) || !isStartClosed();
    }

    public void setIntervalEnabled(boolean intervalEnabled) {
        this.intervalEnabled = intervalEnabled;
    }

    @Override
    public DateTime getStartInterval() {
        return start;
    }

    @Override
    public void setStartInterval(DateTime start) {
        assertMutable();
        assert start.compareTo(this.end) <= 0;
        this.start = start;
    }

    public DateTime getStart() {
        return (isIntervalEnabled() && isStartOpen()) ? this.start.plus(1) : this.start;
    }

    @Override
    public IntervalEdge getStartEdge() {
        return this.startEdge;
    }

    @Override
    public void setStartEdge(IntervalEdge startEdge) {
        assertMutable();
        this.startEdge = startEdge;
    }

    @Override
    public boolean hasEnd() {
        return (!this.end.equals(TimeSpec.MaxPeriodTime) || !isEndClosed());
    }

    @Override
    public DateTime getEndInterval() {
        return end;
    }

    @Override
    public void setEndInterval(DateTime end) {
        assertMutable();
        assert end.compareTo(this.start) >= 0;
        this.end = end;
    }

    @Override
    public DateTime getEnd() {
        return (isIntervalEnabled() && isEndOpen()) ? this.end.minus(1) : this.end;
    }

    @Override
    public IntervalEdge getEndEdge() {
        return this.endEdge;
    }

    @Override
    public void setEndEdge(IntervalEdge startEdge) {
        assertMutable();
        this.startEdge = startEdge;
    }

    @Override
    public Duration getDuration() {
        return new Duration(start, end);
    }

    @Override
    public void setup(DateTime ns, DateTime ne) {
        assertMutable();
        super.setup(ns, ne);
        if (log.isDebugEnabled())
            log.debug("기간을 새로 설정합니다. newStart=[{}], newEnd=[{}]", ns, ne);

        Times.adjustPeriod(ns, ne);
        this.start = firstNotNull(ns, TimeSpec.MinPeriodTime);
        this.end = firstNotNull(ne, TimeSpec.MaxPeriodTime);
    }

    @Override
    public void expandStartTo(DateTime moment) {
        assertMutable();
        if (start.compareTo(moment) > 0)
            start = moment;
    }

    @Override
    public void expandEndTo(DateTime moment) {
        assertMutable();
        if (end.compareTo(moment) < 0)
            end = moment;
    }

    @Override
    public void expandTo(DateTime moment) {
        expandStartTo(moment);
        expandEndTo(moment);
    }

    @Override
    public void expandTo(ITimePeriod period) {
        assert period != null;

        if (period.hasStart())
            expandStartTo(period.getStart());
        if (period.hasEnd())
            expandEndTo(period.getEnd());
    }

    @Override
    public void shrinkStartTo(DateTime moment) {
        assertMutable();
        if (start.compareTo(moment) < 0)
            start = moment;
    }

    @Override
    public void shrinkEndTo(DateTime moment) {
        assertMutable();
        if (end.compareTo(moment) > 0)
            end = moment;
    }

    @Override
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
    public ITimeInterval copy() {
        return copy(Duration.ZERO);
    }

    /** 현재 IInterval에서 오프셋만큼 이동한 {@link ITimeInterval}을 반환합니다. */
    @Override
    public ITimeInterval copy(Duration offset) {
        return new TimeInterval(getStartInterval().plus(offset),
                                getEndInterval().plus(offset),
                                getStartEdge(),
                                getEndEdge(),
                                isIntervalEnabled(),
                                isReadonly());
    }

    @Override
    public void reset() {
        super.reset();
        intervalEnabled = true;
        startEdge = IntervalEdge.Closed;
        endEdge = IntervalEdge.Closed;
    }

    @Override
    public ITimeInterval getIntersection(ITimePeriod other) {
        assert other != null;
        ITimePeriod range = super.getIntersection(other);
        return new TimeInterval(range.getStart(), range.getEnd());
    }

    @Override
    public ITimeInterval getUnion(ITimePeriod other) {
        assert other != null;
        ITimePeriod union = Times.getUnionRange(this, other);
        return new TimeInterval(union.getStart(), union.getEnd());
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), isIntervalEnabled(), getStartEdge(), getEndEdge());
    }
}
