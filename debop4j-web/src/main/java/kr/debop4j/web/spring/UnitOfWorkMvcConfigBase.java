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

package kr.debop4j.web.spring;

import kr.debop4j.web.mvc.UnitOfWorkMvcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * web.xml 을 Java Configuration으로 작업할 수 있도록 합니다.
 * <p/>
 * 참고: http://stackoverflow.com/questions/16081861/spring-interceptor-works-with-xml-but-not-with-config-class
 * 참고: http://gitblog.ihoney.pe.kr/blog/2013/01/11/spring-3-dot-1-mvc-example/
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 7. 오후 4:58
 */
@EnableWebMvc
@Configuration
public class UnitOfWorkMvcConfigBase extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UnitOfWorkMvcInterceptor uow = new UnitOfWorkMvcInterceptor();
        registry.addInterceptor(uow);
        super.addInterceptors(registry);
    }
}
