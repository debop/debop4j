package kr.debop4j.data.hibernate.forTesting.configs;

import kr.debop4j.data.hibernate.forTesting.LongEntityForTesting;
import kr.debop4j.data.hibernate.springconfiguration.PgPoolConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.data.hibernate.forTesting.configs.PgPoolConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 26.
 */
@Configuration
@EnableTransactionManagement
public class PgPoolConfig extends PgPoolConfigBase {

    protected String[] getMappedPackageNames() {
        return new String[0];
    }

    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) {
        super.setupSessionFactory(factoryBean);
        factoryBean.setAnnotatedClasses(new Class[]{LongEntityForTesting.class});
    }
}
