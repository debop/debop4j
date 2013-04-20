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
import kr.debop4j.core.Function;
import kr.debop4j.core.parallelism.AsyncTool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * kr.debop4j.core.tool.With
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 30.
 */
@Slf4j
public final class With {

    private With() { }

    public static void tryAction(Action action) {
        tryAction(action, null, null);
    }

    public static void tryAction(Action action, Action1<Exception> exceptionAction) {
        tryAction(action, exceptionAction, null);
    }

    public static void tryAction(Action action, Action finallyAction) {
        tryAction(action, null, finallyAction);
    }

    public static void tryAction(Action action, Action1<Exception> exceptionAction, Action finallyAction) {
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

    public static <T> void tryAction(Action1<T> action, final T arg) {
        tryAction(action, arg, null, null);
    }

    public static <T> void tryAction(Action1<T> action, final T arg, Action1<Exception> exceptionAction) {
        tryAction(action, arg, exceptionAction, null);
    }

    public static <T> void tryAction(Action1<T> action, final T arg, Action finallyAction) {
        tryAction(action, arg, null, finallyAction);
    }

    public static <T> void tryAction(Action1<T> action, final T arg, Action1<Exception> exceptionAction, Action finallyAction) {
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

    public static <R> R tryFunction(Function<R> func) {
        return tryFunction(func, null, null, null);
    }

    public static <R> R tryFunction(Function<R> func, Function<R> valueFactory) {
        return tryFunction(func, valueFactory, null, null);
    }

    public static <R> R tryFunction(Function<R> func,
                                    Function<R> valueFactory,
                                    Action1<Exception> exceptionAction,
                                    Action finallyAction) {
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

    public static Future<Void> tryActionAsync(final Action action) {
        return tryActionAsync(action, null, null);
    }

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

    public static <R> Future<R> tryFunctionAsync(final Function<R> func) {
        return tryFunctionAsync(func, null, null, null);
    }

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
}
