package kr.debop4j.core.compress;

import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.spring.configuration.CompressorConfiguration;
import kr.debop4j.core.spring.configuration.SerializerConfiguration;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * {@link ICompressor} TestCase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 12
 */
@Slf4j
public class CompressorTest extends AbstractTest {

    private static final int TextCount = 10;
    protected static final String text = "동해물과 백두산이 마르고 닳도록, 하느님이 부우하사 우리나라 만세~ Hello world!";
    protected static String plainText;

    private static Collection<ICompressor> compressors;

    @BeforeClass
    public static void BeforeClass() {

        if (Springs.isNotInitialized())
            Springs.initByAnnotatedClasses(CompressorConfiguration.class,
                                           SerializerConfiguration.class);

        Map<String, ICompressor> compressorMap = Springs.getBeansOfType(ICompressor.class);
        compressors = compressorMap.values();


        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        byte[] bytes = new byte[1024];

        for (int i = 0; i < TextCount; i++) {
            random.nextBytes(bytes);

            builder.append(StringTool.replicate(text, i));
            builder.append(StringTool.getHexString(bytes));
            builder.append(StringTool.replicate(StringTool.reverse(text), i));
        }
        plainText = builder.toString();
    }

    //	@BenchmarkOptions(concurrency = BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES,
//	                  benchmarkRounds = 1,
//	                  warmupRounds = 1)
    @Test
    public void testCompressors() {
        for (ICompressor compressor : compressors) {
            try {
                compressAndDecompress(compressor);
                compressAndDecompressString(compressor);
            } catch (Exception e) {
                log.error("compressor=" + compressor, e);
            }
        }
    }

    private static void compressAndDecompress(ICompressor compressor) {

        if (log.isDebugEnabled())
            log.debug("압축/복원 테스트를 시작합니다... compressor=[{}]", compressor);

        try {
            byte[] plainBytes = StringTool.getUtf8Bytes(plainText);
            byte[] compressed = compressor.compress(plainBytes);
            Assert.assertNotNull(compressed);

            byte[] decompressedBytes = compressor.decompress(compressed);
            Assert.assertNotNull(plainBytes);

            String decompressedText = StringTool.getUtf8String(decompressedBytes);
            Assert.assertNotNull(decompressedText);

            Assert.assertEquals(plainText, decompressedText);

        } catch (Exception e) {

            log.error("압축/복원 테스트 실패 compressor=" + compressor, e);
        }
    }

    private static void compressAndDecompressString(ICompressor compressor) {

        if (log.isDebugEnabled())
            log.debug("압축/복원 테스트를 시작합니다... compressor=[{}]", compressor);

        try {
            String compressedBase64 = compressor.compressString(plainText);
            Assert.assertNotNull(compressedBase64);

            String decompressedText = compressor.decompressString(compressedBase64);
            Assert.assertNotNull(decompressedText);

            Assert.assertEquals(plainText, decompressedText);

        } catch (Exception e) {

            log.error("압축/복원 테스트 실패 compressor=" + compressor, e);
        }
    }
}
