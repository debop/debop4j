package kr.debop4j.core.parallelism;

import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * kr.debop4j.core.parallelism.NamedThreadFactory
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Slf4j
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    private String prefix;

    public NamedThreadFactory(String prefix) {
        this.prefix = StringTool.isEmpty(prefix) ? "thread-" : prefix + " thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        assert r != null;
        return new Thread(r, prefix + threadNumber.getAndIncrement());
    }
}
