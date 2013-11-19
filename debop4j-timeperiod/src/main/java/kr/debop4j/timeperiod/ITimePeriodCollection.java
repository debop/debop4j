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

import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.ITimePeriodCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:36
 */
public interface ITimePeriodCollection extends ITimePeriodContainer {

    /**
     * 대상 ITimePeriod 기간에 속하는 기간이 있다면 true를 반환합니다.
     */
    boolean hasInsidePeriods(ITimePeriod target);

    /**
     * 대상 ITimePeriod 기간과 교집합이 존재하면 true를 반환합니다.
     */
    boolean hasOverlapPeriods(ITimePeriod target);

    /**
     * 대상 시각과 교집합이 존재하면 true를 반환합니다.
     */
    boolean hasIntersectionPeriods(DateTime moment);

    /**
     * 대상 ITimePeriod 기간과 교집합이 존재하면 true를 반환합니다.
     */
    boolean hasIntersectionPeriods(ITimePeriod target);

    /**
     * 대상 ITimePeriod 기간을 포함하는 ITimePeriod 들을 열거합니다.
     */
    Iterable<ITimePeriod> insidePeriods(ITimePeriod target);

    Iterable<ITimePeriod> overlapPeriods(ITimePeriod target);

    /**
     * 지정한 moment 시각과 교집합이 존재하는 TimePeriod를 열거합니다.
     */
    Iterable<ITimePeriod> intersectionPeriods(DateTime moment);

    /**
     * 지정한 target 기간과 교집합이 존재하는 TimePeriod를 열거합니다.
     */
    Iterable<ITimePeriod> intersectionPeriods(ITimePeriod target);

    /**
     * 대상 ITimePeriod 와 특정 관계를 가지는 ITimePeriod 요소들을 열거합니다.
     */
    Iterable<ITimePeriod> relationPeriods(ITimePeriod target, PeriodRelation relation, PeriodRelation... relations);
}
