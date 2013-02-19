package kr.debop4j.data.hibernate.usertype.cryptography;

import kr.debop4j.core.cryptography.symmetric.DESByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;

/**
 * DES 알고리즘({@link DESByteEncryptor})을 이용하여 속성 값을 암호화하여 16진수 문자열로 저장합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 18
 */
public class DESEncryptorUserType extends AbstractSymmetricEncryptStringUserType {

    private static final ISymmetricByteEncryptor encryptor = new DESByteEncryptor();

    @Override
    public ISymmetricByteEncryptor getEncryptor() {
        return encryptor;
    }
}
