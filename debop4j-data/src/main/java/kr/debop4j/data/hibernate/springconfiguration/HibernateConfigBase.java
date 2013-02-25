package kr.debop4j.data.hibernate.springconfiguration;

import com.jolbox.bonecp.BoneCPDataSource;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextBase;
import kr.debop4j.data.hibernate.interceptor.MultiInterceptor;
import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.repository.HibernateDaoFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * hibernate 의 환경설정을 spring framework의 bean 환경설정으로 구현했습니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public abstract class HibernateConfigBase {

    @Getter
    @Setter
    private UnitOfWorkTestContextBase testContext;

    abstract protected String getDatabaseName();

    abstract protected String[] getMappedPackageNames();

    protected Properties hibernateProperties() {

        Properties props = new Properties();

        props.put(Environment.FORMAT_SQL, "true");
        props.put(Environment.HBM2DDL_AUTO, "create"); // create | spawn | spawn-drop | update | validate
        props.put(Environment.SHOW_SQL, "true");
        props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
        props.put(Environment.AUTOCOMMIT, "true");
        props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.put(Environment.STATEMENT_BATCH_SIZE, "50");

        return props;
    }

    protected DataSource buildDataSource(String driverClass, String url, String username, String password) {

        if (log.isDebugEnabled())
            log.debug("build BoneCPDataSource... driverClass=[{}], url=[{}], username=[{}], password=[{}]",
                      driverClass, url, username, password);

//        BasicDataSource ds = new BasicDataSource();
//        ds.setDriverClassName(driverClass);
//        ds.setUrl(url);
//        if (StringTool.isNotWhiteSpace(username))
//            ds.setUsername(username);
//        if (StringTool.isNotWhiteSpace(password))
//            ds.setPassword(password);
//
//        return ds;

        //NOTE: BoneCP가 버그가 있다. - 테스트 후 잘되는 DB만 쓸 것

        BoneCPDataSource ds = new BoneCPDataSource();

        ds.setDriverClass(driverClass);
        ds.setJdbcUrl(url);

        if (StringTool.isNotWhiteSpace(username))
            ds.setUsername(username);
        if (StringTool.isNotWhiteSpace(password))
            ds.setPassword(password);

        ds.setPartitionCount(2);
        ds.setMaxConnectionsPerPartition(50);

        return ds;
    }

    protected DataSource buildEmbeddedDataSource() {
        EmbeddedDatabaseFactoryBean bean = new EmbeddedDatabaseFactoryBean();
        bean.afterPropertiesSet();   // EmbeddedDatabaseFactoryBean가 FactoryBean이므로 필요합니다.
        return bean.getObject();
    }

    @Bean(destroyMethod = "close")
    abstract public DataSource dataSource();

    /**
     * factoryBean 에 추가 설정을 지정할 수 있습니다.
     *
     * @param factoryBean
     */
    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) {
        // Nothing
    }

    @Bean
    public SessionFactory sessionFactory() {

        if (log.isInfoEnabled())
            log.info("SessionFactory Bean을 생성합니다...");

        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        String[] packageNames = getMappedPackageNames();
        if (packageNames != null)
            factoryBean.setPackagesToScan(getMappedPackageNames());

        factoryBean.setHibernateProperties(hibernateProperties());
        factoryBean.setDataSource(dataSource());
        factoryBean.setEntityInterceptor(hibernateInterceptor());

        // Drived class에서 추가 작업을 수행할 수 있도록 합니다.
        setupSessionFactory(factoryBean);

        try {
            factoryBean.afterPropertiesSet();

            if (log.isInfoEnabled())
                log.info("SessionFactory Bean을 생성했습니다!!!");

            return factoryBean.getObject();

        } catch (IOException e) {
            throw new RuntimeException("SessionFactory 빌드에 실패했습니다.", e);
        }
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
    public HibernateDaoFactory hibernateDaoFactory() {
        return new HibernateDaoFactory();
    }
}

