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
import kr.debop4j.core.compress.GZipCompressor;
import kr.debop4j.core.compress.ICompressor;
import lombok.extern.slf4j.Slf4j;

/**
 *{@link ISerializer} 를 통해 직렬화/역직렬환 정보를 압축/복원을 수행하여 전달한다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 17
 */
@Slf4j
public class CompressableSerializer extends SerializerDecorator {

    private final ICompressor compressor;

    /**
     * Instantiates a new Compressable serializer.
     *
     * @param serializer the serializer
     */
    public CompressableSerializer(ISerializer serializer) {
        this(serializer, new GZipCompressor());
    }

    /**
     * Instantiates a new Compressable serializer.
     *
     * @param serializer the serializer
     * @param compressor the compressor
     */
    public CompressableSerializer(ISerializer serializer, ICompressor compressor) {
        super(serializer);
        Guard.shouldNotBeNull(compressor, "compressor");
        this.compressor = compressor;
    }

    @Override
    public byte[] serialize(Object graph) {
        return compressor.compress(super.serialize(graph));
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return super.deserialize(compressor.decompress(bytes), clazz);
    }
}
