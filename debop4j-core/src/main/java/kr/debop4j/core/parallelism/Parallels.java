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
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class Parallels {

    private Parallels() { }

    @Getter(lazy = true)
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    @Getter(lazy = true)
    private static final int processCount = Runtime.getRuntime().availableProcessors();
    @Getter(lazy = true)
    private static final int workerCount = getProcessCount() * 2;

    public static ExecutorService createExecutor() {
        return createExecutor(getWorkerCount());
    }

    public static ExecutorService createExecutor(int threadCount) {
        return Executors.newFixedThreadPool(threadCount);
    }

    private static int getPartitionSize(int itemCount, int partitionCount) {
        return (itemCount / partitionCount) + ((itemCount % partitionCount) > 0 ? 1 : 0);
    }

    /** 지정한 작업을 병렬로 수행합니다. */
    public static void run(int count, final Runnable runnable) {
        run(0, count, runnable);
    }

    public static void run(int fromInclude, int toExclude, final Runnable action) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        run(fromInclude, toExclude, step, action);
    }

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

    public static void run(int count, final Action1<Integer> action) {
        run(0, count, action);
    }

    public static void run(int fromInclude, int toExclude, final Action1<Integer> action) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        run(fromInclude, toExclude, step, action);
    }

    public static void run(int fromInclude, int toExclude, int step, final Action1<Integer> action) {
        shouldNotBeNull(action, "action");

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

    public static <V> List<V> run(int count, final Callable<V> callable) {
        return run(0, count, callable);
    }

    public static <V> List<V> run(int fromInclude, int toExclude, final Callable<V> callable) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        return run(fromInclude, toExclude, step, callable);
    }

    public static <V> List<V> run(int fromInclude, int toExclude, int step, final Callable<V> callable) {
        assert callable != null;

        if (log.isDebugEnabled())
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
            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;
        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public static <V> List<V> run(int count, final Function1<Integer, V> action) {
        return run(0, count, action);
    }

    public static <V> List<V> run(int fromInclude, int toExclude, final Function1<Integer, V> action) {
        int step = (fromInclude <= toExclude) ? 1 : -1;
        return run(fromInclude, toExclude, step, action);
    }

    public static <V> List<V> run(int fromInclude, int toExclude, int step, final Function1<Integer, V> function) {
        shouldNotBeNull(function, "function");

        if (log.isDebugEnabled())
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
            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;
        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public static <T> void runEach(final Iterable<T> elements, final Action1<T> action) {
        assert elements != null;
        assert action != null;

        if (log.isDebugEnabled())
            log.debug("병렬로 작업을 수행합니다... workerCount=[{}]", getWorkerCount());
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

            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 수행하였습니다. workerCount=[{}]", getWorkerCount());

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public static <T, V> List<V> runEach(final Iterable<T> elements, final Function1<T, V> function) {
        assert elements != null;
        assert function != null;

        if (log.isDebugEnabled())
            log.debug("병렬로 작업을 수행합니다... workerCount=[{}]", getWorkerCount());
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

            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 완료했습니다. workerCount=[{}]", getWorkerCount());

            return results;

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
