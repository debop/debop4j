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

package kr.debop4j.web.mvc;

import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring MVC 사용 시 Controller 작업에 대한 인터셉터를 이용하여, UnitOfWork의 시작과 종료를 수행할 수 있습니다.
 * <p/>
 * 참고: http://debop.blogspot.kr/2013/02/spring-mvc-controller.html
 * 예제에는 xml로 환경설정을 하는데 {@link kr.debop4j.web.spring.UnitOfWorkMvcConfigBase} 를 상속받아 환경설정을 해줘야 한다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 7. 오후 3:52
 */
public class UnitOfWorkMvcInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UnitOfWorkMvcInterceptor.class);

    public UnitOfWorkMvcInterceptor() {
        log.info("UnitOfWorkMvcInterceptor를 생성했습니다. Spring MVC 사용 시 UnitOfWorks 를 사용할 수 있습니다.");
    }

    /**
     * MVC Controller 가 수행되기 전에 호출되는 메소드입니다. 여기에서 {@link kr.debop4j.data.hibernate.unitofwork.UnitOfWorks#start()} 초기화 작업을 수행합니다.
     *
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("UnitOfWorks 시작을 요청합니다...");
        try {
            UnitOfWorks.start();
            return UnitOfWorks.isStarted();
        } catch (Exception e) {
            log.error("UnitOfWorks 시작에 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Controller의 메소드가 수행이 완료되고, View 를 호출하기 전에 호출됩니다.
     *
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Nothing to do.
    }

    /**
     * View 작업까지 완료된 후 Client에 응답하기 바로 전에 호출됩니다. 여기에서 {@link kr.debop4j.data.hibernate.unitofwork.UnitOfWorks#stop()} 을 호출합니다.
     *
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("UnitOfWorks 중지를 요청합니다...");
        try {
            UnitOfWorks.stop();
        } catch (Exception e) {
            log.warn("UnitOfWorks를 중지하는데 실패했습니다.", e);
        }
    }
}
