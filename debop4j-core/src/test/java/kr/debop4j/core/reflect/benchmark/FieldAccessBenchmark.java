package kr.debop4j.core.reflect.benchmark;

import kr.debop4j.core.reflect.FieldAccess;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * kr.nsoft.commons.reflect.benchmark.FieldAccessBenchmark
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 21
 */
@Slf4j
public class FieldAccessBenchmark extends Benchmark {

    @Test
    public void benchmarkFieldAccess() throws Exception {
        int count = 1000000;
        Object[] dontCompileMeAway = new Object[count];

        FieldAccess access = FieldAccess.get(SomeClass.class);
        SomeClass someObject = new SomeClass();
        int index = access.getIndex("name");

        Field field = SomeClass.class.getField("name");

        for (int i = 0; i < 100; i++) {
            for (int ii = 0; ii < count; ii++) {
                access.set(someObject, index, "first");
                dontCompileMeAway[ii] = access.get(someObject, index);
            }
            for (int ii = 0; ii < count; ii++) {
                field.set(someObject, "first");
                dontCompileMeAway[ii] = field.get(someObject);
            }
        }
        warmup = false;

        for (int i = 0; i < 100; i++) {
            start();
            for (int ii = 0; ii < count; ii++) {
                access.set(someObject, index, "first");
                dontCompileMeAway[ii] = access.get(someObject, index);
            }
            end("FieldAccess");
        }
        for (int i = 0; i < 100; i++) {
            start();
            for (int ii = 0; ii < count; ii++) {
                field.set(someObject, "first");
                dontCompileMeAway[ii] = field.get(someObject);
            }
            end("Reflection");
        }
    }

    static public class SomeClass {
        public String name;
    }
}
