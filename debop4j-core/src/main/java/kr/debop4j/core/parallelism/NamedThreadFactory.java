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

package kr.debop4j.core.parallelism;

import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 이름을 가진 Thread를 생성합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 28
 */
@Slf4j
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    private String prefix;

    /**
     * Instantiates a new Named thread factory.
     *
     * @param prefix the prefix
     */
    public NamedThreadFactory(String prefix) {
        this.prefix = StringTool.isEmpty(prefix) ? "thread-" : prefix + " thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        assert r != null;
        final String threadName = prefix + threadNumber.getAndIncrement();
        if (log.isTraceEnabled())
            log.trace("새로운 thread를 생성합니다. threadName=[{}]", threadName);
        return new Thread(r, threadName);
    }
}
