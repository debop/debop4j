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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 자동으로 리소스를 정리하는 메소드를 가진 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 30.
 */
public class AutoCloseableAction implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(AutoCloseableAction.class);

    /**
     * close 시에 수행할 actionWhenClosing
     */
    @Getter
    private final Runnable actionWhenClosing;

    /**
     * close 되었는지 여부
     */
    @Getter
    protected boolean closed;

    /**
     * Instantiates a new Auto closeable actionWhenClosing.
     *
     * @param actionWhenClosing closing 시에 수행할 action 입니다. null 이면 수행할 것이 없다는 의미
     */
    public AutoCloseableAction(final Runnable actionWhenClosing) {
        this.actionWhenClosing = actionWhenClosing;
        this.closed = false;
    }

    /**
     * 리소스를 정리할 action을 수행합니다.
     */
    @Override
    public void close() {
        if (closed)
            return;

        try {

            log.trace("AutoCloseable의 close 작업을 수행합니다...");

            if (actionWhenClosing != null)
                actionWhenClosing.run();


            log.trace("AutoCloseable의 close 작업을 완료했습니다.");

        } catch (Throwable t) {
            log.warn("AutoClosesable의 close 작업에 예외가 발생했습니다. 예외는 무시됩니다.", t);
        } finally {
            closed = true;
        }
    }
}
