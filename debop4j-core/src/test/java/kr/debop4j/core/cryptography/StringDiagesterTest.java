package kr.debop4j.core.cryptography;

import kr.debop4j.core.cryptography.disgest.IStringDigester;
import kr.debop4j.core.spring.configuration.EncryptorConfiguration;
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
public class StringDiagesterTest {

    private static final String MESSAGE = "kth1234!";

    @Autowired ApplicationContext context;

    @Test
    public void stringDigesterTest() {
        Collection<IStringDigester> diagesters = context.getBeansOfType(IStringDigester.class).values();

        for (IStringDigester digester : diagesters) {
            if (log.isDebugEnabled())
                log.debug("Digest message by [{}]", digester.getAlgorithm());
            String digest = digester.digest(MESSAGE);
            assertThat(digester.matches(MESSAGE, digest)).isTrue();
        }
    }
}
