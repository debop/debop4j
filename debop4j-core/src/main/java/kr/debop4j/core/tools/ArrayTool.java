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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import kr.debop4j.core.cryptography.CryptoTool;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * Array 관련 Utility class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
@Slf4j
@SuppressWarnings("unchecked")
public final class ArrayTool {

    /**
     * 생성자
     */
    private ArrayTool() { }

    /**
     * The constant EMPTY_BYTE_ARRAY.
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * 지졍된 배열이 null 이거나 빈 배열이면 true를 반환한다.
     *
     * @param array the array
     * @return the boolean
     */
    public static <T> boolean isEmpty(final T[] array) {
        return ((array == null) || (array.length == 0));
    }

    /**
     * Is empty.
     *
     * @param iterable the iterable
     * @return the boolean
     */
    public static <T> boolean isEmpty(final Iterable<T> iterable) {
        return (iterable == null) || (!iterable.iterator().hasNext());
    }

    /**
     * Is empty.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(final byte[] array) {
        return ((array == null) || (array.length == 0));
    }

    /**
     * Is empty.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(final char[] array) {
        return ((array == null) || (array.length == 0));
    }

    /**
     * Is empty.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(final int[] array) {
        return ((array == null) || (array.length == 0));
    }

    /**
     * Is empty.
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(final long[] array) {
        return ((array == null) || (array.length == 0));
    }

    /**
     * Contains boolean.
     *
     * @param array  the array
     * @param target the target
     * @return the boolean
     */
    public static <T> boolean contains(final T[] array, final T target) {
        for (T item : array)
            if (item.equals(target))
                return true;
        return false;
    }

    /**
     * Index of.
     *
     * @param array  the array
     * @param target the target
     * @return the int
     */
    public static <T> int indexOf(final T[] array, final T target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target))
                return i;
        }
        return -1;
    }

    /**
     * As array.
     *
     * @param collection the collection
     * @return the t [ ]
     */
    public static <T> T[] asArray(final Collection<T> collection) {
        shouldNotBeNull(collection, "collection");

        T[] result = (T[]) java.lang.reflect.Array.newInstance(ReflectTool.getGenericParameterType(collection), collection.size());
        return collection.toArray(result);
    }

    /**
     * As array.
     *
     * @param collection the collection
     * @param clazz      the clazz
     * @return the t [ ]
     */
    public static <T> T[] asArray(final Collection<T> collection, final Class clazz) {
        shouldNotBeNull(collection, "collection");

        T[] result = (T[]) java.lang.reflect.Array.newInstance(clazz, collection.size());
        return collection.toArray(result);
    }

    /**
     * As string.
     *
     * @param iterable the iterable
     * @return the string
     */
    public static <T> String asString(final Iterable<T> iterable) {
        return StringTool.join(iterable);
    }

    /**
     * Get random bytes.
     *
     * @param size the size
     * @return the byte [ ]
     */
    public static byte[] getRandomBytes(final int size) {
        return CryptoTool.getRandomBytes(size);
    }

    /**
     * To list.
     *
     * @param array the array
     * @return the list
     */
    public static <T> List<T> toList(final Object[] array) {
        List<T> results = Lists.newArrayList();
        for (Object item : array) {
            results.add((T) item);
        }
        return results;
    }

    /**
     * To list.
     *
     * @param collection the collection
     * @return the list
     */
    public static <T> List<T> toList(final Collection<?> collection) {
        List<T> results = Lists.newArrayList();
        for (Object item : collection) {
            results.add((T) item);
        }
        return results;
    }

    /**
     * To set.
     *
     * @param array the array
     * @return the set
     */
    public static <T> Set<T> toSet(final Object[] array) {
        Set<T> results = Sets.newHashSet();
        for (Object item : array) {
            results.add((T) item);
        }
        return results;
    }

    /**
     * To set.
     *
     * @param collection the collection
     * @return the set
     */
    public static <T> Set<T> toSet(final Collection<?> collection) {
        Set<T> results = Sets.newHashSet();
        for (Object item : collection) {
            results.add((T) item);
        }
        return results;
    }
}
