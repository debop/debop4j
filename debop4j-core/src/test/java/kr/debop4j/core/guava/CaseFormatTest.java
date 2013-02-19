package kr.debop4j.core.guava;

import com.google.common.base.CaseFormat;
import kr.debop4j.core.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * kr.debop4j.core.guava.CaseFormatTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 22.
 */
@Slf4j
public class CaseFormatTest extends AbstractTest {

    @Test
    public void caseFormatTest() {
        String str = "USER_NAME";
        assertEquals("userName", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str));
        assertEquals("user-name", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, str));
        assertEquals("user_name", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, str));
        assertEquals("UserName", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str));
        assertEquals("USER_NAME", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, str));
    }
}
