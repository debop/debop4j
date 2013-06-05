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

package kr.debop4j.core.io;

import kr.debop4j.core.Guard;
import kr.debop4j.core.ISerializer;
import lombok.extern.slf4j.Slf4j;

/**
 *{@link ISerializer} 를 데코레이터 패턴으로, 여러가지 작업을 수행할 수 있도록 합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 17
 */
@Slf4j
public abstract class SerializerDecorator implements ISerializer {

    private final ISerializer serializer;

    /**
     * Instantiates a new Serializer decorator.
     *
     * @param serializer the serializer
     */
    public SerializerDecorator(ISerializer serializer) {
        Guard.shouldNotBeNull(serializer, "serializer");
        this.serializer = serializer;
    }


    @Override
    public byte[] serialize(Object graph) {
        return serializer.serialize(graph);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return serializer.deserialize(bytes, clazz);
    }
}
