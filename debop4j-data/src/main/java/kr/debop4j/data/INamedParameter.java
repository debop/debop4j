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

package kr.debop4j.data;

import java.io.Serializable;

/**
 * SQL 실행 문의 인자 정보를 나타내는 인터페이스입니다.
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19
 */
public interface INamedParameter extends Serializable {

    /** 인자 명 */
    String getName();

    /**
     * 인자 명을 설정합니다.
     *
     * @param name 설정할 인자 명
     */
    void setName(String name);

    /** 인자 값 */
    Object getValue();

    /**
     * 인자 값을 설정합니다.
     *
     * @param value 설정할 인자 값
     */
    void setValue(Object value);
}
