package kr.debop4j.core.unitTesting;

import com.google.common.collect.Lists;
import kr.debop4j.core.Action1;

import java.util.List;
import java.util.concurrent.*;

/**
 * kr.debop4j.core.unitTesting.TestTool
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 3.
 */
public class TestTool {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestTool.class);

    private TestTool() { }

    private static ExecutorService newExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static void runTasks(int count, final Runnable runnable) {

        ExecutorService executor = newExecutorService();

        try {
            final CountDownLatch latch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        runnable.run();
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } catch (InterruptedException e) {
            if (log.isErrorEnabled())
                log.error("작업 수행 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void runTasks(int count, Callable<T> callables) {

        ExecutorService executor = newExecutorService();

        try {
            List<Future<T>> futures = Lists.newArrayList();

            for (int i = 0; i < count; i++) {
                List<Future<T>> results = executor.invokeAll(Lists.newArrayList(callables));
                futures.addAll(results);
            }
            for (Future<T> future : futures) {
                future.get();
            }

        } catch (ExecutionException | InterruptedException e) {
            if (log.isErrorEnabled())
                log.error("예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    @SuppressWarnings("unchecked")
    public static void runTasks(int count, final Action1<Integer> action) {

        ExecutorService executor = newExecutorService();
        final CountDownLatch latch = new CountDownLatch(count);

        try {

            for (int i = 0; i < count; i++) {
                final int index = i;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        action.perform(index);
                        latch.countDown();
                    }
                });
            }
            latch.await();

        } catch (InterruptedException e) {
            if (log.isErrorEnabled())
                log.error("예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
