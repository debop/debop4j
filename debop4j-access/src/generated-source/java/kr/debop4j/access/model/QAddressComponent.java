package kr.debop4j.access.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QAddressComponent is a Querydsl query type for AddressComponent */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QAddressComponent extends BeanPath<AddressComponent> {

    private static final long serialVersionUID = 1366610075;

    public static final QAddressComponent addressComponent = new QAddressComponent("addressComponent");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    public final StringPath state = createString("state");

    public final StringPath street1 = createString("street1");

    public final StringPath street2 = createString("street2");

    public final StringPath zipcode = createString("zipcode");

    public QAddressComponent(String variable) {
        super(AddressComponent.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAddressComponent(Path<? extends AddressComponent> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QAddressComponent(PathMetadata<?> metadata) {
        super(AddressComponent.class, metadata);
    }

}

