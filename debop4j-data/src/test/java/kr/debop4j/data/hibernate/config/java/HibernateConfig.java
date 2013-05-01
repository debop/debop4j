package kr.debop4j.data.hibernate.config.java;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * kr.debop4j.data.hibernate.config.java.HibernateConfig
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 19.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return
                new org.springframework.orm.hibernate4.LocalSessionFactoryBuilder(dataSource())
                        .addAnnotatedClasses(Account.class)
                        .buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new org.springframework.orm.hibernate4.HibernateTransactionManager(sessionFactory());
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseFactoryBean bean = new EmbeddedDatabaseFactoryBean();
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("kr/debop4j/data/hibernate/config/java/schema.sql"));
        bean.setDatabasePopulator(databasePopulator);
        bean.afterPropertiesSet();   // EmbeddedDatabaseFactoryBean가 FactoryBean이므로 필요합니다.
        return bean.getObject();
    }
}
