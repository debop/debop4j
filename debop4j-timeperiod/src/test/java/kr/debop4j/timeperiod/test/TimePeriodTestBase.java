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

package kr.debop4j.timeperiod.test;

import kr.debop4j.timeperiod.test.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.test.TimePeriodTestBase
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 17. 오후 2:08
 */
@Slf4j
public class TimePeriodTestBase {

    public static final DateTime testDate = new DateTime(2000, 10, 2, 13, 45, 53, 673);
    public static final DateTime testDiffDate = new DateTime(2002, 9, 3, 7, 14, 22, 234);
    public static final DateTime testNow = Times.now();
}
