package kr.debop4j.core.guava;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * kr.debop4j.core.guava.ListenableFutureTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 23.
 */
@Slf4j
public class ListenableFutureTest {

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    private static final int ProcessCount = Runtime.getRuntime().availableProcessors();
    private ListeningExecutorService executorService =
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(ProcessCount));

    public ListenableFuture<List<String>> searchAsync(final String query) {
        return executorService.submit(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                log.debug("작업을 시작합니다...");
                List<String> strs = Lists.newArrayListWithCapacity(100);

                for (int i = 0; i < 100; i++)
                    strs.add("Item " + i);
                Thread.sleep(1);

                log.debug("build result asynchronous...");
                return strs;
            }
        });
    }

    @BenchmarkOptions(benchmarkRounds = 24, warmupRounds = 1, concurrency = BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
    @Test
    public void transform() throws Exception {
        ListenableFuture<List<String>> searchTask = searchAsync("abc");

        ListenableFuture<String> result =
                Futures.transform(searchTask,
                                  new AsyncFunction<List<String>, String>() {
                                      @Override
                                      public ListenableFuture<String> apply(final List<String> input) throws Exception {
                                          return executorService.submit(new Callable<String>() {
                                              @Override
                                              public String call() throws Exception {
                                                  Thread.sleep(1);
                                                  log.debug("다음 작업을 비동기로 시작합니다.");
                                                  return Joiner.on(",").join(input);
                                              }
                                          });
                                      }
                                  },
                                  executorService);

        String str = result.get();
        Assert.assertTrue(str.contains("Item"));
    }
}
