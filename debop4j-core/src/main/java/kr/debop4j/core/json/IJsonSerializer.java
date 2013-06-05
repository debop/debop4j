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

package kr.debop4j.core.json;

import kr.debop4j.core.ISerializer;

/**
 * JSON 포맷으로 직렬화 / 역직렬화를 수행합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
public interface IJsonSerializer extends ISerializer {

    public static final byte[] EMPTY_BYTES = new byte[0];

    /**
     * JSON 포맷으로 직렬화하여 Json Text 형식의 문자열로 반환합니다.
     *
     * @param graph 직렬화할 객체
     * @return JSON으로 직렬화한 문자열, 객체가 Null이면 null 반환
     */
    String serializeToText(Object graph);

    /**
     * Json Text 형식의 문자열을 역직렬화하여, 객체로 빌드합니다.
     *
     * @param jsonText    JSON으로 직렬화한 문자열
     * @param targetClass 역직렬화할 타입
     */
    <T> T deserializeFromText(String jsonText, Class<T> targetClass);
}
