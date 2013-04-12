package kr.debop4j.data.mongodb.ogm.test.associations;

import kr.debop4j.data.ogm.test.associations.collection.unidirectional.CollectionUnidirectionalTest;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.ogm.test.associations.CollectionUnidirectionalGlobalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 5:39
 */
public class CollectionUnidirectionalGlobalTest extends CollectionUnidirectionalTest {

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(Environment.MONGODB_ASSOCIATIONS_STORE, AssociationStorage.GLOBAL_COLLECTION.name());
    }
}
