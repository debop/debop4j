package kr.debop4j.core.spring.configuration;

import kr.debop4j.core.compress.BZip2Compressor;
import kr.debop4j.core.compress.DeflateCompressor;
import kr.debop4j.core.compress.GZipCompressor;
import kr.debop4j.core.compress.XZCompressor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ICompressor 를 구현한 클래스들을 Springs Bean 으로 제공하는 Anntated Configuration 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 17
 */
@Slf4j
@Configuration
public class CompressorConfiguration {

    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Bean(name = "bzip2Compressor")
    public BZip2Compressor bzip2Compressor() {
        if (isDebugEnabled)
            log.debug("BZip2Compressor Bean 을 생성합니다.");

        return new BZip2Compressor();
    }

    @Bean(name = "gzipCompressor")
    public GZipCompressor gzipCompressor() {
        if (isDebugEnabled)
            log.debug("GZipCompressor Bean 을 생성합니다.");

        return new GZipCompressor();
    }

    @Bean(name = "deflateCompressor")
    public DeflateCompressor deflateCompressor() {
        if (isDebugEnabled)
            log.debug("DeflateCompressor Bean 을 생성합니다.");

        return new DeflateCompressor();
    }

    @Bean(name = "xzCompressor")
    public XZCompressor xzCompressor() {
        if (isDebugEnabled)
            log.debug("XZCompressor Bean 을 생성합니다.");

        return new XZCompressor();
    }
}
