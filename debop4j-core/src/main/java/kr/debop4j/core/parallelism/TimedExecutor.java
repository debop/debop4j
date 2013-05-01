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

import kr.debop4j.core.Executable;
import kr.debop4j.core.ExecutableAdapter;
import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 시간 제약이 있는 Executor 를 구현했습니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 16.
 */
@Slf4j
public class TimedExecutor {

    @Getter
    private final long timeout;
    @Getter
    private final long checkMilliSeconds;

    /**
     * 수행 시 제한 시간을 제한하여 수행합니다.
     *
     * @param timeout 제한 시간 (Millisecond 단위)
     */
    private TimedExecutor(long timeout) {
        this(timeout, 100);
    }

    /**
     * 수행 시 제한 시간을 두고 작업을 처리하도록 합니다.
     *
     * @param timeout           제한 시간 (Milliseconds 단위)
     * @param checkMilliSeconds 완료 여부 검사 시간 주기 (Milliseconds 딘위)
     */
    private TimedExecutor(long timeout, long checkMilliSeconds) {
        if (log.isDebugEnabled())
            log.debug("TimedExecutor 생성. timeout=[{}] msecs, checkMilliSeconds=[{}] msecs", timeout, checkMilliSeconds);

        this.timeout = timeout;
        this.checkMilliSeconds = Math.max(1, Math.min(timeout, checkMilliSeconds));
    }

    public void execute(Executable executable) throws TimeoutException {
        Guard.shouldNotBeNull(executable, "executable");

        if (log.isDebugEnabled())
            log.debug("제한된 시간[{}] (seconds) 동안 Executable 인스턴스를 수행합니다.", TimeUnit.SECONDS.toSeconds(timeout));

        final ExecutableAdapter adapter = new ExecutableAdapter(executable);
        final Thread separateThread = new Thread(adapter);
        separateThread.start();

        long runningTime = 0L;
        do {
            if (runningTime > timeout) {
                try {
                    executable.timedOut();
                } catch (Exception ignored) {
                }
                throw new TimeoutException();
            }
            try {
                Thread.sleep(checkMilliSeconds);
                runningTime += checkMilliSeconds;
            } catch (InterruptedException ignored) {
            }
        } while (!adapter.isDone());

        adapter.reThrowAnyErrrors();
    }
}
