package kr.debop4j.data.hibernate.usertype.cryptography;

import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.TripleDESByteEncryptor;

/**
 * TripleDES 알고리즘을 이용한 {@link TripleDESByteEncryptor} 를 이용하여, 속성 값을 암호화하여 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public class TripleDESStringUserType extends AbstractSymmetricEncryptStringUserType {

    private static final ISymmetricByteEncryptor encryptor = new TripleDESByteEncryptor();

    @Override
    public ISymmetricByteEncryptor getEncryptor() {
        return encryptor;
    }
}
