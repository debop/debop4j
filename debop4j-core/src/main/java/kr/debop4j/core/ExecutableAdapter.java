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
 * kr.debop4j.core.ExecutableAdapter
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 16.
 */
@Slf4j
public class ExecutableAdapter implements Runnable {

    private final Executable executable;
    private Throwable error;
    @Getter
    private boolean done;

    /**
     * Instantiates a new Executable adapter.
     *
     * @param executable the executable
     */
    public ExecutableAdapter(Executable executable) {
        Guard.shouldNotBeNull(executable, "executable");
        this.executable = executable;
    }

    /** Re throw any errrors. */
    public void reThrowAnyErrrors() {
        if (error == null)
            return;

        if (RuntimeException.class.isInstance(error)) {
            throw RuntimeException.class.cast(error);
        } else if (Error.class.isInstance(error)) {
            throw Error.class.cast(error);
        } else {
            throw new ExceptionWrapper(error);
        }
    }

    @Override
    public void run() {
        log.trace("Executable 인스턴스 실행을 시작합니다...");

        done = false;
        error = null;
        try {
            executable.execute();
            log.trace("Executable 인스턴스 실행을 완료했습니다!!!");
        } catch (Throwable t) {
            if (log.isDebugEnabled())
                log.debug("Executable 인스턴스 실행에 실패했습니다!!!", t);
            error = t;
        } finally {
            done = true;
        }
    }


    /**
     * The type Exception wrapper.
     */
    public static class ExceptionWrapper extends RuntimeException {
        private static final long serialVersionUID = 7550028515694121476L;

        public ExceptionWrapper(Throwable cause) {
            super(cause);
        }
    }
}
