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

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static kr.debop4j.core.Guard.shouldNotBeNull;


/**
 * Reflection 관련 Utility Class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
@Slf4j
public final class ReflectTool {

    private ReflectTool() {}

    /**
     * 객체가 Generic 형식일 경우, 형식인자(type parameter)들을 가져옵니다.
     *
     * @param x 검사할 객체
     * @return 객체가 Generic 형식인 경우, 형식인자의 배열, Generic이 아니면 빈 배열을 반환
     */
    public static Type[] getParameterTypes(Object x) {
        shouldNotBeNull(x, "x");

        if (log.isDebugEnabled())
            log.debug("객체가 Generic 수형이라면 모든 형식인자들을 가져옵니다. clazz=[{}]", x.getClass().getName());
        try {
            ParameterizedType ptype = (ParameterizedType) x.getClass().getGenericSuperclass();
            assert ptype != null : "지정된 객체가 generic 형식이 아닙니다.";
            return ptype.getActualTypeArguments();
        } catch (Exception e) {
            log.warn("Generic 형식의 객체로부터 인자 수형들을 추출하는데 실패했습니다.", e);
            return new Type[0];
        }
    }

    /** 인스턴스가 Generic 형식이라면 첫번째 Type parameter의 수형을 반환한다. */
    public static <T> Class<T> getGenericParameterType(Object x) {
        return getGenericParameterType(x, 0);
    }

    /** 인스턴스가 Generic 형식이라면 index+1 번째 Type parameter의 수형을 반환한다. */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericParameterType(Object x, int index) {
        shouldNotBeNull(x, "x");

        if (log.isDebugEnabled())
            log.debug("인스턴스 [{}]의 [{}] 번째 제너릭 인자 수형을 찾습니다.", x.getClass().getName(), index);

        Type[] types = getParameterTypes(x);

        if (types != null && types.length > index)
            return (Class<T>) types[index];

        throw new UnsupportedOperationException("Generic 형식의 객체로부터 인자 수형들을 추출하는데 실패했습니다.");
    }

    /**
     * 수형이 primitive type 과 호환된다면 Primitive type으로 변경합니다.
     *
     * @param clazz
     * @return
     */
    public static Class toPrimitiveType(Class<?> clazz) {
        if (clazz == Boolean.class)
            return Boolean.TYPE;
        else if (clazz == Character.class)
            return Character.TYPE;
        else if (clazz == Byte.class)
            return Byte.TYPE;
        else if (clazz == Short.class)
            return Short.TYPE;
        else if (clazz == Integer.class)
            return Integer.TYPE;
        else if (clazz == Long.class)
            return Long.TYPE;
        else if (clazz == Float.class)
            return Float.TYPE;
        else if (clazz == Double.class)
            return Double.TYPE;
        else
            return clazz;
    }
}
