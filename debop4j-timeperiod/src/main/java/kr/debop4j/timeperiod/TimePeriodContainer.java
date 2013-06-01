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

import com.google.common.collect.Iterables;
import kr.debop4j.core.Guard;
import kr.debop4j.core.NotSupportException;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 요소로 {@link ITimePeriod} 를 가지는 컨테이너를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오전 2:20
 */
@Slf4j
public class TimePeriodContainer implements ITimePeriodContainer {

    private static final long serialVersionUID = -7112720659283751048L;

    @Getter
    protected final List<ITimePeriod> periods = new ArrayList<ITimePeriod>();

    public TimePeriodContainer() {}

    public TimePeriodContainer(ITimePeriod... periods) {
        Collections.addAll(this.periods, periods);
    }

    public TimePeriodContainer(Iterable<? extends ITimePeriod> collection) {
        if (collection != null)
            Iterables.addAll(periods, collection);
    }


    @Override
    public void setStart(DateTime value) {
        if (size() > 0)
            move(new Duration(getStart(), value));
    }

    @Override
    public void setEnd(DateTime value) {
        if (size() > 0)
            move(new Duration(getEnd(), value));
    }

    @Override
    public DateTime getStart() {
        if (size() == 0) return TimeSpec.MinPeriodTime;

        DateTime min = periods.get(0).getStart();
        for (ITimePeriod period : periods) {
            if (period.getStart().compareTo(min) < 0) {
                min = period.getStart();
            }
        }
        return min;
    }

    @Override
    public DateTime getEnd() {
        if (size() == 0) return TimeSpec.MaxPeriodTime;

        DateTime max = periods.get(0).getEnd();
        for (ITimePeriod period : periods) {
            if (period.getEnd().compareTo(max) > 0) {
                max = period.getEnd();
            }
        }
        return max;
    }

    @Override
    public Duration getDuration() {
        return hasPeriod() ? new Duration(getStart(), getEnd()) : TimeSpec.MaxDuration;
    }

    @Override
    public boolean hasStart() {
        return getStart() != TimeSpec.MinPeriodTime;
    }

    @Override
    public boolean hasEnd() {
        return getEnd() != TimeSpec.MaxPeriodTime;
    }

    @Override
    public boolean hasPeriod() {
        return hasStart() && hasEnd();
    }

    @Override
    public boolean isMoment() {
        return hasStart() && getStart().equals(getEnd());
    }

    @Override
    public boolean isAnytime() {
        return !hasStart() && !hasEnd();
    }

    @Override
    public boolean isReadonly() {
        return false;
    }

    @Override
    public void setup(DateTime newStart, DateTime newEnd) {
        throw new NotSupportException("TimePeriodContainer에서는 setup 메소드를 지원하지 않습니다.");
    }

    @Override
    public ITimePeriod copy(Duration offset) {
        throw new NotSupportException("TimePeriodContainer에서는 copy 메소드를 지원하지 않습니다.");
    }

    @Override
    public void move(Duration offset) {
        if (offset == null || offset.getMillis() == 0)
            return;

        if (log.isTraceEnabled()) log.trace("모든 기간을 offset[{}] 만큼 이동합니다.", offset);

        for (ITimePeriod period : this.periods)
            period.move(offset);
    }

    @Override
    public boolean isSamePeriod(ITimePeriod other) {
        return getStart().equals(other.getStart()) && getEnd().equals(other.getEnd());
    }

    @Override
    public boolean hasInside(DateTime moment) {
        return Times.hasInside(this, moment);
    }

    @Override
    public boolean hasInside(ITimePeriod other) {
        return Times.hasInside(this, other);
    }

    @Override
    public boolean intersectsWith(ITimePeriod other) {
        return Times.intersectsWith(this, other);
    }

    @Override
    public boolean overlapsWith(ITimePeriod other) {
        return Times.overlapsWith(this, other);
    }

    @Override
    public void reset() {
        this.periods.clear();
    }

    @Override
    public PeriodRelation getRelation(ITimePeriod other) {
        return Times.getRelation(this, other);
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        return Times.getIntersectionRange(this, other);
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        return Times.getUnionRange(this, other);
    }

    /** 지정한 기간을 포함하는지 여부 */
    @Override
    public boolean containsPeriod(ITimePeriod target) {
        Guard.shouldNotBeNull(target, "target");
        for (ITimePeriod period : this.periods)
            if (period.isSamePeriod(target))
                return true;

        return false;
    }

    /**
     * 모든 기간들을 추가합니다.
     *
     * @param periods 추가할 기간들
     */
    @Override
    public void addAll(final Iterable<? extends ITimePeriod> periods) {
        Iterables.addAll(this.periods, periods);
    }

    /**
     * 시작시각으로 정렬을 수행합니다.
     *
     * @param sortDir 정렬 방식 (순차|역순)
     */
    @Override
    public void sortByStart(OrderDirection sortDir) {
        if (sortDir == OrderDirection.ASC) {
            Collections.sort(periods, Times.getStartComparator());
        } else {
            Collections.sort(periods, Times.getStartDescComparator());
        }
    }

    /**
     * 완료시각으로 정렬을 수행합니다.
     *
     * @param sortDir 정렬 방식 (순차|역순)
     */
    @Override
    public void sortByEnd(OrderDirection sortDir) {
        if (sortDir == OrderDirection.ASC) {
            Collections.sort(periods, Times.getEndComparator());
        } else {
            Collections.sort(periods, Times.getEndDescComparator());
        }
    }

    /**
     * Duration 속성값으로 정렬을 수행합니다.
     *
     * @param sortDir 정렬 방식 (순차|역순)
     */
    @Override
    public void sortByDuration(OrderDirection sortDir) {
        if (sortDir == OrderDirection.ASC) {
            Collections.sort(periods, Times.getDurationComparator());
        } else {
            Collections.sort(periods, Times.getDurationDescComparator());
        }
    }

    @Override
    public int compareTo(ITimePeriod o) {
        return getStart().compareTo(o.getStart());
    }

    @Override
    public int size() {
        return periods.size();
    }

    @Override
    public boolean isEmpty() {
        return periods.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return periods.contains(o);
    }

    @Override
    public Iterator<ITimePeriod> iterator() {
        return this.periods.iterator();
    }

    @Override
    public Object[] toArray() {
        return toArray(new TimeRange[size()]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return periods.toArray(a);
    }

    @Override
    public boolean add(ITimePeriod period) {
        return periods.add(period);
    }

    @Override
    public boolean remove(Object o) {
        return periods.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return periods.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends ITimePeriod> c) {
        for (ITimePeriod item : c)
            periods.add(item);
        return true;
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends ITimePeriod> c) {
        int start = index;
        for (ITimePeriod item : c) {
            periods.add(start, item);
            start++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return periods.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return periods.retainAll(c);
    }

    @Override
    public void clear() {
        periods.clear();
    }

    @Override
    public ITimePeriod get(int index) {
        return periods.get(index);
    }

    @Override
    public ITimePeriod set(int index, ITimePeriod element) {
        return periods.set(index, element);
    }

    @Override
    public void add(int index, ITimePeriod element) {
        periods.add(index, element);
    }

    @Override
    public ITimePeriod remove(int index) {
        return periods.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return periods.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return periods.lastIndexOf(o);
    }

    @Override
    public ListIterator<ITimePeriod> listIterator() {
        return periods.listIterator();
    }

    @Override
    public ListIterator<ITimePeriod> listIterator(int index) {
        return periods.listIterator(index);
    }

    @Override
    public List<ITimePeriod> subList(int fromIndex, int toIndex) {
        return getPeriods().subList(fromIndex, toIndex);
    }

}
