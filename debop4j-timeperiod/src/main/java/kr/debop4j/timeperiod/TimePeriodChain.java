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

import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;

import static kr.debop4j.core.Guard.*;

/**
 * {@link ITimePeriod}를 시간의 흐름 순으로 Chain (Linked List) 형태로 표현한 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 4:17
 */
@Slf4j
public class TimePeriodChain extends TimePeriodContainer implements ITimePeriodChain {

    private static final long serialVersionUID = -5838724440389574448L;

    public TimePeriodChain() {}

    public TimePeriodChain(ITimePeriod... periods) {
        if (periods != null)
            addAll(Arrays.asList(periods));
    }

    public TimePeriodChain(Iterable<? extends ITimePeriod> collection) {
        if (collection != null)
            addAll(collection);
    }

    @Override
    public DateTime getStart() {
        return (getFirst() != null) ? getFirst().getStart() : TimeSpec.MinPeriodTime;
    }

    @Override
    public void setStart(DateTime value) {
        if (getFirst() != null)
            move(new Duration(getStart(), value));
    }

    @Override
    public DateTime getEnd() {
        return (getLast() != null) ? getLast().getEnd() : TimeSpec.MaxPeriodTime;
    }

    @Override
    public void setEnd(DateTime value) {
        if (getLast() != null)
            move(new Duration(getEnd(), value));
    }

    @Override
    public ITimePeriod getFirst() {
        return (size() > 0) ? getPeriods().get(0) : null;
    }

    @Override
    public ITimePeriod getLast() {
        return (size() > 0) ? getPeriods().get(size() - 1) : null;
    }

    @Override
    public ITimePeriod set(int index, ITimePeriod element) {
        shouldBeInRange(index, 0, size(), "index");
        remove(index);
        add(index, element);
        return element;
    }

    @Override
    public boolean add(ITimePeriod period) {
        shouldNotBeNull(period, "period");
        TimeTool.assertMutable(period);

        ITimePeriod last = getLast();
        if (last != null) {
            assertSpaceAfter(last.getEnd(), period.getDuration());
            period.setup(last.getEnd(), last.getEnd().plus(period.getDuration()));
        }
        if (log.isTraceEnabled())
            log.trace("Period chain의 끝에 추가합니다. period=[{}]", period);

        return getPeriods().add(period);
    }

    @Override
    public void addAll(Iterable<? extends ITimePeriod> periods) {
        shouldNotBeNull(periods, "periods");

        for (ITimePeriod period : periods)
            add(period);
    }

    /**
     * {@link ITimePeriod}의 Chain의 index 번째에 item을 삽입합니다. 선행 Period와 후행 Period의 기간 값이 조정됩니다.
     *
     * @param index 삽입할 순서
     * @param item  삽입할 요소
     */
    @Override
    public void add(int index, ITimePeriod item) {
        shouldNotBeNull(item, "item");
        shouldBeInRange(index, 0, size(), "index");
        TimeTool.assertMutable(item);

        if (log.isTraceEnabled())
            log.trace("Chain의 인덱스[{}]에 새로운 요소[{}]를 삽입합니다...", index, item);

        Duration itemDuration = item.getDuration();
        ITimePeriod prevItem = null;
        ITimePeriod nextItem = null;

        if (size() > 0) {
            if (log.isTraceEnabled()) log.trace("시간적 삽입 공간이 존재하는지 검사합니다...");
            if (index > 0) {
                prevItem = get(index - 1);
                assertSpaceAfter(getEnd(), itemDuration);
            }
            if (index < size() - 1) {
                nextItem = get(index);
                assertSpaceBefore(getStart(), itemDuration);
            }
        }

        getPeriods().add(index, item);

        if (prevItem != null) {
            if (log.isTraceEnabled())
                log.trace("선행 period에 기초하여 삽입한 period와 후행 period들의 시간을 조정합니다...");

            item.setup(prevItem.getEnd(), prevItem.getEnd().plus(itemDuration));

            for (int i = index + 1; i < size(); i++) {
                ITimePeriod p = get(i);
                DateTime startTime = p.getStart().plus(itemDuration);
                p.setup(startTime, startTime.plus(p.getDuration()));
            }
        }

        if (nextItem != null) {
            if (log.isTraceEnabled())
                log.trace("후행 period에 기초하여 삽입한 period와 선행 period들의 시간을 조정합니다...");

            DateTime nextStart = nextItem.getStart().minus(itemDuration);
            item.setup(nextStart, nextStart.plus(itemDuration));

            for (int i = 0; i < index - 1; i++) {
                ITimePeriod p = get(i);
                nextStart = p.getStart().minus(itemDuration);
                p.setup(nextStart, nextStart.plus(p.getDuration()));
            }
        }
    }

    /** 지정한 요소를 제거하고, 후속 ITimePeriod 들의 기간을 재조정합니다. (앞으로 당깁니다) */
    @Override
    public boolean remove(Object o) {
        shouldNotBeNull(o, "o");
        shouldBe(o instanceof ITimePeriod, "o is not ITimePeriod type. class=[%s]", o.getClass());

        if (size() <= 0)
            return false;

        ITimePeriod item = (ITimePeriod) o;

        if (log.isTraceEnabled())
            log.trace("요소 [{}]를 컬렉션에서 제거합니다...", item);

        Duration itemDuration = item.getDuration();
        int index = indexOf(item);

        ITimePeriod next = null;
        if (itemDuration.getMillis() > 0 && index >= 0 && index < size() - 1)
            next = get(index);

        boolean removed = getPeriods().remove(item);

        if (removed && next != null) {
            if (log.isTraceEnabled()) log.trace("요소[{}]를 제거하고, chain의 후속 periods 들의 기간을 조정합니다...", item);

            for (int i = index; i < size(); i++) {
                DateTime start = get(i).getStart().minus(itemDuration);
                get(i).setup(start, start.plus(get(i).getDuration()));
            }
        }

        if (log.isTraceEnabled())
            log.trace("요소[{}]를 제거한 결과=[{}]", item, removed);

        return removed;
    }

    /** 지정한 요소를 제거하고, 후속 ITimePeriod 들의 기간을 재조정합니다. (앞으로 당깁니다) */
    @Override
    public ITimePeriod remove(int index) {
        shouldBeInRange(index, 0, size(), "index");

        ITimePeriod removed = get(index);
        return remove(removed) ? removed : null;
    }

    /** moment 이전에 duration 만큼의 시간적 공간이 있는지 여부 (새로운 기간을 추가하기 위해서는 공간이 필요합니다) */
    protected void assertSpaceBefore(DateTime moment, Duration duration) {
        boolean hasSpace = moment != TimeSpec.MinPeriodTime;
        if (hasSpace) {
            Duration remaining = new Duration(TimeSpec.MinPeriodTime, moment);
            hasSpace = duration.compareTo(remaining) <= 0;
        }
        shouldBe(hasSpace, "duration [%s] is out of range.", duration);
    }

    /** moment 이후에 duration 만큼의 시간적 공간이 있는지 여부 (새로운 기간을 추가하기 위해서는 공간이 필요합니다) */
    protected void assertSpaceAfter(DateTime moment, Duration duration) {
        boolean hasSpace = moment != TimeSpec.MaxPeriodTime;
        if (hasSpace) {
            Duration remaining = new Duration(moment, TimeSpec.MaxPeriodTime);
            hasSpace = duration.compareTo(remaining) <= 0;
        }
        shouldBe(hasSpace, "duration [%s] is out of range.", duration);
    }
}
