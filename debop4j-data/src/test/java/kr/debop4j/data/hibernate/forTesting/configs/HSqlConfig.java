package kr.debop4j.data.hibernate.forTesting.configs;

import kr.debop4j.data.hibernate.forTesting.LongEntityForTesting;
import kr.debop4j.data.hibernate.springconfiguration.HSqlConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * HSql 을 사용하는 Hibernate 환경설정 정보입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public class HSqlConfig extends HSqlConfigBase {

    @Override
    protected String[] getMappedPackageNames() {
        return new String[0];
    }

    @Override
    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) {
        super.setupSessionFactory(factoryBean);

        factoryBean.setAnnotatedClasses(new Class[]{LongEntityForTesting.class});
    }
}
