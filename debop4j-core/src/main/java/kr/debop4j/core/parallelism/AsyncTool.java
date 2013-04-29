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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import kr.debop4j.core.Action1;
import kr.debop4j.core.Function1;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * 비동기 작업 관련 Utility Class
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 14
 */
@Slf4j
public class AsyncTool {

    private AsyncTool() { }

    @Getter(lazy = true)
    private static final ExecutorService executor =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Getter(lazy = true)
    private static final Runnable emptyRunnable =
            new Runnable() {
                @Override
                public void run() {
                    if (log.isDebugEnabled())
                        log.debug("emptyRunnable is runEach");
                }
            };

    /**
     * 지정한 callable 을 비동기적으로 수행하고, 결과를 담는 FutureTask를 발급합니다.
     * {@link java.util.concurrent.FutureTask#run()} 을 실행시켜야 합니다.
     */
    public static <T> FutureTask<T> newTask(Callable<T> callable) {
        return new FutureTask<T>(callable);
    }

    /**
     * 지정한 runnable 을 수행하고, FutureTask 를 발급합니다.
     * {@link java.util.concurrent.FutureTask#run()} 을 실행시켜야 합니다.
     */
    public static <T> FutureTask<T> newTask(Runnable runnable, T result) {
        return new FutureTask<T>(runnable, result);
    }

    /**
     * 지정한 runnable 을 수행하고, Future 를 발급합니다.
     * {@link java.util.concurrent.FutureTask#run()} 을 실행시켜야 합니다.
     */
    public static FutureTask<Void> newTask(Runnable runnable) {
        return new FutureTask<Void>(runnable, null);
    }

    /**
     * 새로운 작업을 생성하고, 자동으로 시작합니다.
     */
    public static <T> Future<T> startNew(Callable<T> callable) {
        return getExecutor().submit(callable);
    }

    /**
     * 새로운 작업을 생성하고, 작업을 실행합니다.
     */
    public static <T> Future<T> startNew(Runnable runnable, @Nullable T result) {
        return getExecutor().submit(runnable, result);
    }

    /**
     * prevTask 가 완료되면, action을 수행합니다.
     */
    public static <T, V> Future<V> continueTask(final FutureTask<T> prevTask,
                                                final Action1<T> action,
                                                final @Nullable V result) {
        Callable<V> chainTask = new Callable<V>() {
            @Override
            public V call() throws Exception {
                T prev = prevTask.get();
                action.perform(prev);
                return result;
            }
        };

        return startNew(chainTask);
    }

    /**
     * prevTask의 실행 결과 값을 받아 후속 function에서 작업하고 결과를 반환합니다.
     */
    public static <T, V> Future<V> continueTask(final FutureTask<T> prevTask,
                                                final Function1<T, V> function) {
        return startNew(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return function.execute(prevTask.get());
            }
        });
    }


    public static <T> FutureTask<T> getTaskHasResult(T result) {
        return newTask(getEmptyRunnable(), result);
    }

    /**
     * 지정한 시퀀스를 인자로 하는 함수를 수행하고, 결과를 반환하는 {@link java.util.concurrent.FutureTask} 의 리스트를 반환한다.
     */
    public static <T, R> List<Future<R>> runAsync(final Iterable<? extends T> elements,
                                                  final Function1<T, R> function) throws InterruptedException {
        shouldNotBeNull(function, "function");

        List<Callable<R>> tasks = Lists.newArrayList();

        for (final T element : elements) {
            Callable<R> task = new Callable<R>() {
                @Override
                public R call() throws Exception {
                    return function.execute(element);
                }
            };

            tasks.add(task);
        }
        return getExecutor().invokeAll(tasks);
    }

    public static <T> void invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        getAll(getExecutor().invokeAll(tasks));
    }

    public static <T> void invokeAll(Collection<? extends Callable<T>> tasks,
                                     long timeout,
                                     TimeUnit unit) throws InterruptedException {
        getAll(getExecutor().invokeAll(tasks, timeout, unit));
    }

    /**
     * 비동기 작업들을 실행하고, 작업이 완료되거나 취소될때까지 기다립니다.
     */
    public static <T> void runAll(Iterable<? extends Future<T>> tasks) {

        if (log.isDebugEnabled())
            log.debug("비동기 작업들이 완료될 때까지 기다립니다...");

        getAll(tasks);

        if (log.isDebugEnabled())
            log.debug("비동기 작업들이 모두 완료 되었습니다!!!");
    }

    /**
     * 비동기 작업 목록들의 결과값을 모두 취합하여 반환합니다. (동시에 모든 작업을 수행하여, 성능 상 이익입니다.)
     */
    public static <T> List<T> getAll(Iterable<? extends Future<T>> tasks) {

        if (log.isDebugEnabled())
            log.debug("비동기 작업의 결과를 취합합니다...");

        List<T> results = new CopyOnWriteArrayList<>();

        try {
            for (Future<T> future : tasks) {
                results.add(future.get());
            }
        } catch (Exception e) {

            log.error("비동기 작업 시 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * 비동기 작업 목록들의 결과값을 모두 취합하여 반환합니다. (동시에 모든 작업을 수행하여, 성능 상 이익입니다.)
     */
    public static <T> List<T> getAll(Iterable<? extends Future<T>> tasks,
                                     long timeout,
                                     TimeUnit unit) {
        if (log.isDebugEnabled())
            log.debug("비동기 작업의 결과를 취합합니다... timeout=[{}], unit=[{}]", timeout, unit);

        List<T> results = new CopyOnWriteArrayList<>();

        try {
            for (final Future<T> task : tasks) {
                results.add(task.get(timeout, unit));
            }
        } catch (Exception e) {

            log.error("비동기 작업 시 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
        return results;
    }

    /**
     * 모든 {@link java.util.concurrent.Future}의 {@link java.util.concurrent.Future#isDone()}
     * 또는 {@link java.util.concurrent.Future#isCancelled()} 가 될 때까지 기다립니다.
     */
    public static <T> void waitAll(Iterable<? extends Future<T>> futures) throws InterruptedException {
        boolean allCompleted = false;

        while (!allCompleted) {
            allCompleted = Iterables.all(futures, new Predicate<Future<?>>() {
                @Override
                public boolean apply(@Nullable Future<?> input) {
                    assert input != null;
                    return input.isDone() || input.isCancelled();
                }
            });
            if (!allCompleted)
                Thread.sleep(1);
        }
    }

    /**
     * 모든 {@link java.util.concurrent.FutureTask}의 {@link java.util.concurrent.Future#isDone()}
     * 또는 {@link java.util.concurrent.Future#isCancelled()} 가 될 때까지 기다립니다.
     */
    public static <T> void waitAllTasks(Iterable<? extends Future<T>> futureTasks) throws InterruptedException {
        boolean allCompleted = false;

        while (!allCompleted) {
            allCompleted = Iterables.all(futureTasks, new Predicate<Future<T>>() {
                @Override
                public boolean apply(Future<T> input) {
                    assert input != null;
                    return (input.isDone() || input.isCancelled());
                }
            });

            if (!allCompleted)
                Thread.sleep(1);
        }
    }
}
