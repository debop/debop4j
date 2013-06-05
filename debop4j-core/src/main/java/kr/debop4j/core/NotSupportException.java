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

package kr.debop4j.core;

/**
 * 시스템에서 지원하지 않는 기능을 호출했을 때 발생하는 예외입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 27.
 */
public class NotSupportException extends RuntimeException {

    private static final long serialVersionUID = 231848084992090602L;

    /**
     * Instantiates a new Not support exception.
     */
    public NotSupportException() {
        this("지원하지 않는 코드입니다.");
    }

    public NotSupportException(String message) {
        super(message);
    }
}
