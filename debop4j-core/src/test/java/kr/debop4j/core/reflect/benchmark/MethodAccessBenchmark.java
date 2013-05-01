package kr.debop4j.core.reflect.benchmark;

import kr.debop4j.core.reflect.MethodAccess;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * kr.debop4j.core.reflect.benchmark.MethodAccessBenchmark
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 21
 */
@Slf4j
public class MethodAccessBenchmark extends Benchmark {

    @Test
    public void benchmarkMethodAccess() throws Exception {
        int count = 100_000;
        Object[] dontCompileMeAway = new Object[count];

        MethodAccess access = MethodAccess.get(SomeClass.class);
        SomeClass someObject = new SomeClass();
        int index = access.getIndex("getName");

        Method method = SomeClass.class.getMethod("getName");

        for (int i = 0; i < 100; i++) {
            for (int ii = 0; ii < count; ii++)
                dontCompileMeAway[ii] = access.invoke(someObject, index);
            for (int ii = 0; ii < count; ii++)
                dontCompileMeAway[ii] = method.invoke(someObject);
        }
        warmup = false;

        for (int i = 0; i < 100; i++) {
            start();
            for (int ii = 0; ii < count; ii++)
                dontCompileMeAway[ii] = access.invoke(someObject, index);
            end("MethodAccess");
        }
        for (int i = 0; i < 100; i++) {
            start();
            for (int ii = 0; ii < count; ii++)
                dontCompileMeAway[ii] = method.invoke(someObject);
            end("Reflection");
        }
    }

    static public class SomeClass {
        private String name = "something";

        public String getName() {
            return name;
        }
    }
}
