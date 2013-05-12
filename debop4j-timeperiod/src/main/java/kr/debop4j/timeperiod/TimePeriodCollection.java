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

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.TimePeriodCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 1:04
 */
@Slf4j
public class TimePeriodCollection extends TimePeriodContainer implements ITimePeriodCollection {

    private static final long serialVersionUID = 106296570654143822L;

    public TimePeriodCollection() {}

    public TimePeriodCollection(Iterable<? extends ITimePeriod> collection) {
        super(collection);
    }

    @Override
    public boolean hasInsidePeriods(ITimePeriod target) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasOverlapPeriods(ITimePeriod target) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasIntersectionPeriods(DateTime moment) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasIntersectionPeriods(ITimePeriod target) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<ITimePeriod> insidePeriods(ITimePeriod target) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<ITimePeriod> overlapPeriods(ITimePeriod target) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<ITimePeriod> intersectionPeriods(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<ITimePeriod> intersectionPeriods(ITimePeriod target) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterable<ITimePeriod> relationPeriods(ITimePeriod target, PeriodRelation relation, PeriodRelation... relations) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDescription(TimeFormatter formatter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
