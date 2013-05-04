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

package org.hibernate.ogm.helper;

import org.hibernate.redis.serializer.JacksonRedisSerializer;

import java.util.Map;

/**
 * Json 변환 관련 Heler class 입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 9:16
 */
public class JsonHelper {

    private final JacksonRedisSerializer serializer = new JacksonRedisSerializer();

    public byte[] toJson(Object graph) {
        return serializer.serialize(graph);
    }

    @SuppressWarnings( "unchecked" )
    public Map<String, Object> createAssociation(byte[] associationAsJson) {
        return (Map<String, Object>) serializer.deserialize(associationAsJson);
    }

}
