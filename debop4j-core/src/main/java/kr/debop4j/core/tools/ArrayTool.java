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
import kr.debop4j.core.Guard;
import kr.debop4j.core.cryptography.CryptoTool;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Array 관련 Utility class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
@Slf4j
public final class ArrayTool {

    private ArrayTool() {
    }

    public static final byte[] EmptyByteArray = new byte[0];

    /** 지졍된 배열이 null 이거나 빈 배열이면 true를 반환한다. */
    public static <T> boolean isEmpty(T[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static <T> boolean isEmpty(Iterable<T> iterable) {
        return (iterable == null) || (!iterable.iterator().hasNext());
    }

    public static boolean isEmpty(byte[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static boolean isEmpty(char[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static boolean isEmpty(int[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static boolean isEmpty(long[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static <T> boolean contains(T[] array, T target) {
        for (T item : array)
            if (item.equals(target))
                return true;
        return false;
    }

    public static <T> int indexOf(T[] array, T target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target))
                return i;
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] asArray(Collection<T> collection) {
        Guard.shouldNotBeNull(collection, "collection");

        T[] result = (T[]) java.lang.reflect.Array.newInstance(ReflectTool.getGenericParameterType(collection), collection.size());
        return (T[]) collection.toArray(result);
    }

    public static <T> T[] asArray(Collection<T> collection, Class clazz) {
        Guard.shouldNotBeNull(collection, "collection");

        T[] result = (T[]) java.lang.reflect.Array.newInstance(clazz, collection.size());
        return (T[]) collection.toArray(result);
    }

    public static <T> String asString(Iterable<T> iterable) {
        return StringTool.join(iterable);
    }

    public static byte[] getRandomBytes(int size) {
        return CryptoTool.getRandomBytes(size);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object[] array) {
        List<T> results = Lists.newArrayList();
        for (Object item : array) {
            results.add((T) item);
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Collection<?> collection) {
        List<T> results = Lists.newArrayList();
        for (Object item : collection) {
            results.add((T) item);
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> toSet(Object[] array) {
        Set<T> results = Sets.newHashSet();
        for (Object item : array) {
            results.add((T) item);
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> toSet(Collection<?> collection) {
        Set<T> results = Sets.newHashSet();
        for (Object item : collection) {
            results.add((T) item);
        }
        return results;
    }
}
