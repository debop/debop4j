package kr.debop4j.data.mongodb.test.associations;

import kr.debop4j.data.ogm.test.associations.collection.manytomany.ManyToManyTest;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.test.associations.ManyToManyGlobalTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 9:59
 */
public class ManyToManyGlobalTest extends ManyToManyTest {

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(Environment.MONGODB_ASSOCIATIONS_STORE,
                        AssociationStorage.GLOBAL_COLLECTION.name());
    }
}
