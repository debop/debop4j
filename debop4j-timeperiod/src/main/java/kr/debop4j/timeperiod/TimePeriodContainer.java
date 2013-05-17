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
import kr.debop4j.timeperiod.tools.TimeTool;
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
    protected final List<ITimePeriod> periods = new ArrayList<>();

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

        for (ITimePeriod period : this.periods)
            period.move(offset);
    }

    @Override
    public boolean isSamePeriod(ITimePeriod other) {
        return getStart().equals(other.getStart()) && getEnd().equals(other.getEnd());
    }

    @Override
    public boolean hasInside(DateTime moment) {
        return TimeTool.hasInside(this, moment);
    }

    @Override
    public boolean hasInside(ITimePeriod other) {
        return TimeTool.hasInside(this, other);
    }

    @Override
    public boolean intersectsWith(ITimePeriod other) {
        return TimeTool.intersectsWith(this, other);
    }

    @Override
    public boolean overlapsWith(ITimePeriod other) {
        return TimeTool.overlapsWith(this, other);
    }

    @Override
    public void reset() {
        this.periods.clear();
    }

    @Override
    public PeriodRelation getRelation(ITimePeriod other) {
        return TimeTool.getRelation(this, other);
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        return TimeTool.getIntersectionRange(this, other);
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        return TimeTool.getUnionRange(this, other);
    }

    @Override
    public boolean containsPeriod(ITimePeriod target) {
        Guard.shouldNotBeNull(target, "target");
        for (ITimePeriod period : this.periods)
            if (period.isSamePeriod(target))
                return true;

        return false;
    }

    @Override
    public void addAll(Iterable<? extends ITimePeriod> periods) {
        Iterables.addAll(this.periods, periods);
    }

    @Override
    public void sortByStart(OrderDirection sortDir) {
        if (sortDir == OrderDirection.ASC) {
            Collections.sort(periods, getStartComparator());
        } else {
            Collections.sort(periods, getStartDescComparator());
        }
    }

    @Override
    public void sortByEnd(OrderDirection sortDir) {
        if (sortDir == OrderDirection.ASC) {
            Collections.sort(periods, getEndComparator());
        } else {
            Collections.sort(periods, getEndDescComparator());
        }
    }

    @Override
    public void sortByDuration(OrderDirection sortDir) {
        if (sortDir == OrderDirection.ASC) {
            Collections.sort(periods, getDurationComparator());
        } else {
            Collections.sort(periods, getDurationDescComparator());
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
        return toArray(new TimeRange[periods.size()]);
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
    public boolean addAll(Collection<? extends ITimePeriod> c) {
        return periods.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends ITimePeriod> c) {
        return periods.addAll(index, c);
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


    @Getter(lazy = true)
    private static final StartComparator startComparator = new StartComparator();
    @Getter(lazy = true)
    private static final StartDescComparator startDescComparator = new StartDescComparator();
    @Getter(lazy = true)
    private static final EndComparator endComparator = new EndComparator();
    @Getter(lazy = true)
    private static final EndDescComparator endDescComparator = new EndDescComparator();
    @Getter(lazy = true)
    private static final DurationComparator durationComparator = new DurationComparator();
    @Getter(lazy = true)
    private static final DurationDescComparator durationDescComparator = new DurationDescComparator();

    public static class StartComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o1.getStart().compareTo(o2.getStart());
        }
    }

    public static class StartDescComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o2.getStart().compareTo(o1.getStart());
        }
    }

    public static class EndComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o1.getEnd().compareTo(o2.getEnd());
        }
    }

    public static class EndDescComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o2.getEnd().compareTo(o1.getEnd());
        }
    }

    public static class DurationComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o1.getDuration().compareTo(o2.getDuration());
        }
    }

    public static class DurationDescComparator implements Comparator<ITimePeriod> {
        @Override
        public int compare(ITimePeriod o1, ITimePeriod o2) {
            return o2.getDuration().compareTo(o1.getDuration());
        }
    }
}
