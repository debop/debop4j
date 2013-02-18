package kr.debop4j.core.io;

import kr.debop4j.core.Guard;
import kr.debop4j.core.ISerializer;
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.RC2ByteEncryptor;

/**
 * 객체 직렬화를 수행한 후, 암호화를 수행합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 17
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
