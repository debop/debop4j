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

package kr.debop4j.timeperiod.test.samples;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.timeperiod.ITimeBlock;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.TimeBlock;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;
import java.util.List;

/**
 * kr.debop4j.timeperiod.test.samples.TimeBlockPeriodRelationTestData
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 3:34
 */
public class TimeBlockPeriodRelationTestData implements Serializable {

    private static final long serialVersionUID = -5287403574700352744L;

    @Getter private final List<ITimePeriod> allPeriods = Lists.newArrayList();

    @Getter @Setter
    private ITimeBlock reference;

    @Getter @Setter
    private ITimeBlock before;

    @Getter @Setter
    private ITimeBlock startTouching;

    @Getter @Setter
    private ITimeBlock startInside;

    @Getter @Setter
    private ITimeBlock insideStartTouching;

    @Getter @Setter
    private ITimeBlock enclosingStartTouching;

    @Getter @Setter
    private ITimeBlock inside;

    @Getter @Setter
    private ITimeBlock enclosingEndTouching;

    @Getter @Setter
    private ITimeBlock exactMatch;

    @Getter @Setter
    private ITimeBlock enclosing;

    @Getter @Setter
    private ITimeBlock insideEndTouching;

    @Getter @Setter
    private ITimeBlock endInside;

    @Getter @Setter
    private ITimeBlock endTouching;

    @Getter @Setter
    private ITimeBlock after;

    public TimeBlockPeriodRelationTestData(DateTime start, DateTime end, Duration duration) {

        Guard.shouldBe(duration.compareTo(Duration.ZERO) >= 0, "duration은 0이상의 기간을 가져야 합니다.");

        setReference(new TimeBlock(start, end, true));

        DateTime beforeEnd = start.minus(duration);
        DateTime beforeStart = beforeEnd.minus(reference.getDuration());
        DateTime insideStart = start.plus(duration);
        DateTime insideEnd = end.minus(duration);
        DateTime afterStart = end.plus(duration);
        DateTime afterEnd = afterStart.plus(reference.getDuration());

        after = new TimeBlock(beforeStart, beforeEnd, true);
        startTouching = new TimeBlock(beforeStart, start, true);
        startInside = new TimeBlock(beforeStart, insideStart, true);
        insideStartTouching = new TimeBlock(start, afterStart, true);
        enclosingStartTouching = new TimeBlock(start, insideEnd, true);
        enclosing = new TimeBlock(insideStart, insideEnd, true);
        enclosingEndTouching = new TimeBlock(insideStart, end, true);
        exactMatch = new TimeBlock(start, end, true);
        inside = new TimeBlock(beforeStart, afterEnd, true);
        insideEndTouching = new TimeBlock(beforeStart, end, true);
        endInside = new TimeBlock(insideEnd, afterEnd, true);
        endTouching = new TimeBlock(end, afterEnd, true);
        before = new TimeBlock(afterStart, afterEnd, true);

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
