package kr.debop4j.core.cryptography.symmetric;

import lombok.extern.slf4j.Slf4j;

/**
 * 알고리즘 (PBEwithMD5andDES) 를 사용하는 대칭형 알고리즘입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
@Slf4j
public class DESByteEncryptor extends SymmetricByteEncryptorBase {

    /**
     * PBEwithMD5andDES
     */
    @Override
    public String getAlgorithm() {
        return "PBEwithMD5andDES";
    }
}
