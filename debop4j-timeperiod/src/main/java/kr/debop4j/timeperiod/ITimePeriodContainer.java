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

import java.util.List;

/**
 * kr.debop4j.timeperiod.ITimePeriodContainer
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:33
 */
public interface ITimePeriodContainer extends List<ITimePeriod>, ITimePeriod {

    /** 시작시각을 설정합니다. */
    void setStart(DateTime start);

    /** 완료시각을 설정합니다. */
    void setEnd(DateTime end);

    /** 읽기전용 여부 */
    boolean isReadonly();

    /** 지정한 기간을 포함하는지 여부 */
    boolean containsPeriod(ITimePeriod target);

    /**
     * 모든 기간들을 추가합니다.
     *
     * @param periods 추가할 기간들
     */
    void addAll(Iterable<? extends ITimePeriod> periods);

    /**
     * 시작시각으로 정렬을 수행합니다.
     *
     * @param sortDir 정렬 방식 (순차|역순)
     */
    void sortByStart(OrderDirection sortDir);

    /**
     * 완료시각으로 정렬을 수행합니다.
     *
     * @param sortDir 정렬 방식 (순차|역순)
     */
    void sortByEnd(OrderDirection sortDir);

    /**
     * Duration 속성값으로 정렬을 수행합니다.
     *
     * @param sortDir 정렬 방식 (순차|역순)
     */
    void sortByDuration(OrderDirection sortDir);
}
