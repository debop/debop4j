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

package kr.debop4j.web;

import kr.debop4j.core.spring.Springs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 웹 Application 시작 시에 호출되는 Initializer를 이용해서 {@link kr.debop4j.core.spring.Springs#init()} 을 수행하도록 합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 7. 오후 4:08
 */
@Slf4j
public abstract class UnitOfWorkWebApplicationInitializerBase implements WebApplicationInitializer {

    /**
     * Web Application 시작 시 {@link kr.debop4j.core.spring.Springs#init()} 을 수행하도록 합니다.
     *
     * @param servletContext 서브릿 컨텍스트
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        log.info("UnitOfWorkWebApplicationInitializer를 시작합니다...");

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(getWebApplicationAnnotatedConfigurationClass());

        Springs.init(rootContext);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // 이부분은 필요에 따라 변경하면 된다.
        //
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("*.htm");
    }

    /**
     * 재정의 해서 Spring의 Java Configuration 클래스 수형을 반환하게 합니다.
     *
     * @return Spring의 Java Configuration 클래스의 수형
     */
    abstract public Class getWebApplicationAnnotatedConfigurationClass();
}
