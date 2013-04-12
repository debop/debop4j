package kr.debop4j.data.ogm.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * kr.debop4j.data.ogm.test.utils.RequiresTransactionalCapabilitiesRule
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 3:01
 */
@Slf4j
public class RequiresTransactionalCapabilitiesRule implements TestRule {

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (TestHelper.backendSupportsTransactions()) {
                    base.evaluate();
                } else {
                    log.info("Skipping test " + description.getMethodName() + " as the current GridDialect doesn't support transactions");
                }
            }
        };
    }
}
