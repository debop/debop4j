package kr.debop4j.data.ogm.test.utils;

import kr.debop4j.data.ogm.test.simpleentity.Hypothesis;
import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.junit.Test;

/**
 * kr.debop4j.data.ogm.test.utils.SkipByGridDialectSelfTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 5:54
 */
public class SkipByGridDialectSelfTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{ Hypothesis.class };
    }

    @Test
    @SkipByGridDialect({
                               GridDialectType.HASHMAP,
                               GridDialectType.EHCACHE,
                               GridDialectType.INFINISPAN,
                               GridDialectType.MONGODB })
    public void testWhichAlwaysFails() {
        // 이거 구현 하지 않았다. 위에 지정된 것은 제외하고 테스트 하려고 만든 거다.
        // Assert.fail("This should never be executed");
    }

    @Test
    public void testCorrect() {
        // all fine
    }
}
