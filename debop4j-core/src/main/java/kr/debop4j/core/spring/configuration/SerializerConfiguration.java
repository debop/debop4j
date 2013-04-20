package kr.debop4j.core.spring.configuration;

import kr.debop4j.core.io.BinarySerializer;
import kr.debop4j.core.json.GsonSerializer;
import kr.debop4j.core.json.JacksonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ISerializer 를 구현한 클래스들을 Springs Bean 으로 제공하는 Anntated Configuration 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 17
 */
@Slf4j
@Configuration
public class SerializerConfiguration {

    @Bean(name = "binarySerializer")
    public BinarySerializer binarySerializer() {
        return new BinarySerializer();
    }

    @Bean(name = "gsonSerializer")
    public GsonSerializer gsonSerializer() {
        return new GsonSerializer();
    }

    @Bean(name = "jacksonSerializer")
    public JacksonSerializer jacksonSerializer() {
        return new JacksonSerializer();
    }
}
