package com.kt.vital.domain.model;

import com.kt.vital.domain.VitalDomainTestBase;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * com.kt.vital.domain.model.MappingTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18.
 */
@Slf4j
public class MappingTest extends VitalDomainTestBase {

    @Test
    public void mappingTest() {
        UnitOfWorks.start();

        // Nothing to do.

        UnitOfWorks.stop();
    }
}
