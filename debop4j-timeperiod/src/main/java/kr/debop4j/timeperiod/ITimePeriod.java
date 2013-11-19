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
import org.joda.time.Duration;

import java.io.Serializable;

/**
 * 기간 (시작시각 ~ 완료시각) 을 나타내는 인터페이스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 10:49
 */
public interface ITimePeriod extends Comparable<ITimePeriod>, Serializable {

    /**
     * 시작 시각
     */
    DateTime getStart();

    /**
     * 종료 시각
     */
    DateTime getEnd();

    /**
     * 기간
     */
    Duration getDuration();

    /**
     * 시작 시각이 있는지 여부
     */
    boolean hasStart();

    /**
     * 종료 시각이 있는지 여부
     */
    boolean hasEnd();

    /**
     * 기간이 있는지 여부
     */
    boolean hasPeriod();

    /**
     * 시작시각과 완료시각의 값이 있고, 같은 값인가?
     */
    boolean isMoment();

    /**
     * 시작시각과 완료시각 모두 정해지지 않은 경우
     */
    boolean isAnytime();

    /**
     * 읽기 전용 여부
     */
    boolean isReadonly();

    /**
     * 기간을 새로운 값으로 설정합니다.
     */
    void setup(DateTime ns, DateTime ne);

    /**
     * 기간을 offset만큼 이동한 새로운 인스턴스를 반환합니다.
     */
    ITimePeriod copy(Duration offset);

    /**
     * 기간을 offset만큼 이동시킵니다.
     */
    void move(Duration offset);

    /**
     * 시작과 완료 시각이 같은지 여부
     */
    boolean isSamePeriod(ITimePeriod other);

    /**
     * 기간안에 moment 가 속하는지 여부
     */
    boolean hasInside(DateTime moment);

    /**
     * 기간안에 other 기간이 속하는지 여부
     */
    boolean hasInside(ITimePeriod other);

    /**
     * 두 기간이 교차하거나, 기간 target의 내부 구간이면 true를 반환합니다.
     */
    boolean intersectsWith(ITimePeriod other);

    /**
     * 기간과 other 기간이 교집합에 해당하는 부분이 있는지 여부
     */
    boolean overlapsWith(ITimePeriod other);

    /**
     * 기간을 미정으로 초기화합니다.
     */
    void reset();

    /**
     * 다른 ITimePeriod 와의 관계를 나타냅니다.
     */
    PeriodRelation getRelation(ITimePeriod other);

    /**
     * 두 기간이 겹치는 (교집합) 기간을 반환합니다.
     */
    ITimePeriod getIntersection(ITimePeriod other);

    /**
     * 두 기간의 합집합 기간을 반환합니다.
     */
    ITimePeriod getUnion(ITimePeriod other);

}
