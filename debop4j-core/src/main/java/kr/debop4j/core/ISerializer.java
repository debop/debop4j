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
 * 객체를 직렬화/역직렬화를 수행하는 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 22.
 */
public interface ISerializer {

    /**
     * 객체를 직렬화하여 바이트 배열로 변환합니다.
     */
    byte[] serialize(Object graph);

    /**
     * 직렬화된 객체 정보를 역직렬화하여 객체로 변환합니다.
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
