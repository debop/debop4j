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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kr.debop4j.core.Action1;
import kr.debop4j.core.Function1;
import kr.debop4j.core.collection.NumberRange;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static kr.debop4j.core.Guard.shouldNotBeNull;


/**
 * 대량 데이터에 대한 병렬 실행을 수행할 수 있도록 해주는 Class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 26.
 */
public abstract class Parallels {

    private static final Logger log = LoggerFactory.getLogger(Parallels.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private Parallels() { }

    @Getter(lazy = true)
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    @Getter(lazy = true)
    private static final int processCount = Runtime.getRuntime().availableProcessors();
    @Getter(lazy = true)
    private static final int workerCount = getProcessCount() * 2;

    /**
     * Create executor.
     *
     * @return the executor service
     */
    public static ExecutorService createExecutor() {
        return createExecutor(getWorkerCount());
    }

    /**
     * Create executor.
     *
     * @param threadCount the thread count
     * @return the executor service
     */
    public static ExecutorService createExecutor(int threadCount) {
        return Executors.newFixedThreadPool(threadCount);
    }

    private static int getPartitionSize(int itemCount, int partitionCount) {
        return (itemCount / partitionCount) + ((itemCount % partitionCount) > 0 ? 1 : 0);
    }

    /**
     * 지정한 작업을 병렬로 수행합니다.  @param count the count
     *
     * @param runnable the runnable
     */
    public static void run(int count, final Runnable runnable) {
        run(0, count, runnable);
    }

    /**
     * Run void.
     *
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param action      the action
     */
    public static void run(int fromInclude, int toExclude, final Runnable action) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        run(fromInclude, toExclude, step, action);
    }

    /**
     * Run void.
     *
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param step        the step
     * @param runnable    the runnable
     */
    public static void run(int fromInclude, int toExclude, int step, final Runnable runnable) {
        assert runnable != null;

        if (log.isDebugEnabled())
            log.debug("병렬로 작업을 수행합니다... fromInclude=[{}], toExclude=[{}], step=[{}], workerCount=[{}]",
                      fromInclude, toExclude, step, getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getWorkerCount());
            List<Callable<Void>> tasks = Lists.newLinkedList();

            for (final NumberRange.IntRange partition : partitions) {
                Callable<Void> task =
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                for (final int element : partition)
                                    runnable.run();
                                return null;
                            }
                        };
                tasks.add(task);
            }

            List<Future<Void>> results = executor.invokeAll(tasks);
            for (Future<Void> result : results) {
                result.get();
            }

            if (log.isDebugEnabled())
                log.debug("병렬 작업을 수행하였습니다!");

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Run void.
     *
     * @param count  the count
     * @param action the action
     */
    public static void run(int count, final Action1<Integer> action) {
        run(0, count, action);
    }

    /**
     * Run void.
     *
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param action      the action
     */
    public static void run(int fromInclude, int toExclude, final Action1<Integer> action) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        run(fromInclude, toExclude, step, action);
    }

    /**
     * Run void.
     *
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param step        the step
     * @param action      the action
     */
    public static void run(int fromInclude, int toExclude, int step, final Action1<Integer> action) {
        shouldNotBeNull(action, "function");

        if (log.isDebugEnabled())
            log.debug("병렬로 작업을 수행합니다... fromInclude=[{}], toExclude=[{}], step=[{}], workerCount=[{}]",
                      fromInclude, toExclude, step, getWorkerCount());
        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getWorkerCount());
            List<Callable<Void>> tasks = Lists.newLinkedList();

            for (final NumberRange.IntRange partition : partitions) {
                Callable<Void> task =
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                for (final int element : partition)
                                    action.perform(element);
                                return null;
                            }
                        };
                tasks.add(task);
            }

            List<Future<Void>> results = executor.invokeAll(tasks);
            for (Future<Void> result : results) {
                result.get();
            }

            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 수행하였습니다!");

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Run list.
     *
     * @param count    the count
     * @param callable the callable
     * @return the list
     */
    public static <V> List<V> run(int count, final Callable<V> callable) {
        return run(0, count, callable);
    }

    /**
     * Run list.
     *
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param callable    the callable
     * @return the list
     */
    public static <V> List<V> run(int fromInclude, int toExclude, final Callable<V> callable) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        return run(fromInclude, toExclude, step, callable);
    }

    /**
     * Run list.
     *
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param step        the step
     * @param callable    the callable
     * @return the list
     */
    public static <V> List<V> run(int fromInclude, int toExclude, int step, final Callable<V> callable) {
        shouldNotBeNull(callable, "callable");
        if (isDebugEnabled)
            log.debug("병렬로 작업을 수행합니다... fromInclude=[{}], toExclude=[{}], step=[{}], workerCount=[{}]",
                      fromInclude, toExclude, step, getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getWorkerCount());
            final Map<Integer, List<V>> localResults = Maps.newLinkedHashMap();
            List<Callable<List<V>>> tasks = Lists.newLinkedList(); // False Sharing을 방지하기 위해

            for (int p = 0; p < partitions.size(); p++) {
                final NumberRange.IntRange partition = partitions.get(p);
                final List<V> localResult = Lists.newArrayListWithCapacity(partition.size());
                localResults.put(p, localResult);

                Callable<List<V>> task = new Callable<List<V>>() {
                    @Override
                    public List<V> call() throws Exception {
                        for (final int element : partition)
                            localResult.add(callable.call());
                        return localResult;
                    }
                };
                tasks.add(task);
            }

            executor.invokeAll(tasks);

            List<V> results = Lists.newCopyOnWriteArrayList();
            for (int i = 0; i < partitions.size(); i++) {
                results.addAll(localResults.get(i));
            }

            if (isDebugEnabled) log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;
        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Run list.
     *
     * @param count    the count
     * @param function the function
     * @return the list
     */
    public static <V> List<V> run(int count, final Function1<Integer, V> function) {
        return run(0, count, function);
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param fromInclude 시작 인덱스 (하한)
     * @param toExclude   종료 인덱스 (상한)
     * @param function    the function
     * @return 결과 값 컬렉션
     */
    public static <V> List<V> run(int fromInclude, int toExclude, final Function1<Integer, V> function) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        return run(fromInclude, toExclude, step, function);
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param fromInclude 시작 인덱스 (하한)
     * @param toExclude   종료 인덱스 (상한)
     * @param step        Step
     * @param function    수행할 함수
     * @return 결과 값 컬렉션
     */
    public static <V> List<V> run(int fromInclude, int toExclude, int step, final Function1<Integer, V> function) {
        shouldNotBeNull(function, "function");
        if (isDebugEnabled)
            log.debug("병렬로 작업을 수행합니다... fromInclude=[{}], toExclude=[{}], step=[{}], workerCount=[{}]",
                      fromInclude, toExclude, step, getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getWorkerCount());
            final Map<Integer, List<V>> localResults = Maps.newLinkedHashMap();
            List<Callable<List<V>>> tasks = Lists.newLinkedList(); // False Sharing을 방지하기 위해

            for (int p = 0; p < partitions.size(); p++) {
                final NumberRange.IntRange partition = partitions.get(p);
                final List<V> localResult = Lists.newArrayListWithCapacity(partition.size());
                localResults.put(p, localResult);

                Callable<List<V>> task = new Callable<List<V>>() {
                    @Override
                    public List<V> call() throws Exception {
                        for (final int element : partition)
                            localResult.add(function.execute(element));
                        return localResult;
                    }
                };
                tasks.add(task);
            }

            executor.invokeAll(tasks);

            List<V> results = Lists.newArrayList();
            for (int i = 0; i < partitions.size(); i++) {
                results.addAll(localResults.get(i));
            }
            if (isDebugEnabled) log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;
        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정한 컬렉션을 분할하여, 멀티스레드 환경하에서 작업을 수행합니다.
     *
     * @param elements action을 입력 인자로 사용할 컬렉션
     * @param action   수행할 function
     */
    public static <T> void runEach(final Iterable<T> elements, final Action1<T> action) {
        shouldNotBeNull(elements, "elements");
        shouldNotBeNull(action, "function");
        if (isDebugEnabled) log.debug("병렬로 작업을 수행합니다... workerCount=[{}]", getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<T> elemList = Lists.newArrayList(elements);
            int partitionSize = getPartitionSize(elemList.size(), getWorkerCount());
            Iterable<List<T>> partitions = Iterables.partition(elemList, partitionSize);
            List<Callable<Void>> tasks = Lists.newLinkedList();

            for (final List<T> partition : partitions) {
                Callable<Void> task =
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                for (final T element : partition)
                                    action.perform(element);
                                return null;
                            }
                        };
                tasks.add(task);
            }
            List<Future<Void>> results = executor.invokeAll(tasks);
            for (Future<Void> result : results) {
                result.get();
            }

            if (isDebugEnabled)
                log.debug("모든 작업을 병렬로 수행하였습니다. workerCount=[{}]", getWorkerCount());

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정한 컬렉션을 분할해서, 병렬로 function을 수행하고, 결과를 반환합니다.
     *
     * @param elements function의 입력 정보
     * @param function 수행할 함수
     * @return 수행 결과의 컬렉션
     */
    public static <T, V> List<V> runEach(final Iterable<T> elements, final Function1<T, V> function) {
        shouldNotBeNull(elements, "elements");
        shouldNotBeNull(function, "function");
        if (isDebugEnabled) log.debug("병렬로 작업을 수행합니다... workerCount=[{}]", getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<T> elemList = Lists.newArrayList(elements);
            int partitionSize = getPartitionSize(elemList.size(), getWorkerCount());
            List<List<T>> partitions = Lists.partition(elemList, partitionSize);
            final Map<Integer, List<V>> localResults = Maps.newLinkedHashMap();

            List<Callable<List<V>>> tasks = Lists.newLinkedList(); // False Sharing을 방지하기 위해

            for (int p = 0; p < partitions.size(); p++) {
                final List<T> partition = partitions.get(p);
                final List<V> localResult = Lists.newArrayListWithCapacity(partition.size());
                localResults.put(p, localResult);

                Callable<List<V>> task = new Callable<List<V>>() {
                    @Override
                    public List<V> call() throws Exception {
                        for (final T element : partition)
                            localResult.add(function.execute(element));
                        return localResult;
                    }
                };
                tasks.add(task);
            }

            executor.invokeAll(tasks);

            List<V> results = Lists.newArrayListWithCapacity(elemList.size());

            for (int i = 0; i < partitions.size(); i++) {
                results.addAll(localResults.get(i));
            }

            if (isDebugEnabled) log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param count  수행할 횟수
     * @param action 수행할 함수
     */
    public static void runPartitions(int count, final Action1<List<Integer>> action) {
        runPartitions(0, count, action);
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param fromInclude 시작 인덱스 (하한)
     * @param toExclude   종료 인덱스 (상한)
     * @param action      수행할 함수
     */
    public static void runPartitions(int fromInclude, int toExclude, final Action1<List<Integer>> action) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        runPartitions(fromInclude, toExclude, step, action);
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param fromInclude 시작 인덱스 (하한)
     * @param toExclude   종료 인덱스 (상한)
     * @param step        Step
     * @param action      수행할 함수
     */
    public static void runPartitions(int fromInclude, int toExclude, int step, final Action1<List<Integer>> action) {
        shouldNotBeNull(action, "function");
        if (isDebugEnabled)
            log.debug("병렬로 작업을 수행합니다... fromInclude=[{}], toExclude=[{}], step=[{}], workerCount=[{}]",
                      fromInclude, toExclude, step, getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getWorkerCount());
            List<Callable<Void>> tasks = Lists.newLinkedList();

            for (NumberRange.IntRange partition : partitions) {
                final List<Integer> inputs = Lists.newArrayList(partition.iterator());
                Callable<Void> task =
                        new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                action.perform(inputs);
                                return null;
                            }
                        };
                tasks.add(task);
            }

            List<Future<Void>> results = executor.invokeAll(tasks);
            for (Future<Void> result : results) {
                result.get();
            }

            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 수행하였습니다!");

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param count    수행할 횟수
     * @param function 수행할 함수
     * @return 결과 값 컬렉션
     */
    public static <V> List<V> runPartitions(int count, final Function1<List<Integer>, List<V>> function) {
        return runPartitions(0, count, function);
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param fromInclude 시작 인덱스 (하한)
     * @param toExclude   종료 인덱스 (상한)
     * @param function    수행할 함수
     * @return 결과 값 컬렉션
     */
    public static <V> List<V> runPartitions(int fromInclude, int toExclude, final Function1<List<Integer>, List<V>> function) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        return runPartitions(fromInclude, toExclude, step, function);
    }

    /**
     * 지정한 범위의 정보를 수행합니다.
     *
     * @param fromInclude 시작 인덱스 (하한)
     * @param toExclude   종료 인덱스 (상한)
     * @param step        Step
     * @param function    수행할 함수
     * @return 결과 값 컬렉션
     */
    public static <V> List<V> runPartitions(int fromInclude, int toExclude, int step, final Function1<List<Integer>, List<V>> function) {
        shouldNotBeNull(function, "function");
        if (isDebugEnabled)
            log.debug("병렬로 작업을 수행합니다... fromInclude=[{}], toExclude=[{}], step=[{}], workerCount=[{}]",
                      fromInclude, toExclude, step, getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getWorkerCount());
            List<Callable<List<V>>> tasks = Lists.newLinkedList(); // False Sharing을 방지하기 위해

            for (final NumberRange.IntRange partition : partitions) {
                final List<Integer> inputs = Lists.newArrayList(partition.iterator());
                Callable<List<V>> task = new Callable<List<V>>() {
                    @Override
                    public List<V> call() throws Exception {
                        return function.execute(inputs);
                    }
                };
                tasks.add(task);
            }
            // 작업 시작
            List<Future<List<V>>> outputs = executor.invokeAll(tasks);

            List<V> results = Lists.newArrayList();
            for (Future<List<V>> output : outputs) {
                results.addAll(output.get());
            }

            if (isDebugEnabled) log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정한 컬렉션을 분할하여, 병렬로 작업을 수행합니다.
     *
     * @param elements 처리할 데이터
     * @param action   수행할 코드
     */
    public static <T> void runPartitions(final Iterable<T> elements, final Action1<List<T>> action) {
        shouldNotBeNull(elements, "elements");
        shouldNotBeNull(action, "function");
        if (isDebugEnabled) log.debug("병렬로 작업을 수행합니다... workerCount=[{}]", getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<T> elemList = Lists.newArrayList(elements);
            int partitionSize = getPartitionSize(elemList.size(), getWorkerCount());
            Iterable<List<T>> partitions = Iterables.partition(elemList, partitionSize);
            List<Callable<Void>> tasks = Lists.newLinkedList();

            for (final List<T> partition : partitions) {
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        action.perform(partition);
                        return null;
                    }
                };
                tasks.add(task);
            }
            // 작업 시작
            List<Future<Void>> results = executor.invokeAll(tasks);

            for (Future<Void> result : results)
                result.get();

            if (isDebugEnabled)
                log.debug("모든 작업을 병렬로 수행했습니다. workCount=[{}]");
        } catch (Exception e) {
            log.error("데이터에 대한 병렬작업중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정한 컬렉션을 분할하여, 병렬로 작업을 수행합니다.
     *
     * @param elements 처리할 데이터
     * @param function 수행할 코드
     * @return 수행한 결과
     */
    public static <T, V> List<V> runPartitions(final Iterable<T> elements, final Function1<List<T>, List<V>> function) {
        shouldNotBeNull(elements, "elements");
        shouldNotBeNull(function, "function");
        if (isDebugEnabled) log.debug("병렬로 작업을 수행합니다... workerCount=[{}]", getWorkerCount());

        ExecutorService executor = Executors.newFixedThreadPool(getWorkerCount());

        try {
            List<T> elemList = Lists.newArrayList(elements);
            int partitionSize = getPartitionSize(elemList.size(), getWorkerCount());
            List<List<T>> partitions = Lists.partition(elemList, partitionSize);
            final Map<Integer, List<V>> localResults = Maps.newLinkedHashMap();

            List<Callable<List<V>>> tasks = Lists.newLinkedList(); // False Sharing을 방지하기 위해

            for (final List<T> partition : partitions) {
                Callable<List<V>> task = new Callable<List<V>>() {
                    @Override
                    public List<V> call() throws Exception {
                        return function.execute(partition);
                    }
                };
                tasks.add(task);
            }
            // 작업 시작
            List<Future<List<V>>> futures = executor.invokeAll(tasks);

            List<V> results = Lists.newArrayListWithCapacity(elemList.size());
            for (Future<List<V>> future : futures)
                results.addAll(future.get());

            if (isDebugEnabled) log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

}
