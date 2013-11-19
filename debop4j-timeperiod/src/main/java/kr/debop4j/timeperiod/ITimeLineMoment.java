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

import java.io.Serializable;

/**
 * ITimePeriod의 컬렉션인 periods를 필드로 가지며, moment를 기준으로 선행 기간의 수와 후행 기간의 수를 파악합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오전 1:31
 */
public interface ITimeLineMoment extends Serializable {

    /**
     * 특정 시점
     */
    DateTime getMoment();

    /**
     * 기간 컬렉션
     */
    ITimePeriodCollection getPeriods();

    /**
     * 선행 기간 수
     */
    int getStartCount();

    /**
     * 후행 기간 수
     */
    int getEndCount();
}
