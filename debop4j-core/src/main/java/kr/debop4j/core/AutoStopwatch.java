package kr.debop4j.core;

import lombok.extern.slf4j.Slf4j;

/**
 * 성능 측정을 자동으로 수행하는 {@link Stopwatch} 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 12
 */
@Slf4j
public class AutoStopwatch implements AutoCloseable {

    private final Stopwatch stopwatch;

    public AutoStopwatch() {
        this("");
    }

    public AutoStopwatch(String msg) {
        stopwatch = new Stopwatch(msg);
        stopwatch.start();
    }

    @Override
    public void close() {
        try {
            stopwatch.stop();
        } catch (Exception ignored) {
        }
    }
}
