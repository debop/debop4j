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

import kr.debop4j.timeperiod.TimePeriodChain;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.test.samples.SchoolDay
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 4:16
 */
public class SchoolDay extends TimePeriodChain {

    private static final long serialVersionUID = -5885304176574135417L;

    @Getter private final Lesson lesson1;
    @Getter private final ShortBreak break1;
    @Getter private final Lesson lesson2;
    @Getter private final LargeBreak break2;
    @Getter private final Lesson lesson3;
    @Getter private final ShortBreak break3;
    @Getter private final Lesson lesson4;

    public SchoolDay() {
        this(getDefaultStartDate());
    }

    public SchoolDay(DateTime moment) {
        lesson1 = new Lesson(moment);
        break1 = new ShortBreak(moment);
        lesson2 = new Lesson(moment);
        break2 = new LargeBreak(moment);
        lesson3 = new Lesson(moment);
        break3 = new ShortBreak(moment);
        lesson4 = new Lesson(moment);

        super.add(lesson1);
        super.add(break1);
        super.add(lesson2);
        super.add(break2);
        super.add(lesson3);
        super.add(break3);
        super.add(lesson4);
    }

    private static DateTime getDefaultStartDate() {
        return Times.trimToHour(Times.now(), 8);
    }
}
