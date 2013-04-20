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
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.RC2ByteEncryptor;

/**
 * 객체 직렬화를 수행한 후, 암호화를 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 17
 */
public class EncryptableSerializer extends SerializerDecorator {

    private final ISymmetricByteEncryptor encryptor;

    public EncryptableSerializer(ISerializer serializer) {
        this(serializer, new RC2ByteEncryptor());
    }

    public EncryptableSerializer(ISerializer serializer, ISymmetricByteEncryptor encryptor) {
        super(serializer);

        Guard.shouldNotBeNull(encryptor, "encryptor");
        this.encryptor = encryptor;
    }

    @Override
    public byte[] serialize(Object graph) {
        return encryptor.encrypt(super.serialize(graph));
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return super.deserialize(encryptor.decrypt(bytes), clazz);
    }
}
