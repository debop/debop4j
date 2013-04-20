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
 * 성능 측정을 자동으로 수행하는 {@link Stopwatch} 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 12
 */
@Slf4j
public class AutoStopwatch implements AutoCloseable {

    private final Stopwatch stopwatch;

    public AutoStopwatch() {
        this("");
    }

    public AutoStopwatch(String msg) {
        stopwatch = new Stopwatch(msg);
        stopwatch.start();
    }

    @Override
    public void close() {
        try {
            stopwatch.stop();
        } catch (Exception ignored) {
        }
    }
}
