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

package kr.debop4j.core;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import static kr.debop4j.core.Guard.shouldNotBeNull;


/**
 * Thread Context 별로 격리된 저장소를 제공합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 12
 */
@Slf4j
public class Local {

    private Local() { }

    private static ThreadLocal<HashMap> threadLocal =
            new ThreadLocal<HashMap>() {
                @Override
                public HashMap initialValue() {
                    if (log.isDebugEnabled())
                        log.debug("현 ThreadContext 에 저장소를 생성합니다...");

                    return Maps.newLinkedHashMap();
                }
            };

    private static HashMap getMap() {
        return threadLocal.get();
    }

    public static Object get(Object key) {
        shouldNotBeNull(key, "key");
        return threadLocal.get().get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key, Class<T> clazz) {
        shouldNotBeNull(key, "key");
        return (T) threadLocal.get().get(key);
    }

    @SuppressWarnings("unchecked")
    public static void put(Object key, Object value) {
        shouldNotBeNull(key, "key");
        if (log.isDebugEnabled())
            log.debug("Local 저장소에 key=[{}], value=[{}]를 저장합니다.", key, value);

        threadLocal.get().put(key, value);
    }

    public static void clear() {
        threadLocal.get().clear();
    }
}
