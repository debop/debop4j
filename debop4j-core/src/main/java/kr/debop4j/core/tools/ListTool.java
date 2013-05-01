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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * kr.debop4j.core.tool.ListTool
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 23.
 */
public final class ListTool {

    private ListTool() { }

    /** 컬렉션에서 최소값을 가지는 요소를 구합니다. */
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
        assert comp != null;
        if (coll == null) return null;
        return Collections.max(coll, comp);
    }

    /** 컬렉션에서 최대값을 구합니다. */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        assert comp != null;
        if (coll == null) return null;

        return Collections.max(coll, comp);
    }
}
