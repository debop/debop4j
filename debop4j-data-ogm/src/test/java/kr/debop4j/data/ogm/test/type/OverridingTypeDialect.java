package kr.debop4j.data.ogm.test.type;

import org.hibernate.ogm.datastore.map.impl.HashMapDialect;
import org.hibernate.ogm.datastore.map.impl.MapDatastoreProvider;
import org.hibernate.ogm.type.GridType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.util.UUID;

/**
 * kr.debop4j.data.ogm.test.type.OverridingTypeDialect
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 5:49
 */
public class OverridingTypeDialect extends HashMapDialect {

    public OverridingTypeDialect(MapDatastoreProvider provider) {
        super(provider);
    }

    @Override
    public GridType overrideType(Type type) {
        //all UUID properties are mapped with exploding type
        if (UUID.class.equals(type.getReturnedClass())) {
            return ExplodingType.INSTANCE;
        }
        //timestamp and time mapping are ignored, only raw dates are handled
        if (type == StandardBasicTypes.DATE) {
            return CustomDateType.INSTANCE;
        }
        return null;
    }
}
