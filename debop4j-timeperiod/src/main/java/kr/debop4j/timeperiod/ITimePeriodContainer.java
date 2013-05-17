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

import java.util.List;

/**
 * kr.debop4j.timeperiod.ITimePeriodContainer
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:33
 */
public interface ITimePeriodContainer extends List<ITimePeriod>, ITimePeriod {

    void setStart(DateTime start);

    void setEnd(DateTime end);

    boolean isReadonly();

    boolean containsPeriod(ITimePeriod target);

    void addAll(Iterable<? extends ITimePeriod> periods);

    void sortByStart(OrderDirection sortDir);

    void sortByEnd(OrderDirection sortDir);

    void sortByDuration(OrderDirection sortDir);
}
