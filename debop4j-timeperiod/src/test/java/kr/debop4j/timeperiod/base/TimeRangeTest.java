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

package kr.debop4j.timeperiod.base;

import kr.debop4j.timeperiod.PeriodRelation;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.clock.ClockProxy;
import kr.debop4j.timeperiod.samples.TimeRangePeriodRelationTestData;
import kr.debop4j.timeperiod.tools.Durations;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.base.TimeRangeTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 2:16
 */
@Slf4j
public class TimeRangeTest {

    private final Duration duration = new Duration(Durations.hours(1));
    private final Duration offset = Durations.Second;

    private DateTime start = DateTime.now();
    private DateTime end = start.plus(duration);
    private TimeRangePeriodRelationTestData testData = new TimeRangePeriodRelationTestData(start, end, offset);

    @Test
    public void anytimeTest() throws Exception {
        assertThat(TimeRange.Anytime.getStart()).isEqualTo(TimeSpec.MinPeriodTime);
        assertThat(TimeRange.Anytime.getEnd()).isEqualTo(TimeSpec.MaxPeriodTime);

        assertThat(TimeRange.Anytime.isAnytime()).isTrue();
        assertThat(TimeRange.Anytime.isReadonly()).isTrue();

        assertThat(TimeRange.Anytime.hasPeriod()).isFalse();
        assertThat(TimeRange.Anytime.hasStart()).isFalse();
        assertThat(TimeRange.Anytime.hasEnd()).isFalse();
        assertThat(TimeRange.Anytime.isMoment()).isFalse();
    }

    @Test
    public void defaultContructorTest() throws Exception {
        TimeRange range = new TimeRange();

        assertThat(range).isNotEqualTo(TimeRange.Anytime);
        assertThat(Times.getRelation(range, TimeRange.Anytime)).isEqualTo(PeriodRelation.ExactMatch);

        assertThat(range.isAnytime()).isTrue();
        assertThat(range.isReadonly()).isFalse();

        assertThat(range.hasPeriod()).isFalse();
        assertThat(range.hasStart()).isFalse();
        assertThat(range.hasEnd()).isFalse();
        assertThat(range.isMoment()).isFalse();
    }

    @Test
    public void momentTest() throws Exception {
        DateTime moment = ClockProxy.getClock().now();
        TimeRange range = new TimeRange(moment);

        assertThat(range.hasStart()).isTrue();
        assertThat(range.hasEnd()).isTrue();
        assertThat(range.getDuration()).isEqualTo(TimeSpec.MinDuration);

        assertThat(range.isAnytime()).isFalse();
        assertThat(range.isMoment()).isTrue();
        assertThat(range.hasPeriod()).isTrue();
    }

    @Test
    public void momentByPeriod() {
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), Duration.ZERO);
        assertThat(range.isMoment()).isTrue();
    }

    @Test
    public void nonMomentTest() {
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), TimeSpec.MinPositiveDuration);
        assertThat(range.isMoment()).isFalse();
        assertThat(range.getDuration()).isEqualTo(TimeSpec.MinPositiveDuration);
    }

    @Test
    public void hasStartTest() {
        // 현재부터 ~
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), (DateTime) null);
        assertThat(range.hasStart()).isTrue();
        assertThat(range.hasEnd()).isFalse();
    }

    @Test
    public void hasEndTest() {
        //  ~ 현재까지
        TimeRange range = new TimeRange(null, ClockProxy.getClock().now());
        assertThat(range.hasStart()).isFalse();
        assertThat(range.hasEnd()).isTrue();
    }

    @Test
    public void startEndTest() {
        TimeRange range = new TimeRange(start, end);

        assertThat(range.getStart()).isEqualTo(start);
        assertThat(range.getEnd()).isEqualTo(end);
        assertThat(range.getDuration()).isEqualTo(duration);

        assertThat(range.hasPeriod()).isTrue();
        assertThat(range.isAnytime()).isFalse();
        assertThat(range.isMoment()).isFalse();
        assertThat(range.isReadonly()).isFalse();
    }

    @Test
    public void startEndSwapTest() {
        TimeRange range = new TimeRange(end, start);

        assertThat(range.getStart()).isEqualTo(start);
        assertThat(range.getEnd()).isEqualTo(end);
        assertThat(range.getDuration()).isEqualTo(duration);

        assertThat(range.hasPeriod()).isTrue();
        assertThat(range.isAnytime()).isFalse();
        assertThat(range.isMoment()).isFalse();
        assertThat(range.isReadonly()).isFalse();
    }

    @Test
    public void startAndDurationTest() {
        TimeRange range = new TimeRange(start, duration);

        assertThat(range.getStart()).isEqualTo(start);
        assertThat(range.getEnd()).isEqualTo(end);
        assertThat(range.getDuration()).isEqualTo(duration);

        assertThat(range.hasPeriod()).isTrue();
        assertThat(range.isAnytime()).isFalse();
        assertThat(range.isMoment()).isFalse();
        assertThat(range.isReadonly()).isFalse();
    }

    @Test
    public void startAndNegateDurationTest() {
        TimeRange range = new TimeRange(start, Durations.negate(duration));

        assertThat(range.getStart()).isEqualTo(start.minus(duration));
        assertThat(range.getEnd()).isEqualTo(end.minus(duration));
        assertThat(range.getDuration()).isEqualTo(duration);

        assertThat(range.hasPeriod()).isTrue();
        assertThat(range.isAnytime()).isFalse();
        assertThat(range.isMoment()).isFalse();
        assertThat(range.isReadonly()).isFalse();
    }

    @Test
    public void copyConstructorTest() {
        TimeRange source = new TimeRange(start, start.plusHours(1), true);
        TimeRange copy = new TimeRange(source);

        assertThat(copy.getStart()).isEqualTo(source.getStart());
        assertThat(copy.getEnd()).isEqualTo(source.getEnd());
        assertThat(copy.getDuration()).isEqualTo(source.getDuration());

        assertThat(copy.isReadonly()).isEqualTo(source.isReadonly());

        assertThat(copy.hasPeriod()).isTrue();
        assertThat(copy.isAnytime()).isFalse();
        assertThat(copy.isMoment()).isFalse();
    }

    @Test
    public void startTest() {
        TimeRange range = new TimeRange(start, start.plusHours(1));
        assertThat(range.getStart()).isEqualTo(start);

        DateTime chanedStart = start.plusHours(1);
        range.setStart(chanedStart);
        assertThat(range.getStart()).isEqualTo(chanedStart);
    }

    @Test( expected = AssertionError.class )
    public void startReadonlyTest() {
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), Durations.hours(1), true);
        range.setStart(range.getStart().minusHours(2));
    }

    @Test( expected = AssertionError.class )
    public void startOutOfRangeTest() {
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), Durations.hours(1), false);
        range.setStart(range.getStart().plusHours(2));
    }

    @Test
    public void endTest() throws Exception {
        TimeRange range = new TimeRange(end.minusHours(1), end);
        assertThat(range.getEnd()).isEqualTo(end);

        DateTime changedEnd = end.plusHours(1);
        range.setEnd(changedEnd);
        assertThat(range.getEnd()).isEqualTo(changedEnd);
    }

    @Test( expected = AssertionError.class )
    public void endReadonlyTest() {
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), Durations.hours(1), true);
        range.setEnd(range.getEnd().plusHours(1));
    }

    @Test( expected = AssertionError.class )
    public void endOutOfRangeTest() {
        TimeRange range = new TimeRange(ClockProxy.getClock().now(), Durations.hours(1), false);
        range.setEnd(range.getEnd().minusHours(2));
    }

    @Test
    public void hasInsideDateTimeTest() {
        TimeRange range = new TimeRange(start, end);

        assertThat(range.getEnd()).isEqualTo(end);

        assertThat(range.hasInside(start.minus(duration))).isFalse();
        assertThat(range.hasInside(start)).isTrue();
        assertThat(range.hasInside(start.plus(duration))).isTrue();

        assertThat(range.hasInside(end.minus(duration))).isTrue();
        assertThat(range.hasInside(end)).isTrue();
        assertThat(range.hasInside(end.plus(duration))).isFalse();
    }

    @Test
    public void hasInsidePeriodTest() {
        TimeRange range = new TimeRange(start, end);

        assertThat(range.getEnd()).isEqualTo(end);

        // before
        TimeRange before1 = new TimeRange(start.minusHours(2), start.minusHours(1));
        TimeRange before2 = new TimeRange(start.minusMillis(1), end);
        TimeRange before3 = new TimeRange(start.minusMillis(1), start);

        assertThat(range.hasInside(before1)).isFalse();
        assertThat(range.hasInside(before2)).isFalse();
        assertThat(range.hasInside(before3)).isFalse();

        // after
        TimeRange after1 = new TimeRange(start.plusHours(1), end.plusHours(1));
        TimeRange after2 = new TimeRange(start, end.plusMillis(1));
        TimeRange after3 = new TimeRange(end, end.plusMillis(1));

        assertThat(range.hasInside(after1)).isFalse();
        assertThat(range.hasInside(after2)).isFalse();
        assertThat(range.hasInside(after3)).isFalse();

        // inside
        assertThat(range.hasInside(range)).isTrue();

        TimeRange inside1 = new TimeRange(start.plusMillis(1), end);
        TimeRange inside2 = new TimeRange(start.plusMillis(1), end.minusMillis(1));
        TimeRange inside3 = new TimeRange(start, end.minusMillis(1));

        assertThat(range.hasInside(inside1)).isTrue();
        assertThat(range.hasInside(inside2)).isTrue();
        assertThat(range.hasInside(inside3)).isTrue();
    }

    @Test
    public void copyTest() {
        TimeRange readonlyTimeRange = new TimeRange(start, end);
        assertThat(readonlyTimeRange.copy()).isEqualTo(readonlyTimeRange);
        assertThat(readonlyTimeRange.copy(Duration.ZERO)).isEqualTo(readonlyTimeRange);

        TimeRange range = new TimeRange(start, end);

        assertThat(range.getStart()).isEqualTo(start);
        assertThat(range.getEnd()).isEqualTo(end);

        TimeRange noMove = range.copy(Durations.Zero);

        assertThat(noMove.getStart()).isEqualTo(range.getStart());
        assertThat(noMove.getEnd()).isEqualTo(range.getEnd());
        assertThat(noMove.getDuration()).isEqualTo(range.getDuration());
        assertThat(noMove).isEqualTo(noMove);

        Duration forwardOffset = Durations.hours(2, 30, 15);
        TimeRange forward = range.copy(forwardOffset);

        assertThat(forward.getStart()).isEqualTo(start.plus(forwardOffset));
        assertThat(forward.getEnd()).isEqualTo(end.plus(forwardOffset));
        assertThat(forward.getDuration()).isEqualTo(duration);

        Duration backwardOffset = Durations.hours(-1, 10, 30);
        TimeRange backward = range.copy(backwardOffset);

        assertThat(backward.getStart()).isEqualTo(start.plus(backwardOffset));
        assertThat(backward.getEnd()).isEqualTo(end.plus(backwardOffset));
        assertThat(backward.getDuration()).isEqualTo(duration);
    }

    @Test
    public void moveTest() {
        TimeRange moveZero = new TimeRange(start, end);
        moveZero.move(Durations.Zero);
        assertThat(moveZero.getStart()).isEqualTo(start);
        assertThat(moveZero.getEnd()).isEqualTo(end);
        assertThat(moveZero.getDuration()).isEqualTo(duration);

        TimeRange forward = new TimeRange(start, end);
        Duration forwardOffset = Durations.hours(2, 30, 15);
        forward.move(forwardOffset);

        assertThat(forward.getStart()).isEqualTo(start.plus(forwardOffset));
        assertThat(forward.getEnd()).isEqualTo(end.plus(forwardOffset));
        assertThat(forward.getDuration()).isEqualTo(duration);

        TimeRange backward = new TimeRange(start, end);
        Duration backwardOffset = Durations.hours(-1, 10, 30);
        backward.move(backwardOffset);

        assertThat(backward.getStart()).isEqualTo(start.plus(backwardOffset));
        assertThat(backward.getEnd()).isEqualTo(end.plus(backwardOffset));
        assertThat(backward.getDuration()).isEqualTo(duration);
    }

    @Test
    public void expandStartToTest() {
        TimeRange range = new TimeRange(start, end);

        range.expandStartTo(start.plusMillis(1));
        assertThat(range.getStart()).isEqualTo(start);

        range.expandStartTo(start.minusMillis(1));
        assertThat(range.getStart()).isEqualTo(start.minusMillis(1));
    }

    @Test
    public void expandEndToTest() {
        TimeRange range = new TimeRange(start, end);

        range.expandEndTo(end.minusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end);

        range.expandEndTo(end.plusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end.plusMillis(1));
    }

    @Test
    public void expandToDateTimeTest() {
        TimeRange range = new TimeRange(start, end);

        // start
        range.expandTo(start.plusMillis(1));
        assertThat(range.getStart()).isEqualTo(start);

        range.expandTo(start.minusMillis(1));
        assertThat(range.getStart()).isEqualTo(start.minusMillis(1));

        // end
        range.expandTo(end.minusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end);

        range.expandTo(end.plusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end.plusMillis(1));
    }

    @Test
    public void expandToPeriodTest() {
        TimeRange range = new TimeRange(start, end);

        // no expansion
        range.expandTo(new TimeRange(start.plusMillis(1), end.minusMillis(1)));
        assertThat(range.getStart()).isEqualTo(start);
        assertThat(range.getEnd()).isEqualTo(end);

        // start
        DateTime changedStart = start.minusMinutes(1);
        range.expandTo(new TimeRange(changedStart, end));
        assertThat(range.getStart()).isEqualTo(changedStart);
        assertThat(range.getEnd()).isEqualTo(end);

        // end
        DateTime changedEnd = end.plusMinutes(1);
        range.expandTo(new TimeRange(changedStart, changedEnd));
        assertThat(range.getStart()).isEqualTo(changedStart);
        assertThat(range.getEnd()).isEqualTo(changedEnd);

        // start/end
        changedStart = changedStart.minusMinutes(1);
        changedEnd = changedEnd.plusMinutes(1);
        range.expandTo(new TimeRange(changedStart, changedEnd));
        assertThat(range.getStart()).isEqualTo(changedStart);
        assertThat(range.getEnd()).isEqualTo(changedEnd);
    }

    @Test
    public void shrinkStartToTest() {
        TimeRange range = new TimeRange(start, end);

        range.shrinkStartTo(start.minusMillis(1));
        assertThat(range.getStart()).isEqualTo(start);

        range.shrinkStartTo(start.plusMillis(1));
        assertThat(range.getStart()).isEqualTo(start.plusMillis(1));
    }

    @Test
    public void shrinkEndToTest() {
        TimeRange range = new TimeRange(start, end);

        range.shrinkEndTo(end.plusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end);

        range.shrinkEndTo(end.minusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end.minusMillis(1));
    }

    @Test
    public void shrinkToDateTimeTest() {
        TimeRange range = new TimeRange(start, end);

        // start
        range.shrinkTo(start.minusMillis(1));
        assertThat(range.getStart()).isEqualTo(start);

        range.shrinkTo(start.plusMillis(1));
        assertThat(range.getStart()).isEqualTo(start.plusMillis(1));

        range = new TimeRange(start, end);

        // end
        range.shrinkTo(end.plusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end);

        range.shrinkTo(end.minusMillis(1));
        assertThat(range.getEnd()).isEqualTo(end.minusMillis(1));
    }

    @Test
    public void shrinkToPeriodTest() {
        TimeRange range = new TimeRange(start, end);

        // no expansion
        range.shrinkTo(new TimeRange(start.minusMillis(1), end.plusMillis(1)));
        assertThat(range.getStart()).isEqualTo(start);
        assertThat(range.getEnd()).isEqualTo(end);

        // start
        DateTime changedStart = start.plusMinutes(1);
        range.shrinkTo(new TimeRange(changedStart, end));
        assertThat(range.getStart()).isEqualTo(changedStart);
        assertThat(range.getEnd()).isEqualTo(end);

        // end
        DateTime changedEnd = end.minusMinutes(1);
        range.shrinkTo(new TimeRange(changedStart, changedEnd));
        assertThat(range.getStart()).isEqualTo(changedStart);
        assertThat(range.getEnd()).isEqualTo(changedEnd);

        // start/end
        changedStart = changedStart.plusMinutes(1);
        changedEnd = changedEnd.minusMinutes(1);
        range.shrinkTo(new TimeRange(changedStart, changedEnd));
        assertThat(range.getStart()).isEqualTo(changedStart);
        assertThat(range.getEnd()).isEqualTo(changedEnd);
    }

    @Test
    public void isSamePeriodTest() {
        TimeRange range1 = new TimeRange(start, end);
        TimeRange range2 = new TimeRange(start, end);

        assertThat(range1.isSamePeriod(range1)).isTrue();
        assertThat(range2.isSamePeriod(range2)).isTrue();

        assertThat(range1.isSamePeriod(range2)).isTrue();
        assertThat(range2.isSamePeriod(range1)).isTrue();

        assertThat(range1.isSamePeriod(TimeRange.Anytime)).isFalse();
        assertThat(range2.isSamePeriod(TimeRange.Anytime)).isFalse();

        range1.move(Durations.Millisecond);
        assertThat(range1.isSamePeriod(range2)).isFalse();
        assertThat(range2.isSamePeriod(range1)).isFalse();

        range1.move(Durations.millis(-1));
        assertThat(range1.isSamePeriod(range2)).isTrue();
        assertThat(range2.isSamePeriod(range1)).isTrue();
    }

    @Test
    public void hasInsideTest() {

        assertThat(testData.getReference().hasInside(testData.getBefore())).isFalse();
    }
}
