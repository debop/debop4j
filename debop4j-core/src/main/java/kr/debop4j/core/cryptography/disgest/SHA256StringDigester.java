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

import lombok.extern.slf4j.Slf4j;

/**
 * SHA-256 알고리즘을 이용하여, 문자열을 Digest 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
@Slf4j
public class SHA256StringDigester extends StringDigesterBase {

    /**
     * SHA-256
     *
     * @return "SHA-256"
     */
    @Override
    public final String getAlgorithm() {
        return "SHA-256";
    }
}
