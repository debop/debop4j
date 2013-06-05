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

package kr.debop4j.core.tools;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * 리스크 관련 Utility class
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 23.
 */
public final class ListTool {

    /** 생성자 */
    private ListTool() { }

    /**
     * 컬렉션에서 최소값을 가지는 요소를 구합니다.
     *
     * @param collection 컬렉션
     * @param comparator 비교자
     * @return 최소값 t
     */
    public static <T> T min(final Collection<? extends T> collection, final Comparator<? super T> comparator) {
        shouldNotBeNull(comparator, "comparator");
        if (collection == null) return null;
        return Collections.max(collection, comparator);
    }

    /**
     * 컬렉션에서 최대값을 구합니다.
     *
     * @param collection 컬렉션
     * @param comparator 비교자
     * @return 최대값 t
     */
    public static <T> T max(final Collection<? extends T> collection, final Comparator<? super T> comparator) {
        shouldNotBeNull(comparator, "comparator");
        if (collection == null) return null;

        return Collections.max(collection, comparator);
    }

    /**
     * 지정한 컬렉션에서 필터링 조건에 만족하는 요소만을 반환합니다.
     *
     * @param collection 원본 컬렉션
     * @param predicate  필터링 조건 메소드
     * @return 필터링된 컬렉션
     */
    public static <T> List<T> filter(final Collection<? extends T> collection, final Predicate<? super T> predicate) {
        shouldNotBeNull(collection, "collection");
        shouldNotBeNull(predicate, "predicate");

        List<T> results = Lists.newArrayList();
        for (T item : collection) {
            if (predicate.apply(item))
                results.add(item);
        }
        return results;
    }

    /**
     * 합집합 (collection1 + collection2)
     *
     * @param collection1 the collection 1
     * @param collection2 the collection 2
     * @return the list
     */
    public static <T> List<T> union(final Collection<? extends T> collection1,
                                    final Collection<? extends T> collection2) {
        List<T> results = Lists.newArrayList();

        results.addAll(collection1);
        for (T item : collection2) {
            if (!collection1.contains(item))
                results.add(item);
        }
        return results;
    }

    /**
     * 차집합  (collection1 - collection2)
     *
     * @param collection1 the collection 1
     * @param collection2 the collection 2
     * @return the list
     */
    public static <T> List<T> difference(final Collection<? extends T> collection1,
                                         final Collection<? extends T> collection2) {
        List<T> results = Lists.newArrayList();

        for (T item : collection1) {
            if (!collection2.contains(item))
                results.add(item);
        }
        return results;
    }
}
