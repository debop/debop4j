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

import lombok.extern.slf4j.Slf4j;

/**
 * 구현되지 않은 코드에 예외를 발생시켜, 코드 구현을 해야 함을 알려주고, 구현 중에 컴파일 에러로 나타나게 하지 않게 하기 위함이다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 25.
 */
@Slf4j
public class NotImplementedException extends RuntimeException {

    private static final long serialVersionUID = -1276105737644188535L;

    public NotImplementedException() {
        this("구현이 되지 않았습니다. 구현해 주시기 바랍니다.");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
