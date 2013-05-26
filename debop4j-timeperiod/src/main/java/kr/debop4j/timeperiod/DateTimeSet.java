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

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.*;

/**
 * {@link DateTime}을 요소로 정렬한 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오전 12:22
 */
@Slf4j
public class DateTimeSet extends TreeSet<DateTime> implements IDateTimeSet {

    private static final long serialVersionUID = -1067490053852738541L;

    @Getter(lazy = true)
    private static final Comparator<? super DateTime> defaultComparator = new Comparator<DateTime>() {
        @Override
        public int compare(DateTime o1, DateTime o2) {
            return o1.compareTo(o2);
        }
    };

    public DateTimeSet() { }

    public DateTimeSet(Iterable<? extends DateTime> c) {
        super(Lists.newArrayList(c));
    }

    public DateTimeSet(Comparator<? super DateTime> comparator) {
        super(comparator);
    }

    public DateTimeSet(SortedSet<DateTime> s) {
        super(s);
    }

    @Override
    public DateTime getMin() {
        return isEmpty() ? null : first();
    }

    @Override
    public DateTime getMax() {
        return isEmpty() ? null : last();
    }

    @Override
    public Duration getDuration() {
        if (isEmpty()) return null;

        DateTime min = getMin();
        DateTime max = getMax();
        return (min != null && max != null) ? new Duration(min, max) : null;
    }

    @Override
    public boolean isMoment() {
        Duration duration = getDuration();
        return (duration != null) && (duration.compareTo(Duration.ZERO) == 0);
    }

    @Override
    public boolean isAnytime() {
        return getMin() != null &&
                getMin().equals(TimeSpec.MinPeriodTime) &&
                getMax() != null &&
                getMax().equals(TimeSpec.MaxPeriodTime);
    }

    @Override
    public boolean add(DateTime moment) {
        if (log.isTraceEnabled())
            log.trace("새로운 요소를 추가합니다. moment=[{}]", moment);
        return !contains(moment) && super.add(moment);
    }

    @Override
    public void addAll(Iterable<DateTime> moments) {
        super.addAll(Lists.newArrayList(moments));
    }

    /** 지정된 구간의 {@link Duration}을 계산합니다. */
    @Override
    public List<Duration> getDurations(int startIndex, int count) {
        Guard.shouldBePositiveOrZeroNumber(startIndex, "startIndex");
        Guard.shouldBePositiveNumber(count, "count");
        Guard.shouldBe(startIndex < count, "startIndex 는 Count보다 작아야 합니다. startIndex=[%d], count=[%d]", startIndex, count);

        if (log.isTraceEnabled())
            log.trace("duration을 구합니다... startIndex=[{}], count=[{}]", startIndex, count);

        int endIndex = Math.min(startIndex + count, size() - 1);
        List<Duration> durations = Lists.newArrayList();

        Iterator<DateTime> iter = iterator();
        DateTime prevItem = null;

        while (iter.hasNext()) {
            DateTime currItem = iter.next();
            if (prevItem == null) {
                prevItem = currItem;
            } else {
                durations.add(new Duration(prevItem, currItem));
            }
        }
        return durations;
    }

    /**
     * 지정된 시각(moment) 바로 전의 시각을 찾습니다. 없으면 null을 반환합니다.
     *
     * @param moment 기준 시각
     * @return 기준 시각 바로 전의 시각, 없으면 null
     */
    @Override
    public DateTime findPrevious(DateTime moment) {
        if (log.isTraceEnabled())
            log.trace("지정된 시각[{}] 바로 전의 시각을 찾습니다. 없으면 null을 반환합니다.", moment);

        if (isEmpty()) return null;

        Iterator<DateTime> iter = descendingIterator();

        while (iter.hasNext()) {
            DateTime item = iter.next();
            if (item.compareTo(moment) < 0)
                return item;
        }
        return null;
    }

    /**
     * 지정된 시각(moment) 바로 후의 시각을 찾습니다. 없으면 null을 반환합니다.
     *
     * @param moment 기준 시각
     * @return moment 이후의 시각, 없으면 null 반환
     */
    @Override
    public DateTime findNext(DateTime moment) {
        if (log.isTraceEnabled())
            log.trace("지정된 시각[{}] 바루 후의 시각을 찾습니다. 없으면 null을 반환합니다.", moment);

        if (isEmpty()) return null;

        Iterator<DateTime> iter = iterator();
        while (iter.hasNext()) {
            DateTime item = iter.next();
            if (item.compareTo(moment) > 0)
                return item;
        }
        return null;
    }
}
