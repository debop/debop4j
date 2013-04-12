package kr.debop4j.data.mongodb.ogm.test.associations;

import kr.debop4j.data.ogm.test.associations.collection.manytomany.ManyToManyTest;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.ogm.test.associations.ManyToManyInEntityTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 10:00
 */
public class ManyToManyInEntityTest extends ManyToManyTest {

    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(Environment.MONGODB_ASSOCIATIONS_STORE,
                        AssociationStorage.IN_ENTITY.name());
    }
}
