package kr.debop4j.core.cacheRepository;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import kr.debop4j.core.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

/**
 * kr.nsoft.commons.caching.dao.FutureWebCacheRepositoryTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 24
 */
@Slf4j
public class FutureWebCacheRepositoryTest {

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    private static final String[] urls = new String[]{
            "http://www.daum.net",
            "http://www.naver.com",
            "http://www.kthcorp.com/"
    };

    @Test
    public void webCacheRepositoryTest() {
        FutureWebCacheRepository repository = new FutureWebCacheRepository();
        Stopwatch stopwatch = new Stopwatch();

        for (String url : urls) {
            stopwatch.start();
            repository.get(url);
            stopwatch.end();
            log.debug("First: " + stopwatch.toString());

            stopwatch.start();
            repository.get(url);
            stopwatch.end();
            log.debug("Second: " + stopwatch.toString());
        }
    }
}
