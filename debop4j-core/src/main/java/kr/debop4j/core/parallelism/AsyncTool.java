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
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * 비동기 작업 관련 Utility Class
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class AsyncTool {

    /** 생성자 */
    private AsyncTool() { }

    private static final ExecutorService executor =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public static final Runnable EMPTY_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            log.trace("EMPTY_RUNNABLE is run.");
        }
    };

    /**
     * 지정한 callable 을 비동기적으로 수행하고, 결과를 담는 FutureTask를 발급합니다.
     * {@link java.util.concurrent.FutureTask#run()} 을 실행시켜야 합니다.
     *
     * @param callable the callable
     * @return the future task
     */
    public static <T> FutureTask<T> newTask(final Callable<T> callable) {
        return new FutureTask<T>(callable);
    }

    /**
     * 지정한 runnable 을 수행하고, FutureTask 를 발급합니다.
     * {@link java.util.concurrent.FutureTask#run()} 을 실행시켜야 합니다.
     *
     * @param runnable the runnable
     * @param result   the result
     * @return the future task
     */
    public static <T> FutureTask<T> newTask(final Runnable runnable, final T result) {
        return new FutureTask<T>(runnable, result);
    }

    /**
     * 지정한 runnable 을 수행하고, Future 를 발급합니다.
     * {@link java.util.concurrent.FutureTask#run()} 을 실행시켜야 합니다.
     *
     * @param runnable the runnable
     * @return the future task
     */
    public static FutureTask<Void> newTask(final Runnable runnable) {
        return new FutureTask<Void>(runnable, null);
    }

    /**
     * 새로운 작업을 생성하고, 자동으로 시작합니다.
     *
     * @param callable the callable
     * @return the future
     */
    public static <T> Future<T> startNew(final Callable<T> callable) {
        return executor.submit(callable);
    }

    /**
     * 새로운 작업을 생성하고, 작업을 실행합니다.
     *
     * @param runnable the runnable
     * @param result   the result
     * @return the future
     */
    public static <T> Future<T> startNew(final Runnable runnable, final T result) {
        return executor.submit(runnable, result);
    }

    /**
     * prevTask 가 완료되면, action을 수행합니다.
     *
     * @param prevTask the prev task
     * @param action   the action
     * @param result   the result
     * @return the future
     */
    public static <T, V> Future<V> continueTask(final FutureTask<T> prevTask,
                                                final Action1<T> action,
                                                final @Nullable V result) {
        final Callable<V> chainTask = new Callable<V>() {
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
     *
     * @param prevTask the prev task
     * @param function the function
     * @return the future
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


    /**
     * Gets task has result.
     *
     * @param result the result
     * @return the task has result
     */
    public static <T> FutureTask<T> getTaskHasResult(final T result) {
        return newTask(EMPTY_RUNNABLE, result);
    }

    /**
     * 지정한 시퀀스를 인자로 하는 함수를 수행하고, 결과를 반환하는 {@link java.util.concurrent.FutureTask} 의 리스트를 반환한다.
     *
     * @param elements the elements
     * @param function the function
     * @return the list
     * @throws InterruptedException the interrupted exception
     */
    public static <T, R> List<Future<R>> runAsync(final Iterable<? extends T> elements,
                                                  final Function1<T, R> function) throws InterruptedException {
        shouldNotBeNull(function, "function");

        final List<Callable<R>> tasks = Lists.newArrayList();

        for (final T element : elements) {
            Callable<R> task = new Callable<R>() {
                @Override
                public R call() throws Exception {
                    return function.execute(element);
                }
            };
            tasks.add(task);
        }
        return executor.invokeAll(tasks);
    }

    /**
     * Invoke all.
     *
     * @param tasks the tasks
     * @throws InterruptedException the interrupted exception
     */
    public static <T> void invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        getAll(executor.invokeAll(tasks));
    }

    /**
     * Invoke all.
     *
     * @param tasks   the tasks
     * @param timeout the timeout
     * @param unit    the unit
     * @throws InterruptedException the interrupted exception
     */
    public static <T> void invokeAll(final Collection<? extends Callable<T>> tasks,
                                     final long timeout,
                                     final TimeUnit unit) throws InterruptedException {
        getAll(executor.invokeAll(tasks, timeout, unit));
    }

    /** 비동기 작업들을 실행하고, 작업이 완료되거나 취소될때까지 기다립니다. */
    public static <T> void runAll(final Iterable<? extends Future<T>> tasks) {


            log.trace("비동기 작업들이 완료될 때까지 기다립니다...");

        getAll(tasks);


            log.trace("비동기 작업들이 모두 완료 되었습니다!!!");
    }

    /**
     * 비동기 작업 목록들의 결과값을 모두 취합하여 반환합니다. (동시에 모든 작업을 수행하여, 성능 상 이익입니다.)
     *
     * @param tasks 실행할 작업
     * @param <T>   결과 값 수형
     * @return 결과 값 컬렉션
     */
    public static <T> List<T> getAll(final Iterable<? extends Future<T>> tasks) {


            log.trace("비동기 작업의 결과를 취합합니다...");

        final List<T> results = new CopyOnWriteArrayList<>();

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
     *
     * @param tasks   수행할 작업들
     * @param timeout 제한 시간
     * @param unit    제한 시간의 단위
     * @param <T>     수형
     * @return 결과값 리스트
     */
    public static <T> List<T> getAll(final Iterable<? extends Future<T>> tasks,
                                     final long timeout,
                                     final TimeUnit unit) {

            log.trace("비동기 작업의 결과를 취합합니다... timeout=[{}], unit=[{}]", timeout, unit);

        final List<T> results = new CopyOnWriteArrayList<>();

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
     *
     * @param futures futures
     * @param <T>     결과 수형
     * @throws InterruptedException
     */
    public static <T> void waitAll(final Iterable<? extends Future<T>> futures) throws InterruptedException {
        boolean allCompleted = false;

        while (!allCompleted) {
            allCompleted = Iterables.all(futures, new Predicate<Future<?>>() {
                @Override
                public boolean apply(Future<?> input) {
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
     *
     * @param futureTasks futures
     */
    public static <T> void waitAllTasks(final Iterable<? extends Future<T>> futureTasks) throws InterruptedException {
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
