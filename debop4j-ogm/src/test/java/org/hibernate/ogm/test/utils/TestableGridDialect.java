package org.hibernate.ogm.test.utils;

/**
 * For testing purposes we need to be able to extract more information than what is mandated from the GridDialect,
 * so each GridDialect implementor should also implement a TestGridDialect, and list it by classname into
 * {@code org.hibernate.ogm.test.utils.TestHelper#knownTestDialects }
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 2:36
 */
public interface TestableGridDialect {
}
