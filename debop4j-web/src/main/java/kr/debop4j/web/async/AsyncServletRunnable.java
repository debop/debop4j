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

package kr.debop4j.web.async;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.AsyncContext;

/**
 * 비동기 방식으로 실행하기 위해 {@link AsyncContext}를 이용하여 작업을 정의할 수 있습니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 7. 오후 5:14
 */
public abstract class AsyncServletRunnable implements Runnable {

    @Getter
    @Setter
    private AsyncContext asyncContext;
}
