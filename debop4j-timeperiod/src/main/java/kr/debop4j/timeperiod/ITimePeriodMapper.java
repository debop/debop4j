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
 * 기간의 시작시각, 완료시각에 대해 영역의 포함여부를 조절할 수 있도록 offset에 대한 처리를 제공합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:55
 */
public interface ITimePeriodMapper extends Serializable {

    /** Start offset을 적용합니다. */
    DateTime mapStart(final DateTime moment);

    /** End offset을 적용합니다. */
    DateTime mapEnd(final DateTime moment);

    /** Offset이 적용된 시각에서 Start offset을 제거합니다. */
    DateTime unmapStart(final DateTime moment);

    /** Offset이 적용된 시각에서 End offset을 제거합니다. */
    DateTime unmapEnd(final DateTime moment);
}
