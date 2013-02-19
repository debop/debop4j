package kr.debop4j.core.cryptography;

import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * CrytpTool 테스트
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 14
 */
@Slf4j
public class CryptoToolTest extends AbstractTest {

    @Test
    public void getRandomBytesTest() throws Exception {

        byte[] bytes = CryptoTool.getRandomBytes(100);
        byte[] bytes2 = CryptoTool.getRandomBytes(100);

        Assert.assertEquals(bytes.length, bytes2.length);
        Assert.assertFalse(Arrays.equals(bytes, bytes2));
    }
}
