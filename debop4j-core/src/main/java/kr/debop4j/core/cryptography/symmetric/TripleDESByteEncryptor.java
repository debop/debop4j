package kr.debop4j.core.cryptography.symmetric;

/**
 * 알고리즘 (PBEwithSHA1andDESEDE) 를 사용하는 대칭형 알고리즘입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 18
 */
public class TripleDESByteEncryptor extends SymmetricByteEncryptorBase {

    /**
     * PBEwithSHA1andDESEDE
     */
    @Override
    public String getAlgorithm() {
        return "PBEwithSHA1andDESEDE";
    }
}
