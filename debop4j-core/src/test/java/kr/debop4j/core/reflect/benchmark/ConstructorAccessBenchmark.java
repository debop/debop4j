package kr.debop4j.core.reflect.benchmark;

import kr.debop4j.core.reflect.ConstructorAccess;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * kr.debop4j.core.reflect.benchmark.ConstructorAccessBenchmark
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 21
 */
@Slf4j
public class ConstructorAccessBenchmark extends Benchmark {

    @Test
    public void construcotAccessBenchmark() throws Exception {
        int count = 1_000_000;
        Object[] dontCompileMeAway = new Object[count];

        Class type = SomeClass.class;
        ConstructorAccess<SomeClass> access = ConstructorAccess.get(type);

        for (int i = 0; i < 100; i++)
            for (int j = 0; j < count; j++)
                dontCompileMeAway[j] = access.newInstance();

        for (int i = 0; i < 100; i++)
            for (int j = 0; j < count; j++)
                dontCompileMeAway[j] = type.newInstance();

        warmup = false;

        for (int i = 0; i < 100; i++) {
            start();
            for (int j = 0; j < count; j++)
                dontCompileMeAway[j] = access.newInstance();
            end("ConstructorAccess");
        }

        for (int i = 0; i < 100; i++) {
            start();
            for (int j = 0; j < count; j++)
                dontCompileMeAway[j] = type.newInstance();
            end("Reflection");
        }

    }

    static public class SomeClass {
        public String name;
    }
}
