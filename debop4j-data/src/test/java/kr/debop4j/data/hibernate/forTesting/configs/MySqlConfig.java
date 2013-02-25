package kr.debop4j.data.hibernate.forTesting.configs;

import kr.debop4j.data.hibernate.forTesting.LongEntityForTesting;
import kr.debop4j.data.hibernate.springconfiguration.MySqlConfigBase;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * MySQL 을 사용하는 Hibernate 설정 정보입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 22.
 */
@Component
@EnableTransactionManagement
public class MySqlConfig extends MySqlConfigBase {

    protected String[] getMappedPackageNames() {
        return new String[0];
    }

    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) {
        super.setupSessionFactory(factoryBean);
        factoryBean.setAnnotatedClasses(new Class[]{LongEntityForTesting.class});
    }

    // Windows 에서 BoneCP + MySQL 접속에 문제가 있어 dbcp를 사용합니다.
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost/" + getDatabaseName());
        ds.setUsername("root");
        ds.setPassword("root");

        return ds;
    }
}
