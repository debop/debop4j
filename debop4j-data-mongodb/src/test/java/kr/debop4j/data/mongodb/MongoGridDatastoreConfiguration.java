package kr.debop4j.data.mongodb;

import kr.debop4j.data.mongodb.model.Project;
import kr.debop4j.data.mongodb.spring.cfg.MongoGridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * kr.debop4j.data.mongodb.ogm.MongoDbConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Configuration
@ComponentScan({ "kr.debop4j.data.mongodb.spring.cfg",
                       "kr.debop4j.data.mongodb.tools" })
@Slf4j
public class MongoGridDatastoreConfiguration extends MongoGridDatastoreConfigBase {

    @Override
    protected String getDatabaseName() {
        return "debop4j_data";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Project.class.getPackage().getName(),
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
