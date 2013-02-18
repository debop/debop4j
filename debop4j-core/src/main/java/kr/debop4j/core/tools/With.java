package kr.debop4j.core.tools;

import kr.debop4j.core.Action;
import kr.debop4j.core.Action1;
import kr.debop4j.core.Function;
import kr.debop4j.core.Guard;
import kr.debop4j.core.parallelism.AsyncTool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * kr.nsoft.commons.tools.With
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 30.
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
        Guard.shouldNotBeNull(action, "action");
        try {
            action.perform();
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                if (log.isWarnEnabled())
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
        Guard.shouldNotBeNull(action, "action");
        try {
            action.perform(arg);
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                if (log.isWarnEnabled())
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
        Guard.shouldNotBeNull(func, "func");
        try {
            return func.execute();
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                if (log.isWarnEnabled())
                    log.warn("작업 중에 예외가 발생했지만, 무시합니다^^", e);
            }
        } finally {
            if (finallyAction != null)
                finallyAction.perform();
        }
        return (valueFactory != null) ? valueFactory.execute() : null;
    }

    public static void tryActionAsync(final Action action) {
        tryActionAsync(action, null, null);
    }

    public static void tryActionAsync(final Action action, Action1<Exception> exceptionAction, Action finallyAction) {

        Guard.shouldNotBeNull(action, "action");
        try {
            Future<Void> future =
                    AsyncTool.startNew(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            action.perform();
                            return null;
                        }
                    });
            future.get();
        } catch (Exception e) {
            if (exceptionAction != null) {
                exceptionAction.perform(e);
            } else {
                if (log.isWarnEnabled())
                    log.warn("작업 중에 예외가 발생했지만 무시합니다^^", e);
            }
        } finally {
            if (finallyAction != null)
                finallyAction.perform();
        }
    }
}
