package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QProductPreference is a Querydsl query type for ProductPreference */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QProductPreference extends EntityPathBase<ProductPreference> {

    private static final long serialVersionUID = 2068878073;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductPreference productPreference = new QProductPreference("productPreference");

    public final kr.debop4j.access.model.QPreferenceBase _super = new kr.debop4j.access.model.QPreferenceBase(this);

    //inherited
    public final StringPath defaultValue = _super.defaultValue;

    //inherited
    public final StringPath description = _super.description;

    //inherited
    public final StringPath exAttr = _super.exAttr;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath key = _super.key;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QProduct product;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    //inherited
    public final StringPath value = _super.value;

    public QProductPreference(String variable) {
        this(ProductPreference.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QProductPreference(Path<? extends ProductPreference> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductPreference(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductPreference(PathMetadata<?> metadata, PathInits inits) {
        this(ProductPreference.class, metadata, inits);
    }

    public QProductPreference(Class<? extends ProductPreference> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

