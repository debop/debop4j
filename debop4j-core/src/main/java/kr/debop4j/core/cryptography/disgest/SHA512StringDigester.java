package kr.debop4j.core.cryptography.disgest;

import lombok.extern.slf4j.Slf4j;

/**
 * 설명을 추가하세요.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 18
 */
@Slf4j
public class SHA512StringDigester extends StringDigesterBase {

    @Override
    public final String getAlgorithm() {
        return "SHA-512";
    }
}
