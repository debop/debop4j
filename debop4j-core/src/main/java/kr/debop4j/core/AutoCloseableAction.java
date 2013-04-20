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

/**
 * 자동으로 리소스를 정리하는 메소드를 가진 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 30.
 */
public class AutoCloseableAction implements AutoCloseable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoCloseableAction.class);

    @Getter
    private final Runnable action;
    @Getter
    protected boolean closed;

    public AutoCloseableAction(final Runnable action) {
        assert action != null;
        this.action = action;
        this.closed = false;
    }

    @Override
    public void close() {
        if (closed)
            return;

        try {
            if (log.isTraceEnabled())
                log.trace("AutoCloseable의 close 작업을 수행합니다...");

            if (action != null)
                action.run();

            if (log.isTraceEnabled())
                log.trace("AutoCloseable의 close 작업을 완료했습니다.");

        } catch (Exception e) {
            log.warn("AutoClosesable의 close 작업에 예외가 발생했습니다. 예외는 무시됩니다.", e);
        } finally {
            closed = true;
        }
    }
}
