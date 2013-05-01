/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
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
