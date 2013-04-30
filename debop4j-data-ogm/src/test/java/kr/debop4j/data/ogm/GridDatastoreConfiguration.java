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

package kr.debop4j.data.ogm;

import kr.debop4j.data.ogm.dao.Player;
import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * kr.debop4j.data.ogm.GridDatastoreConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 12:05
 */
@Configuration
@Slf4j
public class GridDatastoreConfiguration extends GridDatastoreConfigBase {
    @Override
    protected String getDatabaseName() {
        return "debop4j_ogm";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Player.class.getPackage().getName()
        };
    }

    @Override
    protected Properties getHibernateOgmProperties() {
        Properties props = super.getHibernateOgmProperties();

        // hibernate-search 환경설정
        props.put("hibernate.search.default.indexmanager", "near-real-time");
        props.put("hibernate.search.default.directory_provider", "filesystem");
        props.put("hibernate.search.default.indexBase", ".lucene/indexes");
        props.put("hibernate.search.default.locking_strategy", "single");

        // hibernate-search index worker settings
        props.put("hibernate.search.worker.execution", "async");
        props.put("hibernate.search.worker.thread_pool.size", "8");
        props.put("hibernate.search.worker.buffer_queue.max", "1000000");

        // hibernate-search performance settings
        props.put("hibernate.search.default.indexwriter.max_buffered_doc", "true");
        props.put("hibernate.search.default.indexwriter.max_merge_docs", "100");
        props.put("hibernate.search.default.indexwriter.merge_factor", "20");
        props.put("hibernate.search.default.indexwriter.term_index_interval", "default");
        props.put("hibernate.search.default.indexwriter.ram_buffer_size", "2048");
        props.put("hibernate.search.default.exclusive_index_use", "true");

        return props;
    }
}
