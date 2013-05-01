package kr.debop4j.data.mongodb.test.associations;

import kr.debop4j.data.ogm.test.associations.manytoone.ManyToOneTest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.test.associations.ManyToOneCollectionTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오후 10:06
 */
@Slf4j
public class ManyToOneCollectionTest extends ManyToOneTest {

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(Environment.MONGODB_ASSOCIATIONS_STORE,
                        AssociationStorage.COLLECTION.name());
    }
}
