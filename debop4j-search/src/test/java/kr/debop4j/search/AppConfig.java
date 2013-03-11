package kr.debop4j.search;

import kr.debop4j.data.hibernate.interceptor.MultiInterceptor;
import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.repository.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import kr.debop4j.data.jdbc.JdbcTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * hibernate와 hibernate-search를 이용한 검색 라이브러리를 테스트하기 위한 Spring 환경설정 파일입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 28.
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class AppConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        return JdbcTool.getEmbeddedHsqlDataSource();
    }

    protected Properties hibernateProperties() {

        Properties props = new Properties();

        props.put(Environment.FORMAT_SQL, "true");
        props.put(Environment.HBM2DDL_AUTO, "create"); // create | spawn | spawn-drop | update | validate
        props.put(Environment.SHOW_SQL, "true");
        props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
        props.put(Environment.AUTOCOMMIT, "true");
        props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.put(Environment.STATEMENT_BATCH_SIZE, "50");

        // hibernate-search 환경설정
        props.put("hibernate.search.default.directory_provider", "filesystem");
        props.put("hibernate.search.default.indexBase", "lucene/indexes");

        return props;
    }

    private static String[] mappingPackages = new String[]{
            "kr.debop4j.search.hibernate.model"
    };

    @Bean
    public SessionFactory sessionFactory() {

        if (log.isInfoEnabled())
            log.info("SessionFactory Bean을 생성합니다...");

        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setHibernateProperties(hibernateProperties());
        factoryBean.setDataSource(dataSource());
        factoryBean.setEntityInterceptor(hibernateInterceptor());
        factoryBean.setPackagesToScan(mappingPackages);

        try {
            factoryBean.afterPropertiesSet();

            if (log.isInfoEnabled())
                log.info("SessionFactory Bean을 생성했습니다!!!");

            SessionFactory sessionFactory = factoryBean.getObject();

            // NOTE: hibernate-search에서 직접 등록합니다. 할 필요가 없습니다.
            // EventListener를 등록한다.
//            HibernateTool.registerEventListener(sessionFactory,
//                                                new UpdateTimestampedEventListener(),
//                                                EventType.PRE_INSERT, EventType.PRE_UPDATE);

            if (log.isInfoEnabled())
                log.info("SessionFactory Bean을 생성했습니다!!!");

            return sessionFactory;

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

        // 꼭 Springs.initByAnnotatedClasses(AppConfig.clss) 를 먼저 수행해줘야 합니다
        // UnitOfWorks.setUnitOfWorkFactory(factory);
        return factory;
    }

    @Bean
    public HibernateRepositoryFactory hibernateRepositoryFactory() {
        return new HibernateRepositoryFactory();
    }
}
