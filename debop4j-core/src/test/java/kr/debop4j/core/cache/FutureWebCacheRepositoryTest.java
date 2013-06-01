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

package kr.debop4j.core.cache;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import kr.debop4j.core.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

/**
 * kr.debop4j.core.caching.dao.FutureWebCacheRepositoryTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 24
 */
@Slf4j
public class FutureWebCacheRepositoryTest {

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    private static final String[] urls = new String[] {
            "http://www.daum.net",
            "http://www.naver.com",
            "http://www.nate.com"
    };

    @Test
    public void webCacheRepositoryTest() throws Exception {
        FutureWebCacheRepository repository = new FutureWebCacheRepository();
        Stopwatch stopwatch = new Stopwatch();

        for (final String url : urls) {
            stopwatch.start();
            repository.get(url);
            stopwatch.stop();
            log.debug("First: " + stopwatch.toString());

            stopwatch.start();
            repository.get(url);
            stopwatch.stop();
            log.debug("Second: " + stopwatch.toString());
        }
    }
}
