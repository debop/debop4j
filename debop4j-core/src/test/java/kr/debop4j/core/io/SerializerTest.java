package kr.debop4j.core.io;

import kr.debop4j.core.ISerializer;
import kr.debop4j.core.compress.ICompressor;
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.spring.configuration.CompressorConfiguration;
import kr.debop4j.core.spring.configuration.EncryptorConfiguration;
import kr.debop4j.core.spring.configuration.SerializerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * 설명을 추가하세요.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 17
 */
@Slf4j
public class SerializerTest {

    static List<ICompressor> compressors;
    static List<ISerializer> serializers;
    static List<ISymmetricByteEncryptor> encryptors;

    @BeforeClass
    public static void beforeClass() {
        if (Springs.isNotInitialized())
            Springs.initByAnnotatedClasses(CompressorConfiguration.class,
                                           EncryptorConfiguration.class,
                                           SerializerConfiguration.class);

        compressors = Springs.getBeansByType(ICompressor.class);
        serializers = Springs.getBeansByType(ISerializer.class);
        encryptors = Springs.getBeansByType(ISymmetricByteEncryptor.class);
    }

    private static final Company company;

    static {
        company = new Company();
        company.setCode("KTH");
        company.setName("KT Hitel");
        company.setAmount(10000L);
        company.setDescription("한국통신 하이텔");

        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName("USER_" + i);
            user.setEmployeeNumber("EMPNO_" + i);
            user.setAddress("ADDR_" + i);
            company.getUsers().add(user);
        }
    }


    @Test
    public void compressableSerializeTest() {
        for (ICompressor compressor : compressors) {
            for (ISerializer serializer : serializers) {
                ISerializer cs = new CompressableSerializer(serializer, compressor);

                if (log.isDebugEnabled())
                    log.debug("compressor=[{}], serializer=[{}]", compressor.getClass(), serializer.getClass());

                byte[] bytes = cs.serialize(company);
                Company deserialized = cs.deserialize(bytes, Company.class);

                Assert.assertNotNull(deserialized);
                Assert.assertEquals(deserialized.getCode(), company.getCode());
                Assert.assertEquals(deserialized.getUsers().size(), company.getUsers().size());
            }
        }
    }

    @Test
    public void encryptableSerializeTest() {
        for (ISymmetricByteEncryptor encryptor : encryptors) {
            for (ISerializer serializer : serializers) {
                ISerializer cs = new EncryptableSerializer(serializer, encryptor);

                if (log.isDebugEnabled())
                    log.debug("encryptor=[{}], serializer=[{}]", encryptor.getClass(), serializer.getClass());

                byte[] bytes = cs.serialize(company);
                Company deserialized = cs.deserialize(bytes, Company.class);

                Assert.assertNotNull(deserialized);
                Assert.assertEquals(deserialized.getCode(), company.getCode());
                Assert.assertEquals(deserialized.getUsers().size(), company.getUsers().size());
            }
        }
    }
}
