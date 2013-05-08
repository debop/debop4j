package kr.debop4j.core.cryptography;

import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.spring.configuration.EncryptorConfiguration;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 설명을 추가하세요.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 18
 */
@Slf4j
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = { EncryptorConfiguration.class } )
public class EncryptorTest {

    private static final String PLAIN_TEXT = "동해물과 백두산이 마르고 닳도록~ Hello World! 1234567890";

    @Autowired
    ApplicationContext context;

    @Test
    public void byteEncryptorTest() {
        Collection<ISymmetricByteEncryptor> byteEncryptors = context.getBeansOfType(ISymmetricByteEncryptor.class).values();

        for (ISymmetricByteEncryptor encryptor : byteEncryptors) {
            if (log.isDebugEnabled())
                log.debug("Encryptor=[{}] 를 테스트합니다.", encryptor.getClass().getSimpleName());

            encryptor.setPassword("debop");

            byte[] encryptedBytes = encryptor.encrypt(StringTool.getUtf8Bytes(PLAIN_TEXT));
            byte[] decryptedBytes = encryptor.decrypt(encryptedBytes);

            assertThat(decryptedBytes).isNotNull();
            assertThat(StringTool.getUtf8String(decryptedBytes)).isEqualTo(PLAIN_TEXT);
        }
    }
}
