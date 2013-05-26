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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import kr.debop4j.timeperiod.test.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * {@link ITimePeriod}를 요소로 가지는 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 1:04
 */
@Slf4j
public class TimePeriodCollection extends TimePeriodContainer implements ITimePeriodCollection {

    private static final long serialVersionUID = 106296570654143822L;

    public TimePeriodCollection() {}

    public TimePeriodCollection(ITimePeriod... collection) {
        super(collection);
    }

    public TimePeriodCollection(Iterable<? extends ITimePeriod> collection) {
        super(collection);
    }

    @Override
    public boolean hasInsidePeriods(ITimePeriod target) {
        shouldNotBeNull(target, "target");
        for (ITimePeriod period : getPeriods())
            if (Times.hasInside(target, period))
                return true;
        return false;
    }

    @Override
    public boolean hasOverlapPeriods(ITimePeriod target) {
        shouldNotBeNull(target, "target");
        for (ITimePeriod period : getPeriods())
            if (Times.overlapsWith(target, period))
                return true;
        return false;
    }

    @Override
    public boolean hasIntersectionPeriods(DateTime moment) {
        for (ITimePeriod period : getPeriods())
            if (Times.hasInside(period, moment))
                return true;
        return false;
    }

    @Override
    public boolean hasIntersectionPeriods(ITimePeriod target) {
        shouldNotBeNull(target, "target");
        for (ITimePeriod period : getPeriods())
            if (Times.intersectsWith(target, period))
                return true;
        return false;
    }

    @Override
    public Iterable<ITimePeriod> insidePeriods(final ITimePeriod target) {
        shouldNotBeNull(target, "target");
        return Iterables.filter(getPeriods(), new Predicate<ITimePeriod>() {
            @Override
            public boolean apply(@Nullable ITimePeriod input) {
                return Times.hasInside(target, input);
            }
        });
    }

    @Override
    public Iterable<ITimePeriod> overlapPeriods(final ITimePeriod target) {
        shouldNotBeNull(target, "target");
        return Iterables.filter(getPeriods(), new Predicate<ITimePeriod>() {
            @Override
            public boolean apply(@Nullable ITimePeriod input) {
                return Times.overlapsWith(target, input);
            }
        });
    }

    @Override
    public Iterable<ITimePeriod> intersectionPeriods(final DateTime moment) {
        shouldNotBeNull(moment, "moment");
        return Iterables.filter(getPeriods(), new Predicate<ITimePeriod>() {
            @Override
            public boolean apply(@Nullable ITimePeriod input) {
                return Times.hasInside(input, moment);
            }
        });
    }

    @Override
    public Iterable<ITimePeriod> intersectionPeriods(final ITimePeriod target) {
        shouldNotBeNull(target, "target");
        return Iterables.filter(getPeriods(), new Predicate<ITimePeriod>() {
            @Override
            public boolean apply(@Nullable ITimePeriod input) {
                return Times.intersectsWith(target, input);
            }
        });
    }

    @Override
    public Iterable<ITimePeriod> relationPeriods(final ITimePeriod target, PeriodRelation relation, PeriodRelation... relations) {
        shouldNotBeNull(target, "target");
        final List<PeriodRelation> filteringRelation = Lists.newArrayList(relations);
        filteringRelation.add(0, relation);

        return Iterables.filter(getPeriods(), new Predicate<ITimePeriod>() {
            @Override
            public boolean apply(@Nullable ITimePeriod input) {
                PeriodRelation targetRelation = Times.getRelation(input, target);
                return filteringRelation.contains(targetRelation);
            }
        });
    }
}
