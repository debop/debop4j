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

package kr.debop4j.core.cryptography.disgest;

/**
 * 문자열을 Digest 수행하는 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
public interface IStringDigester {

    /**
     * Digester 알고리즘 ( MD5, SHA-1, SHA-256, SHA-384, SHA-512 )
     */
    String getAlgorithm();

    /**
     * Digester 가 초기화 되었는지 여부, 초기화 된 상태에서는 속성을 변경 못합니다.
     */
    boolean isInitialized();

    /**
     * 메시지를 암호화 합니다.
     */
    String digest(String message);

    /**
     * 지장한 메시지가 암호화된 내용과 일치하는지 확인합니다.
     *
     * @param message 일반 메시지
     * @param digest  암호화된 메시지
     * @return 메시지 일치 여부
     */
    boolean matches(String message, String digest);
}
