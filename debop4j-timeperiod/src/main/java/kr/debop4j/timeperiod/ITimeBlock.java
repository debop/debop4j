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

/**
 * Duration에 따라 시작시각, 완료시각을 계산되는 자료 구조입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:47
 */
public interface ITimeBlock extends ITimePeriod {

    /** 시작시각을 설정합니다. */
    void setStart(DateTime start);

    /** 완료시각을 설정합니다. */
    void setEnd(DateTime end);

    /** 시작시각을 기준으로 기간을 설정합니다. */
    void setDuration(Duration duration);

    /** 기간 설정 */
    void setup(DateTime ns, DateTime ne);

    /** 시작시각은 고정, 기간(duration)으로 완료시각를 재설정 */
    void durationFromStart(Duration newDuration);

    /** 완료시각은 고정, 이전 기간(duration)으로 시작시각을 계산하여, 기간으로 재설정 */
    void durationFromEnd(Duration newDuration);

    /** 지정된 Offset만큼 기간이 이전 시간으로 이동한 TimeBlock을 반환한다. */
    ITimeBlock getPreviousBlock(Duration offset);

    /** 지정된 Offset만큼 기간이 이후 시간으로 이동한 TimeBlock을 반환한다. */
    ITimeBlock getNextBlock(Duration offset);

}
