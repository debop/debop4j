package kr.debop4j.data.hibernate.forTesting.configs;

import kr.debop4j.data.hibernate.forTesting.LongEntityForTesting;
import kr.debop4j.data.hibernate.spring.H2ConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * H2 DB 를 사용하는 Hibernate Configuration 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 22.
 */
@Configuration
@EnableTransactionManagement
public class H2Config extends H2ConfigBase {
    @Override
    protected String[] getMappedPackageNames() {
        return null;
    }

    @Override
    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) {
        factoryBean.setAnnotatedClasses(new Class[] { LongEntityForTesting.class });
    }
}
