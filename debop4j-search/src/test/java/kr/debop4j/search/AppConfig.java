/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.search;

import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.repository.IHibernateRepositoryFactory;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWorkFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.jdbc.JdbcTool;
import kr.debop4j.search.dao.HibernateSearchDao;
import kr.debop4j.search.dao.IHibernateSearchDao;
import kr.debop4j.search.dao.SearchDao;
import kr.debop4j.search.dao.SearchDaoImpl;
import kr.debop4j.search.hibernate.model.SearchItem;
import kr.debop4j.search.twitter.Twit;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.apache.lucene.util.Version;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.beanvalidation.BeanValidationEventListener;
import org.hibernate.event.spi.EventType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 28.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = { UnitOfWorks.class, HibernateTool.class })
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
        props.put("hibernate.search.lucene_version", "LUCENE_36");
        // props.put("hibernate.search.default.indexmanager", "near-real-time");
        props.put("hibernate.search.default.directory_provider", "filesystem");
        props.put("hibernate.search.default.indexBase", ".lucene/indexes");

        // hibernate-search sharding
//        String defaultPrefix = "hibernate.search.default";
//        props.put(defaultPrefix + ".sharding_strategy.nbr_of_shards", Integer.toString(Runtime.getRuntime().availableProcessors()));
//        props.put(defaultPrefix + ".directory_provider", FSDirectoryProvider.class.getName());

        // hibernate-search performance settings
        // props.put("hibernate.search.worker.execution", "async");
        props.put("hibernate.search.worker.thread_pool.size", "8");
        props.put("hibernate.search.worker.buffer_queue.max", "1000");
        props.put("hibernate.search.default.indexwriter.max_buffered_doc", "true");
        props.put("hibernate.search.default.indexwriter.max_merge_docs", "100");
        props.put("hibernate.search.default.indexwriter.merge_factor", "20");
        props.put("hibernate.search.default.indexwriter.term_index_interval", "default");
        props.put("hibernate.search.default.indexwriter.ram_buffer_size", "1024");

        //
        props.put("hibernate.search.default.exclusive_index_use", "true");

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
    public IUnitOfWorkFactory unitOfWorkFactory() {
        UnitOfWorkFactory factory = new UnitOfWorkFactory();
        factory.setSessionFactory(sessionFactory());
        return factory;
    }

    @Bean
    public IHibernateRepositoryFactory hibernateRepositoryFactory() {
        return new HibernateRepositoryFactory();
    }

    private static final String LUCENE_ANALYZER_CURRENT = Analyzer.class.getName() + ".Current";

    @Bean
    @Scope("prototype")
    public Analyzer luceneAnalyzer() {
        Analyzer analyzer = Local.get(LUCENE_ANALYZER_CURRENT, Analyzer.class);
        if (analyzer == null) {
            try {
                analyzer = new KoreanAnalyzer(Version.LUCENE_36);
                Local.put(LUCENE_ANALYZER_CURRENT, analyzer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return analyzer;
    }

    public static final String HIBERNATE_SEARCH_DAO_CURRENT = HibernateSearchDao.class.getName() + ".Current";

    @Bean
    @Scope("prototype")
    public IHibernateSearchDao hibernateSearchDao() {
        IHibernateSearchDao dao = Local.get(HIBERNATE_SEARCH_DAO_CURRENT, HibernateSearchDao.class);
        if (dao == null) {
            dao = new HibernateSearchDao();
            Local.put(HIBERNATE_SEARCH_DAO_CURRENT, dao);
        }
        return dao;
    }

    public static final String SEARCH_DAO_CURRENT = SearchDao.class.getName() + ".Current";

    @Bean
    @Scope("prototype")
    public SearchDao searchDao() {
        SearchDao dao = Local.get(SEARCH_DAO_CURRENT, SearchDao.class);
        if (dao == null) {
            dao = new SearchDaoImpl(); //new SearchDaoImpl(sessionFactory());
            Local.put(SEARCH_DAO_CURRENT, dao);
        }
        return dao;
    }
}
