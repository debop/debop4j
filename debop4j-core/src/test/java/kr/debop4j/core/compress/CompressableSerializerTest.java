package kr.debop4j.core.compress;

import kr.debop4j.core.compress.spring.CompressorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 압축 관련 테스트
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 17
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CompressorConfiguration.class })
public class CompressableSerializerTest {

    @Autowired
    ApplicationContext context;

    @Test
    public void shouldBeExistsCompressors() {
        Map<String, ICompressor> compressorMap = context.getBeansOfType(ICompressor.class);
        assertThat(compressorMap).isNotNull();
        assertThat(compressorMap.size()).isGreaterThan(0);
    }
}
