package kr.debop4j.data.hibernate.forTesting.configurations;

import com.jolbox.bonecp.BoneCPDataSource;
import kr.debop4j.data.hibernate.forTesting.DatabaseEngine;
import kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextBase;
import kr.debop4j.data.hibernate.interceptor.MultiInterceptor;
import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.repository.HibernateDaoFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * kr.debop4j.data.hibernate.forTesting.configurations.DbConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
public abstract class DbConfiguration {

    @Getter
    @Setter
    private UnitOfWorkTestContextBase testContext;

    public DatabaseEngine getDatabaseEngine() {
        return DatabaseEngine.HSql;
    }

    abstract protected String getDatabaseName();

    abstract protected String[] getMappedPackageNames();

    protected Properties hibernateProperties() {

        Properties props = new Properties();

        props.put(Environment.FORMAT_SQL, "true");
        props.put(Environment.HBM2DDL_AUTO, "create"); // create | spawn | spawn-drop | update | validate
        props.put(Environment.SHOW_SQL, "true");
        // props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
        // props.put(Environment.AUTOCOMMIT, "true");
        // props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.put(Environment.STATEMENT_BATCH_SIZE, "30");

        return props;
    }

    protected DataSource buildDataSource(String driverClass, String url, String username, String password) {
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setDriverClass(driverClass);
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setPartitionCount(2);
        ds.setMaxConnectionsPerPartition(100);

        return ds;
    }

    protected DataSource buildEmbeddedDataSource() {
        EmbeddedDatabaseFactoryBean bean = new EmbeddedDatabaseFactoryBean();
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        bean.setDatabasePopulator(databasePopulator);
        bean.afterPropertiesSet();   // EmbeddedDatabaseFactoryBean가 FactoryBean이므로 필요합니다.
        return bean.getObject();
    }

    @Bean
    public DataSource dataSource() {
        return buildEmbeddedDataSource();
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setPackagesToScan(getMappedPackageNames());
        factoryBean.setHibernateProperties(hibernateProperties());
        factoryBean.setDataSource(dataSource());
        factoryBean.setEntityInterceptor(hibernateInterceptor());

        // 꼭 이 함수를 호출해야 합니다.
        try {
            factoryBean.afterPropertiesSet();
        } catch (IOException e) {
            throw new RuntimeException("SessionFactory 빌드에 실패했습니다.", e);
        }

        return factoryBean.getObject();
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    @Bean
    public MultiInterceptor hibernateInterceptor() {
        MultiInterceptor interceptor = new MultiInterceptor();

        interceptor.getInterceptors().add(statuefulEntityInterceptor());
        interceptor.getInterceptors().add(updateTimestampedInterceptor());

        return interceptor;
    }

    @Bean
    public StatefulEntityInterceptor statuefulEntityInterceptor() {
        return new StatefulEntityInterceptor();
    }

    @Bean
    public UpdateTimestampedInterceptor updateTimestampedInterceptor() {
        return new UpdateTimestampedInterceptor();
    }

    @Bean
    public UnitOfWorkFactory unitOfWorkFactory() {
        UnitOfWorkFactory factory = new UnitOfWorkFactory();
        factory.setSessionFactory(sessionFactory());
        return factory;
    }

    @Bean
    @Scope("prototype")
    public HibernateDaoFactory hibernateDaoFactory() {
        return new HibernateDaoFactory();
    }
}

