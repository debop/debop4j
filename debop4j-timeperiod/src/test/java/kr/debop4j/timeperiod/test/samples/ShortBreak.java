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

package kr.debop4j.timeperiod.test.samples;

import kr.debop4j.timeperiod.TimeBlock;
import kr.debop4j.timeperiod.tools.Durations;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * kr.debop4j.timeperiod.test.samples.ShortBreak
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 3:38
 */
public class ShortBreak extends TimeBlock {

    private static final long serialVersionUID = 7242522879472102764L;

    public static Duration ShortBreakDuration = Durations.minutes(5);

    public ShortBreak(DateTime moment) {
        super(moment, ShortBreakDuration);
    }
}
