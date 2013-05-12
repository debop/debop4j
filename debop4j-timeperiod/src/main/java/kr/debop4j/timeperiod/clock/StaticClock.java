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

/**
 * 생성자에 의해 지정된 시간을 현재 시간으로 제공합니다. (단위 테스트 실행 시마다 현재 시각이 변경되는 것을 방지하기 위해서 사용합니다)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 7:01
 */
public class StaticClock extends AbstractClock {
    private static final long serialVersionUID = -7274496462870176221L;

    public StaticClock(DateTime now) {
        super(now);
    }
}
