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

package kr.debop4j.core.tools;

import kr.debop4j.core.Action;
import kr.debop4j.core.Action1;
import kr.debop4j.core.AutoStopwatch;
import kr.debop4j.core.Function;
import kr.debop4j.core.parallelism.AsyncTool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * kr.debop4j.core.tool.With
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 30.
 */
@Slf4j
public final class With {

    private With() { }

    /**
     * try/catch/finally 구문으로 지정된 action을 수행합니다.
     *
     * @param action 실행할 action
     */
    public static void tryAction(final Action action) {
        tryAction(action, null, null);
    }

    /**
     * try/catch/finally 구문으로 지정된 action을 수행합니다.
     *
     * @param action          실행할 action
     * @param exceptionAction 예외 처리용 action
     */
    public static void tryAction(final Action action, final Action1<Exception> exceptionAction) {
        tryAction(action, exceptionAction, null);
    }

    /**
     * try/catch/finally 구문으로 지정된 action을 수행합니다.
     *
     * @param action        실행할 action
     * @param finallyAction 정리 시 수행할 action
     */
    public static void tryAction(final Action action, final Action finallyAction) {
        tryAction(action, null, finallyAction);
    }

    /**
     * try/catch/finally 구문으로 지정된 action을 수행합니다.
     *
     * @param action          실행할 action
     * @param exceptionAction 예외 처리용 action
     * @param finallyAction   정리 시 수행할 action
     */
    public static void tryAction(final Action action, final Action1<Exception> exceptionAction, final Action finallyAction) {
        assert action != null;
        try {
            action.perform();
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                log.warn("예외가 발생했지만, 무시합니다^^", e);
            }
        } finally {
            if (finallyAction != null)
                finallyAction.perform();
        }
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param action 실행할 action
     */
    public static <T> void tryAction(final Action1<T> action, final T arg) {
        tryAction(action, arg, null, null);
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param action          실행할 action
     * @param exceptionAction 예외 처리용 action
     */
    public static <T> void tryAction(final Action1<T> action, final T arg, Action1<Exception> exceptionAction) {
        tryAction(action, arg, exceptionAction, null);
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param action        실행할 action
     * @param finallyAction 정리 시 수행할 action
     */
    public static <T> void tryAction(final Action1<T> action, final T arg, Action finallyAction) {
        tryAction(action, arg, null, finallyAction);
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param action          실행할 action
     * @param exceptionAction 예외 처리용 action
     * @param finallyAction   정리 시 수행할 action
     */
    public static <T> void tryAction(final Action1<T> action,
                                     final T arg,
                                     final Action1<Exception> exceptionAction,
                                     final Action finallyAction) {
        assert action != null;
        try {
            action.perform(arg);
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                log.warn("예외가 발생했지만, 무시합니다^^", e);
            }
        } finally {
            if (finallyAction != null)
                finallyAction.perform();
        }
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param func 실행할 function
     * @return 수행한 결과
     */
    public static <R> R tryFunction(final Function<R> func) {
        return tryFunction(func, null, null, null);
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param func         실행할 function
     * @param valueFactory 작업 실패 시에 제공할 값 생성 factory
     * @return 수행한 결과
     */
    public static <R> R tryFunction(final Function<R> func, final Function<R> valueFactory) {
        return tryFunction(func, valueFactory, null, null);
    }

    /**
     * try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param func            실행할 function
     * @param valueFactory    작업 실패 시에 제공할 값 생성 factory
     * @param exceptionAction 예외 처리용 action
     * @param finallyAction   정리 시 수행할 action
     * @return 수행한 결과
     */
    public static <R> R tryFunction(final Function<R> func,
                                    final Function<R> valueFactory,
                                    final Action1<Exception> exceptionAction,
                                    final Action finallyAction) {
        assert func != null;
        try {
            return func.execute();
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                log.warn("작업 중에 예외가 발생했지만, 무시합니다^^", e);
            }
        } finally {
            if (finallyAction != null)
                finallyAction.perform();
        }
        return (valueFactory != null) ? valueFactory.execute() : null;
    }

    /**
     * 비동기 방식으로 try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param action 실행할 action
     */
    public static Future<Void> tryActionAsync(final Action action) {
        return tryActionAsync(action, null, null);
    }

    /**
     * 비동기 방식으로 try/catch/finally 구문으로 지정된 action 들을 수행합니다.
     *
     * @param action          실행할 action
     * @param exceptionAction 예외 처리용 action
     * @param finallyAction   정리 시 수행할 action
     */
    public static Future<Void> tryActionAsync(final Action action,
                                              final Action1<Exception> exceptionAction,
                                              final Action finallyAction) {
        assert action != null;
        return AsyncTool.startNew(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    action.perform();
                } catch (Exception e) {
                    if (exceptionAction != null) {
                        exceptionAction.perform(e);
                    } else {
                        log.warn("작업 중에 예외가 발생했지만 무시합니다^^", e);
                    }
                } finally {
                    if (finallyAction != null)
                        finallyAction.perform();
                }
                return null;
            }
        });
    }

    /**
     * 비동기 방식으로 try/catch/finally 구문으로 지정된 func 을 수행합니다.
     *
     * @param func 실행할 function
     * @return 수행한 결과
     */
    public static <R> Future<R> tryFunctionAsync(final Function<R> func) {
        return tryFunctionAsync(func, null, null, null);
    }

    /**
     * 비동기 방식으로 try/catch/finally 구문으로 지정된 func 을 수행합니다.
     *
     * @param func            실행할 function
     * @param valueFactory    작업 실패 시에 제공할 값 생성 factory
     * @param exceptionAction 예외 처리용 action
     * @param finallyAction   정리 시 수행할 action
     * @return 수행한 결과
     */
    public static <R> Future<R> tryFunctionAsync(final Function<R> func,
                                                 final Function<R> valueFactory,
                                                 final Action1<Exception> exceptionAction,
                                                 final Action finallyAction) {
        assert func != null;
        return AsyncTool.startNew(new Callable<R>() {
            @Override
            public R call() throws Exception {
                try {
                    return func.execute();
                } catch (Exception e) {
                    if (exceptionAction != null) {
                        exceptionAction.perform(e);
                    } else {
                        log.warn("작업 중에 예외가 발생했지만 무시합니다^^", e);
                    }
                } finally {
                    if (finallyAction != null)
                        finallyAction.perform();
                }
                return (valueFactory != null) ? valueFactory.execute() : null;
            }
        });
    }

    /**
     * {@link AutoStopwatch} 를 이용하여, 주어진 action을 수행하는데 들어간 시간을 측정합니다.
     *
     * @param action 실행할 action
     */
    public static void stopwatch(final Action action) {
        shouldNotBeNull(action, "action");
        try (AutoStopwatch sw = new AutoStopwatch()) {
            action.perform();
        }
    }

    /**
     * {@link AutoStopwatch} 를 이용하여, 주어진 action을 수행하는데 들어간 시간을 측정합니다.
     *
     * @param action 실행할 action
     */
    public static void stopwatch(final String msg, final Action action) {
        shouldNotBeNull(action, "action");
        try (AutoStopwatch sw = new AutoStopwatch(msg)) {
            action.perform();
        }
    }
}
