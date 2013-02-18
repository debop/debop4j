package kr.debop4j.core;

/**
 * Byte 배열의 정보를 문자열로 표현할 때 사용할 형식 (Base64 또는 HexDecimal 방식)
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
public enum BinaryStringFormat {

    /**
     * Base64 인코딩 방식으로 문자열로 표현
     */
    Base64,

    /**
     * 16진수 방식으로 문자열로 표현
     */
    HexDecimal
}
