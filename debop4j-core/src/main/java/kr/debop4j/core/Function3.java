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

/**
 * 인자 3를 받고, 결과를 반환하는 메소드를 가지는 인터페이스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 27.
 */
public interface Function3<T1, T2, T3, R> {

    /**
     * 수행할 함수
     *
     * @param arg1 함수 인자 1
     * @param arg2 함수 인자 2
     * @param arg3 함수 인자 3
     * @return 수행 결과
     */
    R execute(T1 arg1, T2 arg2, T3 arg3);
}
