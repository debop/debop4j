package kr.debop4j.data.mongodb.spring;

import com.mongodb.MongoClient;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.dao.MongoOgmDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.mongodb.spring.SpringConfigurationTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 17. 오후 2:15
 */
@Slf4j
public class SpringConfigurationTest extends MongoGridDatastoreTestBase {

    @Autowired MongoClient mongoClient;
    @Autowired MongoTemplate mongoTemplate;
    @Autowired MongoOgmDao mongoOgmDao;

    @Test
    public void mongoClientBean() {
        assertThat(mongoClient).isNotNull();
    }

    @Test
    public void mongoTemplateBean() {
        assertThat(mongoTemplate).isNotNull();
    }

    @Test
    public void mongoOgmDaoBean() {
        assertThat(mongoOgmDao).isNotNull();
    }
}
