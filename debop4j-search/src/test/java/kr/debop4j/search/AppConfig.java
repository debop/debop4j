package kr.debop4j.search;

import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import kr.debop4j.data.jdbc.JdbcTool;
import kr.debop4j.search.dao.HibernateSearchDaoImpl;
import kr.debop4j.search.hibernate.model.SearchItem;
import kr.debop4j.search.twitter.Twit;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.util.Version;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.beanvalidation.BeanValidationEventListener;
import org.hibernate.event.spi.EventType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * hibernate와 hibernate-search를 이용한 검색 라이브러리를 테스트하기 위한 Spring 환경설정 파일입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 28.
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

        props.put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");

        props.put(Environment.FORMAT_SQL, "true");
        props.put(Environment.HBM2DDL_AUTO, "create"); // create | spawn | spawn-drop | update | validate
        props.put(Environment.SHOW_SQL, "true");
        props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
        props.put(Environment.AUTOCOMMIT, "true");
        props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.put(Environment.STATEMENT_BATCH_SIZE, "50");

        // hibernate-search 환경설정
        props.put("hibernate.search.default.indexmanager", "near-real-time");
        props.put("hibernate.search.default.directory_provider", "filesystem");
        props.put("hibernate.search.default.indexBase", "./lucene/indexes");

        // hibernate-search performance settings
        props.put("hibernate.search.worker.execution", "async");
        props.put("hibernate.search.worker.thread_pool.size", "2");
        props.put("hibernate.search.worker.buffer_queue.max", "100");
        props.put("hibernate.search.default.indexwriter.max_buffered_doc", "true");
        props.put("hibernate.search.default.indexwriter.max_merge_docs", "100");
        props.put("hibernate.search.default.indexwriter.merge_factor", "20");
        props.put("hibernate.search.default.indexwriter.term_index_interval", "default");
        props.put("hibernate.search.default.indexwriter.ram_buffer_size", "1024");

        //
        props.put("hibernate.search.default.exclusive_index_use", "true");

        // sharding


        // Validator
        props.put("javax.persistencexml.validation.group.pre-persist", "javax.validation.groups.Default");
        props.put("javax.persistencexml.validation.group.pre-update", "javax.validation.groups.Default");


        return props;
    }

    private static String[] mappingPackages = new String[]{
            SearchItem.class.getPackage().getName(),
            Twit.class.getPackage().getName()
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


            // validator용 listener 추가
            HibernateTool.registerEventListener(sessionFactory,
                                                beanValidationEventListener(),
                                                EventType.PRE_INSERT, EventType.PRE_UPDATE, EventType.PRE_DELETE);

            if (log.isInfoEnabled())
                log.info("SessionFactory Bean을 생성했습니다!!!");

            return sessionFactory;

        } catch (IOException e) {
            throw new RuntimeException("SessionFactory 빌드에 실패했습니다.", e);
        }
    }

    @Bean
    public BeanValidationEventListener beanValidationEventListener() {
        return new BeanValidationEventListener();
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    @Bean
    public org.hibernate.Interceptor hibernateInterceptor() {
        return statuefulEntityInterceptor();
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
    public Analyzer luceneAnalyzer() {
        return new CJKAnalyzer(Version.LUCENE_36);
    }

    @Bean
    @Scope("prototype")
    public HibernateSearchDaoImpl hibernateSearchDao() {
        return new HibernateSearchDaoImpl(sessionFactory());
    }
}
