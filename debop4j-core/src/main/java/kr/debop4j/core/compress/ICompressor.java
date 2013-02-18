package kr.debop4j.core.compress;

/**
 * 설명을 추가하세요.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
public interface ICompressor {

    int BUFFER_SIZE = 4096;

    /**
     * 압축 알고리즘 정보
     */
    String getAlgorithm();

    /**
     * 정보를 압축합니다.
     *
     * @param plain 압축할 데이타
     * @return 압축한 바이트 배열
     */
    byte[] compress(byte[] plain);

    /**
     * 압축된 정보를 복원합니다.
     *
     * @param compressed 압축된 데이타 정보
     * @return 압축 해제한 바이트 배열
     */
    byte[] decompress(byte[] compressed);

    /**
     * 문자열을 압축하여, base64 문자열로 만듭니다.
     */
    String compressString(String plainText);

    /**
     * 압축된 base64 문자열을 복원하여 일반 문자열로 만듭니다.
     */
    String decompressString(String compressedBase64);
}