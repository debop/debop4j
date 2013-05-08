package kr.debop4j.core.io;

import kr.debop4j.core.ISerializer;
import kr.debop4j.core.compress.ICompressor;
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.spring.configuration.CompressorConfiguration;
import kr.debop4j.core.spring.configuration.EncryptorConfiguration;
import kr.debop4j.core.spring.configuration.SerializerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

/**
 * 설명을 추가하세요.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 17
 */
@Slf4j
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = {
        CompressorConfiguration.class,
        EncryptorConfiguration.class,
        SerializerConfiguration.class
} )
public class SerializerTest {

    @Autowired
    ApplicationContext context;

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

        Collection<ICompressor> compressors = context.getBeansOfType(ICompressor.class).values();
        Collection<ISerializer> serializers = context.getBeansOfType(ISerializer.class).values();

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

        Collection<ICompressor> compressors = context.getBeansOfType(ICompressor.class).values();
        Collection<ISerializer> serializers = context.getBeansOfType(ISerializer.class).values();
        Collection<ISymmetricByteEncryptor> encryptors = context.getBeansOfType(ISymmetricByteEncryptor.class).values();

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
