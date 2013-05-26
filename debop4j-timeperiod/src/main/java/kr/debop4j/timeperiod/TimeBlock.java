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
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import static kr.debop4j.timeperiod.tools.TimeSpec.ZeroDuration;
import static org.joda.time.Duration.ZERO;

/**
 * 기준 일자의 시간 간격을 이용하여 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 9:04
 */
@Slf4j
public class TimeBlock extends TimePeriodBase implements ITimeBlock {
    private static final long serialVersionUID = 3967092362222887325L;

    public static final TimeBlock Anytime = new TimeBlock(true);

    public static TimeRange toRange(TimeBlock block) {
        return new TimeRange(block.getStart(), block.getDuration(), block.isReadonly());
    }

    public static TimeInterval toInterval(TimeBlock block) {
        return new TimeInterval(block.getStart(), block.getEnd(), block.isReadonly());
    }

    // region << Constructor >>

    public TimeBlock() {}

    public TimeBlock(boolean readonly) {
        super(readonly);
        this.duration = TimeSpec.MaxDuration;
    }

    public TimeBlock(DateTime moment) {
        super(moment);
        this.duration = super.getDuration();
    }

    public TimeBlock(DateTime moment, boolean readonly) {
        super(moment, readonly);
        this.duration = super.getDuration();
    }

    public TimeBlock(DateTime start, DateTime end) {
        this(start, end, false);
    }

    public TimeBlock(DateTime start, DateTime end, boolean readonly) {
        super(start, end, readonly);
        this.duration = super.getDuration();
    }

    public TimeBlock(DateTime start, Duration duration) {
        this(start, duration, false);
    }

    public TimeBlock(DateTime start, Duration duration, boolean readonly) {
        super(start, duration, readonly);
        assertValidDuration(duration);
        this.duration = super.getDuration();
    }

    public TimeBlock(Duration duration, DateTime end) {
        this(duration, end, false);
    }

    public TimeBlock(Duration duration, DateTime end, boolean readonly) {
        super(null, end, readonly);
        assertValidDuration(duration);
        this.duration = duration;
        this.start = end.minus(duration);
    }

    public TimeBlock(ITimePeriod source) {
        super(source);
        this.duration = source.getDuration();
    }

    public TimeBlock(ITimePeriod source, boolean readonly) {
        super(source, readonly);
        this.duration = source.getDuration();
    }

    // endregion

    private Duration duration;

    protected void assertValidDuration(Duration duration) {
        assert duration.compareTo(ZeroDuration) >= 0 : "duration은 0 이상이어야 합니다.";
    }

    @Override
    public void setStart(DateTime start) {
        assertMutable();
        this.start = start;
    }

    @Override
    public void setEnd(DateTime end) {
        assertMutable();
        this.end = end;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        assertMutable();
        assertValidDuration(duration);
        durationFromStart(duration);
    }

    @Override
    public TimeBlock copy() {
        return copy(ZERO);
    }

    @Override
    public TimeBlock copy(Duration offset) {
        if (ZERO.isEqual(offset))
            return new TimeBlock(this);

        return new TimeBlock(hasStart() ? start.plus(offset) : start,
                             hasEnd() ? end.plus(offset) : end,
                             readonly);
    }

    public void setup(DateTime newStart, Duration duration) {
        assertMutable();
        assertValidDuration(duration);

        if (log.isTraceEnabled())
            log.trace("TimeBlock 값을 새로 설정합니다. newStart=[{}], duration=[{}]", newStart, duration);

        this.start = newStart;
        this.duration = duration;
        this.end = newStart.plus(duration);
    }

    @Override
    public void durationFromStart(Duration newDuration) {
        assertMutable();
        assertValidDuration(newDuration);

        if (newDuration.isEqual(TimeSpec.MaxDuration)) {
            this.duration = newDuration;
            this.end = TimeSpec.MaxPeriodTime;
        } else {
            if (end != null)
                assert this.end.plus(newDuration).compareTo(TimeSpec.MaxPeriodTime) <= 0 : "duration 이 너무 크네요.";

            this.duration = newDuration;
            this.end = this.start.plus(this.duration);
        }
    }

    @Override
    public void durationFromEnd(Duration newDuration) {
        assertMutable();
        assertValidDuration(newDuration);
        this.duration = newDuration;
        this.start = this.end.minus(this.duration);
    }

    public ITimeBlock getPreviousBlock() {
        return getPreviousBlock(ZERO);
    }

    @Override
    public ITimeBlock getPreviousBlock(Duration offset) {
        Duration endOffset = (offset.compareTo(ZERO) > 0) ? new Duration(-offset.getMillis()) : offset;
        return new TimeBlock(getDuration(), getStart().plus(endOffset), readonly);
    }

    public ITimeBlock getNextBlock() {
        return getNextBlock(ZERO);
    }

    @Override
    public ITimeBlock getNextBlock(Duration offset) {
        Duration startOffset = (offset.compareTo(ZERO) > 0) ? offset : new Duration(-offset.getMillis());
        return new TimeBlock(getEnd().plus(startOffset), getDuration(), readonly);
    }

    public TimeBlock getIntersection(ITimePeriod other) {
        assert other != null;
        return Times.getIntersectionBlock(this, other);
    }

    public TimeBlock getUnion(ITimePeriod other) {
        assert other != null;
        return Times.getUnionBlock(this, other);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper();
        //.add("duration", duration);
    }
}
