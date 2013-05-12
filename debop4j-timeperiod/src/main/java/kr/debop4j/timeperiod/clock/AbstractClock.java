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

/**
 * 시계를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 7:01
 */
public class AbstractClock implements IClock {

    private static final long serialVersionUID = 8521868359154760695L;

    private DateTime now;

    protected AbstractClock() {}

    protected AbstractClock(DateTime now) {
        this.now = now;
    }

    @Override
    public DateTime now() {
        if (now == null)
            now = DateTime.now();
        return now;
    }

    protected void setNow(DateTime now) {
        this.now = now;
    }

    @Override
    public DateTime today() {
        return now().withTimeAtStartOfDay();
    }

    @Override
    public Duration timeOfDay() {
        return new Duration(now().getMillisOfDay());
    }
}
