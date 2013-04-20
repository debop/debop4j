package kr.debop4j.core.guava;

import com.google.common.base.CharMatcher;
import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * kr.debop4j.core.guava.CharMatcherTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 22.
 */
@Slf4j
public class CharMatcherTest extends AbstractTest {

    @Test
    public void charMatcherTest() {
        Assert.assertFalse(CharMatcher.is('x').apply('a'));
    }
}
