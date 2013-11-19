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

package kr.debop4j.data.mysql;

import java.lang.annotation.*;

/**
 * ReadOnlyConnection 를 정의된 메소드는
 * {@link java.sql.Connection#isReadOnly} 속성을 true로 하여, MySQL의 Slave 서버에 접속하도록 합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 5. 오후 11:08
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ReadOnlyConnection {
}
