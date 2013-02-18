package kr.debop4j.core.cryptography.disgest;

import lombok.extern.slf4j.Slf4j;

/**
 * MD5 알고리즘을 이용하여, 문자열을 Digest 합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 18
 */
@Slf4j
public class MD5StringDigester extends StringDigesterBase {

    @Override
    public final String getAlgorithm() {
        return "MD5";
    }
}
