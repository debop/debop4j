package kr.debop4j.data.mongodb.ogm.test.associations;

import kr.debop4j.data.ogm.test.id.CompositeIdTest;
import kr.debop4j.data.ogm.test.utils.jpa.GetterPersistenceUnitInfo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.ogm.test.associations.CompositeIdInEmbeddedTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오후 9:51
 */
@Slf4j
public class CompositeIdInEmbeddedTest extends CompositeIdTest {

    @Override
    protected void refineInfo(GetterPersistenceUnitInfo info) {
        super.refineInfo(info);

        info.getProperties().setProperty(Environment.MONGODB_ASSOCIATIONS_STORE,
                                         AssociationStorage.IN_ENTITY.name());
    }
}
