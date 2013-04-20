package kr.debop4j.data.hibernate.usertype.cryptography;

import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.RC2ByteEncryptor;

/**
 * RC2 알고리즘을 이용한 {@link RC2ByteEncryptor} 를 이용하여, 속성 값을 16진수 문자열로 암호화하여 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public class RC2StringUserType extends AbstractSymmetricEncryptStringUserType {

    private static final ISymmetricByteEncryptor encryptor = new RC2ByteEncryptor();

    @Override
    public ISymmetricByteEncryptor getEncryptor() {
        return encryptor;
    }
}
