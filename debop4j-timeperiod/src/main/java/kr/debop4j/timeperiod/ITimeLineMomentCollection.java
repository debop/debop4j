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
 * {@link ITimeLineMoment} 의 컬렉션
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 16. 오전 1:34
 */
public interface ITimeLineMomentCollection extends Iterable<ITimeLineMoment>, Serializable {

    int size();

    boolean isEmpty();

    ITimeLineMoment getMin();

    ITimeLineMoment getMax();

    ITimeLineMoment get(int index);

    void add(ITimePeriod period);

    void addAll(Iterable<? extends ITimePeriod> periods);

    void remove(ITimePeriod period);

    ITimeLineMoment find(DateTime moment);

    boolean contains(DateTime moment);
}
