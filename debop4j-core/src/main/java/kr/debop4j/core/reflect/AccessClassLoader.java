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
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AccessClassLoader
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 21
 */
@Slf4j
class AccessClassLoader extends ClassLoader {

    private static final List<AccessClassLoader> accessClassLoaders = Lists.newArrayList();

    /**
     * AccessClassLoger를 생성합니다.
     */
    static AccessClassLoader get(Class type) {
        if (log.isDebugEnabled())
            log.debug("AccessClassLoader를 생성합니다. type=[{}]", type);
        ClassLoader parent = type.getClassLoader();

        // com.google.common.collect.Iterables 를 사용하여 변경해 보세요.
        synchronized (accessClassLoaders) {
            for (AccessClassLoader loader : accessClassLoaders) {
                if (loader.getParent() == parent)
                    return loader;
            }
            AccessClassLoader loader = new AccessClassLoader(parent);
            accessClassLoaders.add(loader);
            return loader;
        }
    }

    private AccessClassLoader(ClassLoader parent) {
        super(parent);
    }

    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Guard.shouldNotBeEmpty(name, "name");
        if (name.equals(FieldAccess.class.getName())) return FieldAccess.class;
        if (name.equals(MethodAccess.class.getName())) return MethodAccess.class;
        if (name.equals(ConstructorAccess.class.getName())) return ConstructorAccess.class;

        //  all other classes come from the ClassLoader
        return super.loadClass(name, resolve);
    }

    Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        Guard.shouldNotBeEmpty(name, "name");
        Guard.shouldNotBeNull(bytes, "bytes");
        try {
            Method method = ClassLoader.class
                    .getDeclaredMethod("defineClass",
                                       new Class[] { String.class, byte[].class, int.class, int.class });
            method.setAccessible(true);

            // NOTE: 꼭 Integer.valueOf() 를 써야 합니다.
            //
            return (Class) method.invoke(getParent(), name, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length));
        } catch (Exception ignored) { }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
