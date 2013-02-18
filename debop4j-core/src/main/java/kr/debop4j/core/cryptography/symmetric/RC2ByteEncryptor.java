package kr.debop4j.core.cryptography.symmetric;

/**
 * 알고리즘 (PBEwithSHA1andRC2_40) 를 사용하는 대칭형 알고리즘입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 18
 */
public class RC2ByteEncryptor extends SymmetricByteEncryptorBase {

    @Override
    public String getAlgorithm() {
        return "PBEwithSHA1andRC2_40";
    }
}
