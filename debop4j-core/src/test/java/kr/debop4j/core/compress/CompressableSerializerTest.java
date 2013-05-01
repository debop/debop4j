package kr.debop4j.core.compress;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.spring.configuration.CompressorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * 압축 관련 테스트
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 17
 */
@Slf4j
public class CompressableSerializerTest {

    @BeforeClass
    public static void beforeClass() {
        if (Springs.isNotInitialized())
            Springs.initByPackages(CompressorConfiguration.class.getPackage().getName());
    }

    @Test
    public void shouldBeExistsCompressors() {
        Map<String, ICompressor> compressorMap = Springs.getBeansOfType(ICompressor.class);
        Assert.assertNotNull(compressorMap);
        Assert.assertTrue(compressorMap.size() > 0);
    }

    @Test
    public void compressBinarySerializer() {
    }
}
