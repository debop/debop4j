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

package kr.debop4j.data.mongodb.spring;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import kr.debop4j.core.Local;
import kr.debop4j.data.mongodb.dao.MongoOgmDao;
import kr.debop4j.data.mongodb.tools.MongoTool;
import kr.debop4j.data.ogm.spring.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.net.UnknownHostException;
import java.util.Properties;

/**
 * MongoDB 를 hibernate-ogm 엔티티의 저장소로 사용하도록 하는 Spring 환경설정입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 29
 */
@Configuration
@Slf4j
public abstract class MongoGridDatastoreConfigBase extends GridDatastoreConfigBase {

    public static final String MONGODB_DATASTORE_PROVIDER =
            "org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider";

    @Bean
    public ServerAddress serverAddress() {
        try {
            return new ServerAddress("localhost");
        } catch (UnknownHostException e) {
            MongoGridDatastoreConfigBase.log.error("서버를 찾지 못했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Properties getHibernateProperties() {
        Properties props = super.getHibernateProperties();

        String defaultPrefix = "hibernate.search.default";
        props.put(defaultPrefix + ".sharding_strategy.nbr_of_shards", Integer.toString(Runtime.getRuntime().availableProcessors()));
        props.put(defaultPrefix + ".directory_provider", FSDirectoryProvider.class.getName());

        return props;
    }

    @Override
    protected Properties getHibernateOgmProperties() {
        Properties props = super.getHibernateOgmProperties();

        props.put("hibernate.ogm.datastore.provider", MONGODB_DATASTORE_PROVIDER);
        props.put(Environment.MONGODB_DATABASE, getDatabaseName());
        props.put(Environment.MONGODB_TIMEOUT, 200);

        props.put(Environment.MONGODB_HOST, serverAddress().getHost());
        props.put(Environment.MONGODB_PORT, serverAddress().getPort());

        // 엔티티 저장 방식
        props.put(Environment.MONGODB_ASSOCIATIONS_STORE, getAssociationStorage().name());

        if (MongoGridDatastoreConfigBase.log.isDebugEnabled())
            MongoGridDatastoreConfigBase.log.debug("hibernate-ogm 환경설정 정보를 지정했습니다. props=\n{}", props.toString());

        return props;
    }

    protected AssociationStorage getAssociationStorage() {
        return AssociationStorage.IN_ENTITY;
    }

    private static final String MONGO_OGM_DAO_CLASS_NAME = MongoOgmDao.class.getName();

    @Override
    @Bean
    @Scope("prototype")
    public MongoOgmDao hibernateOgmDao() {
        MongoOgmDao dao = Local.get(MONGO_OGM_DAO_CLASS_NAME, MongoOgmDao.class);
        if (dao == null) {
            dao = new MongoOgmDao(sessionFactory());
            Local.put(MONGO_OGM_DAO_CLASS_NAME, dao);

            if (MongoGridDatastoreConfigBase.log.isDebugEnabled())
                MongoGridDatastoreConfigBase.log.debug("현 스레드에서 새로운 MongoOgmDao 인스턴스를 생성했습니다. ThreadId=[{}]", Thread.currentThread().getId());
        }
        return dao;
    }

    private static final String MONGO_CLIENT_CLASS_NAME = MongoClient.class.getName();

    @Bean
    @Scope("prototype")
    public MongoClient mongoClient() {
        MongoClient client = Local.get(MONGO_CLIENT_CLASS_NAME, MongoClient.class);
        if (client == null) {
            client = new MongoClient(serverAddress());
            Local.put(MONGO_CLIENT_CLASS_NAME, client);
        }
        return client;
    }

    private static final String MONGO_TEMPLATE_CLASS_NAME = MongoTemplate.class.getName();

    @Bean
    @Scope("prototype")
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = Local.get(MONGO_TEMPLATE_CLASS_NAME, MongoTemplate.class);
        if (template == null) {
            template = new MongoTemplate(mongoClient(), getDatabaseName());
            Local.put(MONGO_TEMPLATE_CLASS_NAME, template);
        }
        return template;
    }

    @Bean
    public MongoTool mongoTool() {
        GridDialect dialect = gridDialect();
        DatastoreProvider provider = datastoreProvider();

        return new MongoTool(dialect, provider);
    }
}