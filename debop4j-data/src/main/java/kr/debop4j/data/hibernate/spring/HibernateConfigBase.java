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

package kr.debop4j.data.hibernate.spring;

import kr.debop4j.core.Local;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextBase;
import kr.debop4j.data.hibernate.interceptor.MultiInterceptor;
import kr.debop4j.data.hibernate.interceptor.StatefulEntityInterceptor;
import kr.debop4j.data.hibernate.interceptor.UpdateTimestampedInterceptor;
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import kr.debop4j.data.hibernate.repository.IHibernateRepositoryFactory;
import kr.debop4j.data.hibernate.repository.impl.HibernateDao;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWorkFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.jdbc.JdbcTool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * hibernate 의 환경설정을 spring framework의 bean 환경설정으로 구현했습니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 21.
 */
@Slf4j
@ComponentScan(basePackageClasses = { UnitOfWorks.class, HibernateTool.class })
public abstract class HibernateConfigBase {

    @Getter
    @Setter
    private UnitOfWorkTestContextBase testContext;

    /** Database 명 */
    abstract public String getDatabaseName();

    /** Database JDBC URL */
    abstract public String getJdbcUrl();

    /** 사용자 Id */
    public String getUsername() {
        return "";
    }

    /** 사용자 비밀번호 */
    public String getPassword() {
        return "";
    }

    /** 매핑된 엔티티가 있는 Pagacke 명들 */
    abstract protected String[] getMappedPackageNames();

    /** Hibernate 설정 값 */
    protected Properties hibernateProperties() {

        Properties props = new Properties();

        props.put(Environment.FORMAT_SQL, "true");
        props.put(Environment.HBM2DDL_AUTO, "create"); // create | spawn | spawn-drop | update | validate | none
        props.put(Environment.SHOW_SQL, "true");
        props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
        props.put(Environment.AUTOCOMMIT, "true");
        props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.put(Environment.STATEMENT_BATCH_SIZE, "30");

        return props;
    }

    protected DataSource buildDataSource(String driverClass, String url, String username, String password) {
        return JdbcTool.getDataSource(driverClass, url, username, password);
    }

    protected DataSource buildEmbeddedDataSource() {
        return JdbcTool.getEmbeddedHsqlDataSource();
    }

    @Bean(destroyMethod = "close")
    abstract public DataSource dataSource();

    /** factoryBean 에 추가 설정을 지정할 수 있습니다. */
    protected void setupSessionFactory(LocalSessionFactoryBean factoryBean) { }

    @Bean
    public SessionFactory sessionFactory() {

        if (log.isInfoEnabled())
            log.info("SessionFactory Bean을 생성합니다...");

        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        String[] packageNames = getMappedPackageNames();
        if (packageNames != null) {
            log.info("hibernate용 entity를 scan합니다. packages=[{}]", StringTool.listToString(packageNames));
            factoryBean.setPackagesToScan(packageNames);
        }

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
    public HibernateTransactionManager hibernateTransactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    @Bean
    public MultiInterceptor hibernateInterceptor() {

        MultiInterceptor interceptor = new MultiInterceptor();
        interceptor.getInterceptors().add(new StatefulEntityInterceptor());
        interceptor.getInterceptors().add(new UpdateTimestampedInterceptor());

        return interceptor;
    }

    /** {@link IUnitOfWorkFactory} 를 생성합니다. */
    @Bean
    public IUnitOfWorkFactory unitOfWorkFactory() {
        log.info("UnitOfWorkFactory를 생성합니다.");
        UnitOfWorkFactory factory = new UnitOfWorkFactory();
        factory.setSessionFactory(sessionFactory());
        return factory;
    }

    private static final String HIBERNATE_DAO_KEY = HibernateDao.class.getName() + ".Current";

    /**
     * {@link IHibernateDao}를 Transaction Context별로 제공합니다.
     *
     * @return
     */
    @Bean
    @Scope("prototype")
    public IHibernateDao hibernateDao() {
        IHibernateDao hibernateDao = Local.get(HIBERNATE_DAO_KEY, IHibernateDao.class);
        if (hibernateDao == null) {
            hibernateDao = new HibernateDao(true);
            Local.put(HIBERNATE_DAO_KEY, hibernateDao);

            if (log.isDebugEnabled())
                log.debug("HibernateDao 인스턴스를 생성했습니다.");
        }
        return hibernateDao;
    }

    @Bean
    public IHibernateRepositoryFactory hibernateRepositoryFactory() {
        return new HibernateRepositoryFactory();
    }
}

