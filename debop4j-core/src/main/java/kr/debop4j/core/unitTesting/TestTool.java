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

package kr.debop4j.core.unitTesting;

import com.google.common.collect.Lists;
import kr.debop4j.core.Action1;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * 멀티쓰레디로 테스트를 수행합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 3.
 */
@Slf4j
public abstract class TestTool {

    private TestTool() { }

    private static ExecutorService newExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * 지정된 횟수만큼 멀티스레드를 이용하여 작업을 수행합니다.
     *
     * @param count    수행활 횟수
     * @param runnable 수행할 함수 객체
     */
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
            log.error("작업 테스트 중 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정된 횟수만큼 멀티스레드를 이용하여 작업을 수행합니다.
     *
     * @param count     수행 횟수
     * @param callables 실행할 메소드들
     * @param <T>       반환할 수형
     */
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
            log.error("예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 지정된 횟수만큼 멀티스레드를 이용하여 작업을 수행합니다.
     *
     * @param count  수행 횟수
     * @param action 실행할 메소드들
     */
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
                        log.trace("run task... index=[{}]", index);
                        action.perform(index);
                        latch.countDown();
                    }
                });
            }
            latch.await();
        } catch (Exception e) {
            log.error("예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
