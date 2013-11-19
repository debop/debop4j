package kr.debop4j.core.spring;

import kr.debop4j.core.compress.DeflateCompressor;
import kr.debop4j.core.compress.GZipCompressor;
import kr.debop4j.core.compress.ICompressor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.core.spring.SpringTestConfig
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 2.
 */
@Slf4j
@Configuration
@ComponentScan(basePackageClasses = { Springs.class })
public class AnnotatedBeanConfig {

    @Bean
    public ICompressor defaultCompressor() {
        return new GZipCompressor();
    }

    @Bean
    public ICompressor deflateCompressor() {
        return new DeflateCompressor();
    }
}
