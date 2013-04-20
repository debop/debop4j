package kr.debop4j.core.cryptography.disgest;

import lombok.extern.slf4j.Slf4j;

/**
 * SHA-384 알고리즘을 이용하여, 문자열을 Digest 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
@Slf4j
public class SHA384StringDigester extends StringDigesterBase {

    @Override
    public final String getAlgorithm() {
        return "SHA-384";
    }
}
