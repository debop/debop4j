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

package kr.debop4j.timeperiod.clock;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;

/**
 * kr.debop4j.timeperiod.clock.IClock
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 7:14
 */
public interface IClock extends Serializable {

    /** 현재 시각 */
    DateTime now();

    /** 현재 시각의 asDate part 만 */
    DateTime today();

    /** 현재 시각의 일자를 제외한 time part 만 */
    Duration timeOfDay();
}
