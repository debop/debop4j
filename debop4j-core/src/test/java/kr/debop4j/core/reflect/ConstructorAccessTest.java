package kr.debop4j.core.reflect;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * kr.debop4j.core.reflect.ConstructorAccessTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 21
 */
@Slf4j
public class ConstructorAccessTest extends AbstractTest {

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void instanceClass() {
        ConstructorAccess<User> access = ConstructorAccess.get(User.class);

        Assert.assertEquals(User.class, access.newInstance().getClass());
        Assert.assertEquals(User.class, access.newInstance().getClass());
        Assert.assertEquals(User.class, access.newInstance().getClass());
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1)
    @Test
    public void instanceStaticInnerClass() {
        ConstructorAccess<SomeClass> access = ConstructorAccess.get(SomeClass.class);
        SomeClass someObject = new SomeClass();

        Assert.assertEquals(someObject, access.newInstance());
        Assert.assertEquals(someObject, access.newInstance());
        Assert.assertEquals(someObject, access.newInstance());
    }

    static public class SomeClass {
        public String name;
        public int intValue;
        protected float test1;
        Float test2;
        private String test3;

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            SomeClass other = (SomeClass) obj;
            if (intValue != other.intValue) return false;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            if (Float.floatToIntBits(test1) != Float.floatToIntBits(other.test1)) return false;
            if (test2 == null) {
                if (other.test2 != null) return false;
            } else if (!test2.equals(other.test2)) return false;
            if (test3 == null) {
                if (other.test3 != null) return false;
            } else if (!test3.equals(other.test3)) return false;
            return true;
        }
    }
}
