package kr.debop4j.access.test.model;

import kr.debop4j.access.test.AccessTestBase;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * kr.debop4j.access.test.model.MappingTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 7.
 */
@Slf4j
public class MappingTest extends AccessTestBase {

    @Test
    public void mappingTest() {
        UnitOfWorks.start();

        // Nothing to do.

        UnitOfWorks.stop();
    }
}
