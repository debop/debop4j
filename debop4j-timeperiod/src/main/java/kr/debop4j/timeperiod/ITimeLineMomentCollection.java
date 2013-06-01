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

    /**
     * get size of collection.
     *
     * @return size
     */
    int size();

    /**
     * Is empty.
     *
     * @return the boolean
     */
    boolean isEmpty();

    /**
     * Gets min.
     *
     * @return the min
     */
    ITimeLineMoment getMin();

    /**
     * Gets max.
     *
     * @return the max
     */
    ITimeLineMoment getMax();

    /**
     * Get i time line moment.
     *
     * @param index the index
     * @return the i time line moment
     */
    ITimeLineMoment get(int index);

    /**
     * Add void.
     *
     * @param period the period
     */
    void add(ITimePeriod period);

    /**
     * Add all.
     *
     * @param periods the periods
     */
    void addAll(Iterable<? extends ITimePeriod> periods);

    /**
     * Remove void.
     *
     * @param period the period
     */
    void remove(ITimePeriod period);

    /**
     * Find i time line moment.
     *
     * @param moment the moment
     * @return the i time line moment
     */
    ITimeLineMoment find(DateTime moment);

    /**
     * Contains boolean.
     *
     * @param moment the moment
     * @return the boolean
     */
    boolean contains(DateTime moment);
}
