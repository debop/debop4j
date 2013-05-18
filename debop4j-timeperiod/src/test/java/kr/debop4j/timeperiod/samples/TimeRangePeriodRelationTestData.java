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

package kr.debop4j.timeperiod.samples;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.ITimeRange;
import kr.debop4j.timeperiod.TimeRange;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;
import java.util.List;

/**
 * kr.debop4j.timeperiod.samples.TimeRangePeriodRelationTestData
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 2:22
 */
public class TimeRangePeriodRelationTestData implements Serializable {

    private static final long serialVersionUID = 7453794280924071741L;

    @Getter private final List<ITimePeriod> allPeriods = Lists.newArrayList();

    @Getter @Setter
    private ITimeRange reference;

    @Getter @Setter
    private ITimeRange before;

    @Getter @Setter
    private ITimeRange startTouching;

    @Getter @Setter
    private ITimeRange startInside;

    @Getter @Setter
    private ITimeRange insideStartTouching;

    @Getter @Setter
    private ITimeRange enclosingStartTouching;

    @Getter @Setter
    private ITimeRange inside;

    @Getter @Setter
    private ITimeRange enclosingEndTouching;

    @Getter @Setter
    private ITimeRange exactMatch;

    @Getter @Setter
    private ITimeRange enclosing;

    @Getter @Setter
    private ITimeRange insideEndTouching;

    @Getter @Setter
    private ITimeRange endInside;

    @Getter @Setter
    private ITimeRange endTouching;

    @Getter @Setter
    private ITimeRange after;

    public TimeRangePeriodRelationTestData(DateTime start, DateTime end, Duration duration) {
        Guard.shouldBe(duration.compareTo(Duration.ZERO) >= 0, "duration은 0이상의 기간을 가져야 합니다.");

        setReference(new TimeRange(start, end, true));

        DateTime beforeEnd = start.minus(duration);
        DateTime beforeStart = beforeEnd.minus(reference.getDuration());
        DateTime insideStart = start.plus(duration);
        DateTime insideEnd = end.minus(duration);
        DateTime afterStart = end.plus(duration);
        DateTime afterEnd = afterStart.plus(reference.getDuration());

        after = new TimeRange(beforeStart, beforeEnd, true);
        startTouching = new TimeRange(beforeStart, start, true);
        startInside = new TimeRange(beforeStart, insideStart, true);
        insideStartTouching = new TimeRange(start, afterStart, true);
        enclosingStartTouching = new TimeRange(start, insideEnd, true);
        enclosing = new TimeRange(insideStart, insideEnd, true);
        enclosingEndTouching = new TimeRange(insideStart, end, true);
        exactMatch = new TimeRange(start, end, true);
        inside = new TimeRange(beforeStart, afterEnd, true);
        insideEndTouching = new TimeRange(beforeStart, end, true);
        endInside = new TimeRange(insideEnd, afterEnd, true);
        endTouching = new TimeRange(end, afterEnd, true);
        before = new TimeRange(afterStart, afterEnd, true);

        allPeriods.add(reference);
        allPeriods.add(after);
        allPeriods.add(startTouching);
        allPeriods.add(startInside);
        allPeriods.add(insideStartTouching);
        allPeriods.add(enclosingStartTouching);
        allPeriods.add(enclosing);
        allPeriods.add(enclosingEndTouching);
        allPeriods.add(exactMatch);
        allPeriods.add(inside);
        allPeriods.add(insideEndTouching);
        allPeriods.add(endInside);
        allPeriods.add(endTouching);
        allPeriods.add(before);
    }
}
