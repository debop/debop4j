package kr.debop4j.data.hibernate.forTesting.configs;

import kr.debop4j.data.hibernate.forTesting.LongEntityForTesting;
import kr.debop4j.data.hibernate.spring.DerbyConfigBase;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Derby DB 를 사용하는 Hibernate 환경설정 정보입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 22.
 */
@Component
@EnableTransactionManagement
public class DerbyConfig extends DerbyConfigBase {

    protected String[] getMappedPackageNames() {
        return new String[0];
    }

    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) {
        super.setupSessionFactory(factoryBean);
        factoryBean.setAnnotatedClasses(new Class[] { LongEntityForTesting.class });
    }
}
