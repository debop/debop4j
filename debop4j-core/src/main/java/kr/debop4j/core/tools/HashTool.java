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

/**
 * 해시코드를 생성합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 27
 */
public class HashTool {

    public static final int NullValue = 0;
    public static final int OneValue = 1;
    public static final int Factor = 31;

    private static int computeInternal(Object x) {
        return (x != null) ? x.hashCode() : NullValue;
    }

    /**
     * 지정된 객체들의 Hash Code를 조합한 Hash Code를 생성합니다.
     *
     * @param objs 해쉬코드를 생성할 객체 배열
     * @return 조합된 Hash code
     */
    public static int compute(Object... objs) {
        if (objs == null || objs.length == 0)
            return NullValue;

        int hash = NullValue;
        for (Object x : objs) {
            hash = hash * Factor + computeInternal(x);
        }
        return hash;
    }
}
