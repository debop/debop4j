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

package kr.debop4j.timeperiod.timeline;

import jodd.util.collection.SortedArrayList;
import kr.debop4j.timeperiod.ITimeLineMoment;
import kr.debop4j.timeperiod.ITimeLineMomentCollection;
import kr.debop4j.timeperiod.ITimePeriod;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.Iterator;

/**
 * kr.debop4j.timeperiod.timeline.TimeLineMomentCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오전 11:35
 */
@Slf4j
public class TimeLineMomentCollection implements ITimeLineMomentCollection {

    private static final long serialVersionUID = -5739605965754152358L;

    @Getter(lazy = true)
    private static final TimeLineMomentComparer defaultComparer = new TimeLineMomentComparer();

    private final SortedArrayList<ITimeLineMoment> timeLineMoments = new SortedArrayList<>(getDefaultComparer());

    @Override
    public int size() {
        return timeLineMoments.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public ITimeLineMoment getMin() {
        return isEmpty() ? null : timeLineMoments.get(0);
    }

    @Override
    public ITimeLineMoment getMax() {
        return isEmpty() ? null : timeLineMoments.get(size() - 1);
    }

    @Override
    public ITimeLineMoment get(int index) {
        return timeLineMoments.get(index);
    }

    @Override
    public void add(ITimePeriod period) {
        if (period != null) {
            addPeriod(period.getStart(), period);
            addPeriod(period.getEnd(), period);
        }
    }

    @Override
    public synchronized void addAll(Iterable<? extends ITimePeriod> periods) {
        for (ITimePeriod period : periods) {
            if (period != null)
                add(period);
        }
    }

    @Override
    public void remove(ITimePeriod period) {
        if (period != null) {
            removePeriod(period.getStart(), period);
            removePeriod(period.getEnd(), period);
        }
    }

    @Override
    public ITimeLineMoment find(DateTime moment) {
        for (ITimeLineMoment item : timeLineMoments) {
            if (item.getMoment().equals(moment))
                return item;
        }
        return null;
    }

    @Override
    public boolean contains(DateTime moment) {
        return find(moment) != null;
    }

    @Override
    public Iterator<ITimeLineMoment> iterator() {
        return this.timeLineMoments.iterator();
    }

    /**
     * 요소를 추가합니다.  @param moment the moment
     *
     * @param period the period
     */
    protected synchronized final void addPeriod(DateTime moment, ITimePeriod period) {

        ITimeLineMoment item = find(moment);

        if (item == null) {
            item = new TimeLineMoment(moment);
            this.timeLineMoments.add(item);


                log.trace("TimeLineMoment를 추가했습니다. timeLineMoment=[{}]", item);
        }
        item.getPeriods().add(period);
    }

    /**
     * 요소를 제거합니다.  @param moment the moment
     *
     * @param period the period
     */
    protected synchronized final void removePeriod(DateTime moment, ITimePeriod period) {
        ITimeLineMoment item = find(moment);

        if (item != null && item.getPeriods().contains(period)) {
            item.getPeriods().remove(period);
            if (item.getPeriods().size() == 0)
                this.timeLineMoments.remove(item);


                log.trace("TimeLineMoment를 제거했습니다. timeLineMoment=[{}]", item);
        }

    }
}
