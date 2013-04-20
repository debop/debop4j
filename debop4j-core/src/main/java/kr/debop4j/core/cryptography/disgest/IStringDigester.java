package kr.debop4j.core.cryptography.disgest;

/**
 * 설명을 추가하세요.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
public interface IStringDigester {

    /**
     * Digester 알고리즘 ( MD5, SHA-1, SHA-256, SHA-384, SHA-512 )
     */
    String getAlgorithm();

    /**
     * Digester 가 초기화 되었는지 여부, 초기화 된 상태에서는 속성을 변경 못합니다.
     */
    boolean isInitialized();

    /**
     * 메시지를 암호화 합니다.
     */
    String digest(String message);

    /**
     * 지장한 메시지가 암호화된 내용과 일치하는지 확인합니다.
     *
     * @param message 일반 메시지
     * @param digest  암호화된 메시지
     * @return 메시지 일치 여부
     */
    boolean matches(String message, String digest);
}
