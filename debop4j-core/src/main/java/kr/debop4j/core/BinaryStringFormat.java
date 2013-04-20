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
 * Byte 배열의 정보를 문자열로 표현할 때 사용할 형식 (Base64 또는 HexDecimal 방식)
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 12
 */
public enum BinaryStringFormat {

    /**
     * Base64 인코딩 방식으로 문자열로 표현
     */
    Base64,

    /**
     * 16진수 방식으로 문자열로 표현
     */
    HexDecimal
}
