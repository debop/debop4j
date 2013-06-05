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
import org.jasypt.digest.StandardStringDigester;

/**
 * 문자열을 Hash 암호화를 수행한ㄴ 기본 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 18
 */
@Slf4j
public abstract class StringDigesterBase implements IStringDigester {

    private final StandardStringDigester standardStringDigester;

    /** Instantiates a new String digester base. */
    public StringDigesterBase() {
        this(5);
    }

    /**
     * Instantiates a new String digester base.
     *
     * @param iterations the iterations
     */
    public StringDigesterBase(int iterations) {
        standardStringDigester = new StandardStringDigester();
        standardStringDigester.setAlgorithm(getAlgorithm());
        standardStringDigester.setIterations(iterations);

        if (log.isDebugEnabled())
            log.debug("문자열을 암호화하는 [{}] 인스턴스를 생성했습니다. algorithm=[{}], iteration=[{}]",
                    getClass().getName(), getAlgorithm(), iterations);
    }

    abstract public String getAlgorithm();

    /** {@inheritDoc} */
    @Override
    public boolean isInitialized() {
        return standardStringDigester.isInitialized();
    }

    /** {@inheritDoc} */
    @Override
    public String digest(String message) {
        if (log.isTraceEnabled())
            log.trace("문자열을 암호화합니다. message=[{}]", message);
        return standardStringDigester.digest(message);
    }

    /** {@inheritDoc} */
    @Override
    public boolean matches(String message, String digest) {
        boolean match = standardStringDigester.matches(message, digest);
        if (log.isTraceEnabled())
            log.trace("문자열이 암호화된 문자열과 같은 것인지 확인합니다. message=[{}], digest=[{}], match=[{}]", message, digest, match);
        return match;
    }
}
