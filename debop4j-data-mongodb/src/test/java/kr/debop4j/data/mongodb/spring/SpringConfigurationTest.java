package kr.debop4j.data.mongodb.spring;

import com.mongodb.MongoClient;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.dao.MongoOgmDao;
import kr.debop4j.data.ogm.dao.HibernateOgmDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.mongodb.spring.SpringConfigurationTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 17. 오후 2:15
 */
@Slf4j
public class SpringConfigurationTest extends MongoGridDatastoreTestBase {

    @Test
    public void mongoClientBean() {
        MongoClient mongoClient = Springs.getBean(MongoClient.class);
        assertThat(mongoClient).isNotNull();
    }

    @Test
    public void mongoTemplateBean() {
        MongoTemplate mongoTemplate = Springs.getBean(MongoTemplate.class);
        assertThat(mongoTemplate).isNotNull();
    }

    @Test
    public void mongoOgmDaoBean() {
        MongoOgmDao mongoOgmDao = (MongoOgmDao) Springs.getBean(HibernateOgmDao.class);
        assertThat(mongoOgmDao).isNotNull();
        //assertThat(mongoOgmDao.getMongoTemplate()).isNotNull();
    }
}
