package kr.debop4j.core.reflect.benchmark;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * kr.debop4j.core.reflect.benchmark.Benchmark
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 21
 */
@Slf4j
public class Benchmark {

    public boolean warmup = true;
    public HashMap<String, Long> runTimes = Maps.newHashMap();
    private long s;

    public void start() {
        s = System.nanoTime();
    }

    public void end(String name) {
        if (warmup)
            return;

        long e = System.nanoTime();
        long time = e - s;
        Long oldTime = runTimes.get(name);
        if (oldTime == null || time < oldTime) runTimes.put(name, time);

        if (log.isDebugEnabled())
            log.debug("[{}]: [{}] ms", name, runTimes.get(name) / 1_000_000f);
    }
}
