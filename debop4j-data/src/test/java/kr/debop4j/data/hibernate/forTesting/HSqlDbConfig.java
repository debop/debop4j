package kr.debop4j.data.hibernate.forTesting;

import kr.debop4j.data.hibernate.forTesting.configurations.HSqlDbConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.data.hibernate.forTesting.HSqlDbConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public class HSqlDbConfig extends HSqlDbConfiguration {

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{"kr.debop4j.data.hibernate.forTesting"};
    }
}
