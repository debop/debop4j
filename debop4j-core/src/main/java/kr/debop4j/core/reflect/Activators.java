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

package kr.debop4j.core.reflect;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 리플렉션을 이용하여, 객체를 생성시키는 Utility Class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
@Slf4j
public final class Activators {

    private Activators() { }

    public static Object createInstance(String className) {
        try {
            return createInstance(Class.forName(className));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 지정된 수형의 새로운 인스턴스를 생성합니다.
     *
     * @param clazz 생성할 수형
     * @param <T>   수형
     * @return 지정한 수형의 새로운 인스턴스, 생성 실패시에는 null을 반환합니다.
     */
    public static <T> T createInstance(Class<T> clazz) {
        Guard.shouldNotBeNull(clazz, "clazz");
        if (log.isTraceEnabled())
            log.trace("수형 [{}] 의 새로운 인스턴스를 생성합니다...", clazz.getName());

        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            log.warn(clazz.getName() + " 수형을 생성하는데 실패했습니다.", e);
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> clazz, Object... initArgs) {
        Guard.shouldNotBeNull(clazz, "clazz");
        if (log.isTraceEnabled())
            log.trace("[{}] 수형의 객체를 생성합니다. initArgs=[{}]", clazz.getName(), StringTool.listToString(initArgs));
        if (initArgs == null || initArgs.length == 0)
            return createInstance(clazz);

        try {
            List<Class<?>> parameterTypes = Lists.newArrayList();
            for (Object arg : initArgs) {
                Class<?> argClass = arg.getClass();
                if (argClass == Integer.class)
                    argClass = Integer.TYPE;
                if (argClass == Long.class)
                    argClass = Long.TYPE;
                parameterTypes.add(argClass);
            }
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor(ArrayTool.asArray(parameterTypes, Class.class));
                return (constructor != null)
                        ? constructor.newInstance(initArgs)
                        : null;
            } catch (Throwable ignored) {
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                for (Constructor<?> ctor : constructors) {
                    if (ctor.getParameterTypes().length == parameterTypes.size())
                        return (T) ctor.newInstance(initArgs);
                }
            }
        } catch (Exception e) {
            log.error(clazz.getName() + " 수형을 생성하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        if (log.isTraceEnabled())
            log.trace("[{}] 수형의 생성자를 구합니다. parameterTypes=[{}]",
                      clazz.getName(), StringTool.listToString(parameterTypes));
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
