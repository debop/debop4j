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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static kr.debop4j.core.Guard.shouldNotBeNull;


/**
 * 대량 데이터에 대한 병렬 실행을 수행할 수 있도록 해주는 Class 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 26.
 */
@Slf4j
public class Parallels {

    private Parallels() { }

    @Getter(lazy = true)
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    @Getter(lazy = true)
    private static final int processCount = Runtime.getRuntime().availableProcessors();
    @Getter(lazy = true)
    private static final ExecutorService defaultExecutor =
            Executors.newFixedThreadPool(getProcessCount() * 2, new NamedThreadFactory("Parallels"));

    public static ExecutorService createExecutor(int threadCount) {
        return Executors.newFixedThreadPool(threadCount);
    }

    private static int getPartitionSize(int itemCount, int partitionCount) {
        return (itemCount / partitionCount) + ((itemCount % partitionCount) > 0 ? 1 : 0);
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

        ExecutorService executor = Executors.newFixedThreadPool(getProcessCount());

        if (log.isDebugEnabled())
            log.debug("작업을 병렬로 수행합니다. 작업 스레드 수=[{}]", getProcessCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getProcessCount());
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
            executor.invokeAll(tasks);

            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 수행하였습니다.");

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

        ExecutorService executor = Executors.newFixedThreadPool(getProcessCount());

        if (log.isDebugEnabled())
            log.debug("작업을 병렬로 수행합니다. 작업 스레드 수=[{}]", getProcessCount());

        try {
            List<NumberRange.IntRange> partitions = NumberRange.partition(fromInclude, toExclude, step, getProcessCount());
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
                log.debug("모든 작업을 병렬로 완료했습니다. partition count=[{}]", getProcessCount());

            return results;
        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public static <T> void runEach(final Iterable<T> elements, final Action1<T> action) {
        shouldNotBeNull(elements, "elements");
        shouldNotBeNull(action, "action");

        ExecutorService executor = Executors.newFixedThreadPool(getProcessCount());

        if (log.isDebugEnabled())
            log.debug("작업을 병렬로 수행합니다. 작업 스레드 수=[{}]", getProcessCount());

        try {
            List<T> elemList = Lists.newArrayList(elements);
            int partitionSize = getPartitionSize(elemList.size(), getProcessCount());
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
            executor.invokeAll(tasks);

            if (log.isDebugEnabled())
                log.debug("모든 작업을 병렬로 수행하였습니다. partition count=[{}]", getProcessCount());

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public static <T, V> List<V> runEach(final Iterable<T> elements, final Function1<T, V> function) {
        shouldNotBeNull(elements, "elements");
        shouldNotBeNull(function, "function");

        ExecutorService executor = Executors.newFixedThreadPool(getProcessCount());

        if (log.isDebugEnabled())
            log.debug("작업을 병렬로 수행합니다. 작업 스레드 수=[{}]", getProcessCount());

        try {
            List<T> elemList = Lists.newArrayList(elements);
            int partitionSize = getPartitionSize(elemList.size(), getProcessCount());
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
                log.debug("모든 작업을 병렬로 완료했습니다. partition count=[{}]", getProcessCount());

            return results;

        } catch (Exception e) {
            log.error("데이터에 대한 병렬 작업 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
