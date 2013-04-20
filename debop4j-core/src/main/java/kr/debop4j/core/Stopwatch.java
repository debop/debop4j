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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 실행 시간을 측정하는 Stopwatch 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 12
 */
@Slf4j
public class Stopwatch {

    private static final double NANO_TO_MILLISECOND = 1000_000.0;

    private long startTime;
    private long endTime;

    @Getter
    private long elapsedTime;

    private final boolean runGC;
    private final String message;

    public Stopwatch() {
        this("", false);
    }

    public Stopwatch(String msg) {
        this(msg, false);
    }

    public Stopwatch(boolean runGC) {
        this("", runGC);
    }


    public Stopwatch(String msg, boolean runGC) {
        this.message = msg;
        this.runGC = runGC;
    }

    private void cleanUp() {
        System.gc();
    }

    public void reset() {
        startTime = 0;
        endTime = 0;
        elapsedTime = 0;
    }

    public void start() {
        if (startTime != 0)
            reset();

        if (this.runGC)
            cleanUp();

        startTime = System.nanoTime();
    }

    public double stop() throws IllegalStateException {
        if (startTime == 0)
            throw new IllegalStateException("call start() method at first.");

        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;

        if (log.isDebugEnabled())
            log.debug("{} elapsed time=[{}] msecs", message, nanoToMillis(elapsedTime));

        if (this.runGC)
            cleanUp();

        return elapsedTime / 1000000.0;
    }

    @Override
    public String toString() {
        return message + " elapsed time=[" + nanoToMillis(elapsedTime) + "] msecs";
    }

    private static double nanoToMillis(double nano) {
        return nano / NANO_TO_MILLISECOND;
    }
}
