package kr.debop4j.core.cryptography;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 암호화 관련 Helper Class
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 14
 */
@Slf4j
public class CryptoTool {

    private static final String RandomNumberGeneration = "SHA1PRNG";
    private static final SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance(RandomNumberGeneration);
        } catch (NoSuchAlgorithmException e) {
            if (log.isErrorEnabled())
                log.error("해당 난수 발생 알고리즘을 찾을 수 없습니다. algorithm=" + RandomNumberGeneration);
            throw new RuntimeException(e);
        }
    }

    private CryptoTool() { }

    /**
     * 난수를 byte array 에 채웁니다.
     *
     * @param numBytes 바이트 수
     * @return 난수가 채워진 byte array
     */
    public static byte[] getRandomBytes(int numBytes) {
        assert (numBytes >= 0);
        byte[] bytes = new byte[numBytes];
        if (numBytes > 0)
            random.nextBytes(bytes);
        return bytes;
    }
}
