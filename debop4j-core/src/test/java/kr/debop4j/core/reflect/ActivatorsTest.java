package kr.debop4j.core.reflect;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.tools.JClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * kr.debop4j.core.reflect.ActivatorsTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 20.
 */
@Slf4j
public class ActivatorsTest extends AbstractTest {

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void createInstanceWithDefaultConstructor() {
        JClass obj = Activators.createInstance(JClass.class);
        Assert.assertNotNull(obj);
        Assert.assertEquals(0, obj.getId());
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void crateInstanceWithParameters() {
        JClass obj = Activators.createInstance(JClass.class, 100, "Dynamic", 200);
        Assert.assertNotNull(obj);
        Assert.assertEquals(100, obj.getId());
        Assert.assertEquals("Dynamic", obj.getName());
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void crateInstanceWithParameterTypes() throws Exception {
        JClass obj = (JClass) Activators
                .getConstructor(JClass.class, Integer.TYPE, String.class, Integer.class)
                .newInstance(100, "Dynamic", 200);
        Assert.assertNotNull(obj);
        Assert.assertEquals(100, obj.getId());
        Assert.assertEquals("Dynamic", obj.getName());
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void reflectionsWithDefaultConstructor() {
        try {
            JClass obj =
                    (JClass) JClass.class
                            .getConstructor(Integer.TYPE, String.class, Integer.class)
                            .newInstance(100, "Dynamic", 200);
            Assert.assertNotNull(obj);
            Assert.assertEquals(100, obj.getId());
            Assert.assertEquals("Dynamic", obj.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



