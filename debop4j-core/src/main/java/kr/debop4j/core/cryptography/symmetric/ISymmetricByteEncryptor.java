package kr.debop4j.core.cryptography.symmetric;

/**
 * 설명을 추가하세요.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 18
 */
public interface ISymmetricByteEncryptor {

    /**
     * 대칭형 암호화 알고리즘
     *
     * @return 대칭형 암호화 알고리즘
     */
    String getAlgorithm();

    /**
     * 초기화 여부
     */
    boolean isInitialized();

    /**
     * 비밀번호 지정
     *
     * @param password 비밀번호
     */
    void setPassword(final String password);

    /**
     * 데이터를 암호화합니다.
     *
     * @param plainBytes 암호화할 데이터
     * @return 암호화된 데이터
     */
    byte[] encrypt(byte[] plainBytes);

    /**
     * 암호화된 데이터를 복원합니다.
     *
     * @param encryptedBytes 암호화된 정보
     * @return 복원된 데이터
     */
    byte[] decrypt(byte[] encryptedBytes);
}
