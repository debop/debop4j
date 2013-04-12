package kr.debop4j.data.mongodb.ogm.test.associations;

import kr.debop4j.data.ogm.test.associations.collection.unidirectional.CollectionUnidirectionalTest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.ogm.test.associations.CollectionUnidirectionalCollectionTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 10. 오후 9:48
 */
@Slf4j
public class CollectionUnidirectionalCollectionTest extends CollectionUnidirectionalTest {

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(Environment.MONGODB_ASSOCIATIONS_STORE,
                        AssociationStorage.COLLECTION.name());
    }
}
