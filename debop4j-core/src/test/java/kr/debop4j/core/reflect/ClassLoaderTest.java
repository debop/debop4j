package kr.debop4j.core.reflect;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * kr.debop4j.core.reflect.ClassLoaderTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 21
 */
@Slf4j
public class ClassLoaderTest extends AbstractTest {

    static public class TestClass {
        public String name;

        public String toString() {
            return name;
        }
    }

    private static final String testClassName = "kr.debop4j.core.reflect.ClassLoaderTest$TestClass";

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void differentClassLoader() throws Exception {
        ClassLoader testClassLoader = new ClassLoader() {
            @Override
            protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                Class c = findLoadedClass(name);
                if (c != null) return c;
                if (name.startsWith("java.")) return super.loadClass(name, resolve);
                if (!name.equals(testClassName))
                    throw new ClassNotFoundException("Class not found on purpose: " + name);
                ByteArrayOutputStream output = new ByteArrayOutputStream(32 * 1024);
                InputStream input = ClassLoaderTest.class.getResourceAsStream("/" + name.replace('.', '/') + ".class");
                if (input == null) return null;
                try {
                    byte[] buffer = new byte[4096];
                    int total = 0;
                    while (true) {
                        int length = input.read(buffer, 0, buffer.length);
                        if (length == -1) break;
                        output.write(buffer, 0, length);
                    }
                } catch (IOException ex) {
                    throw new ClassNotFoundException("Error reading class file.", ex);
                } finally {
                    try {
                        input.close();
                    } catch (IOException ignored) {
                    }
                }
                byte[] buffer = output.toByteArray();
                return defineClass(name, buffer, 0, buffer.length);
            }
        };
        Class testClass = testClassLoader.loadClass(testClassName);
        Object testObject = testClass.newInstance();
        Assert.assertNotNull(testObject);

        FieldAccess access = FieldAccess.get(testObject.getClass());
        access.set(testObject, "name", "first");
        Assert.assertEquals("first", testObject.toString());
        Assert.assertEquals("first", access.get(testObject, "name"));
    }
}
