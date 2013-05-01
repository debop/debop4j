package kr.debop4j.data.mongodb.spring;

import kr.debop4j.data.mongodb.MongoGridDatastoreConfiguration;
import kr.debop4j.data.mongodb.model.MongoDoc;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.mongodb.spring.MongoTemplateTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 17. 오후 4:04
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoGridDatastoreConfiguration.class })
public class MongoTemplateTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void injectTest() {
        assertThat(mongoTemplate).isNotNull();
    }

    @Test
    public void collectionExistsTest() {
        if (mongoTemplate.collectionExists(MongoDoc.class)) {
            mongoTemplate.dropCollection(MongoDoc.class);
            assertThat(mongoTemplate.collectionExists(MongoDoc.class)).isFalse();
        }
        mongoTemplate.createCollection(MongoDoc.class);
        assertThat(mongoTemplate.collectionExists(MongoDoc.class)).isTrue();
    }

    @Test
    public void crudTest() throws Exception {
        if (mongoTemplate.collectionExists(MongoDoc.class)) {
            mongoTemplate.dropCollection(MongoDoc.class);
        }
        assertThat(mongoTemplate.count(new Query(), MongoDoc.class)).isEqualTo(0);

        String collectionName = mongoTemplate.getCollectionName(MongoDoc.class);

        mongoTemplate.insert(new MongoDoc("name-1"));
        mongoTemplate.insert(new MongoDoc("name-2"));
        mongoTemplate.insert(new MongoDoc("name-3"));

        assertThat(mongoTemplate.count(new Query(), collectionName)).isEqualTo(3);
        assertThat(mongoTemplate.count(new Query(), MongoDoc.class)).isEqualTo(3);

        mongoTemplate.insert(new MongoDoc("aaa"));

        List<MongoDoc> found = mongoTemplate.find(new Query(Criteria.where("name").is("aaa")), MongoDoc.class);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getName()).isEqualTo("aaa");

        mongoTemplate.remove(found.get(0), collectionName);
        found = mongoTemplate.find(new Query(Criteria.where("name").is("aaa")), MongoDoc.class);
        assertThat(found.size()).isEqualTo(0);

        assertThat(mongoTemplate.count(new Query(), collectionName)).isEqualTo(3);
        assertThat(mongoTemplate.count(new Query(), MongoDoc.class)).isEqualTo(3);

        mongoTemplate.dropCollection(MongoDoc.class);
    }
}
