package kr.debop4j.core.cryptography;

import com.google.common.collect.Lists;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Assert;
import org.junit.Test;

import java.security.Provider;
import java.security.Security;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

/**
 * jasypt.org 테스트
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 18
 */
@Slf4j
public class JasyptTest {

    private static final String[] DiageterAlgorithms =
            new String[]{"MD5", "SHA", "SHA-256", "SHA-384", "SHA-512"};

    @Test
    public void loadAlgorithms() {
        for (Provider provider : Security.getProviders()) {
            log.debug("Provider=[{}]", provider.getName());
            for (Provider.Service service : provider.getServices())
                log.debug("    Algorithm=[{}]", service.getAlgorithm());
        }
    }

    @Test
    public void loadMessageDigest() {
        final List<String> algos = Lists.newArrayList(Security.getAlgorithms("MessageDigest"));

        for (Object algorithm : sort(algos, on(String.class))) {
            log.debug("MessageDigest Algorithm=[{}]", algorithm);
        }
    }

    @Test
    public void loadCiphers() {
        final List<String> algos = Lists.newArrayList(Security.getAlgorithms("Cipher"));
        for (Object algorithm : sort(algos, on(String.class))) {
            log.debug("Symethric Algorithm=[{}]", algorithm);
        }
    }


    @Test
    public void standardStringDigester() {

        for (String algorithm : DiageterAlgorithms) {
            StandardStringDigester digester = new StandardStringDigester();
            digester.setAlgorithm(algorithm);
            digester.setIterations(10);
            String digest = digester.digest("password");

            Assert.assertFalse(digester.matches("Password", digest));
            Assert.assertFalse(digester.matches("passworD", digest));
            Assert.assertTrue(digester.matches("password", digest));
        }

    }

    @Test
    public void pooledStringDigester() {

        for (String algorithm : DiageterAlgorithms) {
            PooledStringDigester digester = new PooledStringDigester();
            digester.setPoolSize(5);
            digester.setAlgorithm(algorithm);
            digester.setIterations(10);

            for (int i = 0; i < 10; i++) {
                String digest = digester.digest("password");

                Assert.assertFalse(digester.matches("Password", digest));
                Assert.assertFalse(digester.matches("passworD", digest));
                Assert.assertTrue(digester.matches("password", digest));
            }
        }
    }


    private static final String PLAIN_TEXT = "동해물과 백두산이 마르고 닳도록~ Hello World! 1234567890";

    private static final String[] EncryptorAlgorithms =
            new String[]{"AES", "AESWARP", "ARCFOUR", "BLOWFISH", "DES", "DESEDE", "DESEDEWARP",
                    "PBEWITHMD5ANDDES", "PBEWITHMD5ANDTRIPLEDES", "PBEWITHSHA1ANDDESEDE", "PBEWITHSHA1ANDRC2_40", "RC2"};

    @Test
    public void basicTestEncryptor() {

        for (String algorithm : EncryptorAlgorithms) {

            BasicTextEncryptor encryptor = new BasicTextEncryptor();
            encryptor.setPassword("debop");

            String encrypted = encryptor.encrypt(PLAIN_TEXT);
            String decrypted = encryptor.decrypt(encrypted);

            Assert.assertEquals(PLAIN_TEXT, decrypted);
        }
    }

    private static final String[] PBEAlgorithms =
            new String[]{"PBEwithMD5andDES", "PBEwithSHA1andDESEDE", "PBEwithSHA1andRC2_40"};

    @Test
    public void standardPBEStringEncryptor() {
        for (String algorithm : PBEAlgorithms) {
            if (log.isDebugEnabled())
                log.debug("StandardPBEStringEncryptor Algorithm = [{}]", algorithm);

            try {
                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                encryptor.setPassword("debop");
                encryptor.setAlgorithm(algorithm);

                String encryptedText = encryptor.encrypt(PLAIN_TEXT);
                String decryptedText = encryptor.decrypt(encryptedText);

                Assert.assertEquals(PLAIN_TEXT, decryptedText);
            } catch (Exception e) {
                log.error(algorithm + "은 지원하지 않습니다.", e);
            }
        }
    }

    @Test
    public void standardPBEByteEncryptor() {
        for (String algorithm : PBEAlgorithms) {
            if (log.isDebugEnabled())
                log.debug("StandardPBEStringEncryptor Algorithm = [{}]", algorithm);

            try {
                StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor();
                encryptor.setPassword("debop");
                encryptor.setAlgorithm(algorithm);

                byte[] encryptedBytes = encryptor.encrypt(StringTool.getUtf8Bytes(PLAIN_TEXT));
                byte[] decryptedBytes = encryptor.decrypt(encryptedBytes);

                Assert.assertEquals(PLAIN_TEXT, StringTool.getUtf8String(decryptedBytes));
            } catch (Exception e) {
                log.error(algorithm + "은 지원하지 않습니다.", e);
            }
        }
    }
}
