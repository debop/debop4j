package kr.debop4j.core.parallelism;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

/**
 * pudding.pudding.commons.parallelism.forkjoin.FibonacciTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 28.
 */
@Slf4j
public class FibonacciTest {

    private static final int N = 32;

    @Test
    public void sillyWorkerTest() {

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        FibonacciProblem bigProblem = new FibonacciProblem(N);
        long result = bigProblem.solve();

        stopwatch.stop();

        if (log.isDebugEnabled()) {
            log.debug("Computing Fibonacci number=[{}]", N);
            log.debug("Computed Result=[{}]", result);
            log.debug(stopwatch.toString());
        }
    }

    @Test
    public void forkJoinWorkerTest() {
        int processors = Runtime.getRuntime().availableProcessors();

        if (log.isDebugEnabled())
            log.debug("process count=[{}]", processors);

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        FibonacciProblem bigProblem = new FibonacciProblem(N);
        FibonacciTask task = new FibonacciTask(bigProblem);
        ForkJoinPool pool = new ForkJoinPool(processors);

        pool.invoke(task);

        long result = task.getResult();

        stopwatch.stop();
        if (log.isDebugEnabled()) {
            log.debug("Computed result=[{}]", result);
            log.debug(stopwatch.toString());
        }
    }
}
