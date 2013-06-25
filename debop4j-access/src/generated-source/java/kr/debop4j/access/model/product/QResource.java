package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QResource is a Querydsl query type for Resource */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QResource extends EntityPathBase<Resource> {

    private static final long serialVersionUID = -545053857;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResource resource = new QResource("resource");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final StringPath code = createString("code");

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QProduct product;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QResource(String variable) {
        this(Resource.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QResource(Path<? extends Resource> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QResource(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QResource(PathMetadata<?> metadata, PathInits inits) {
        this(Resource.class, metadata, inits);
    }

    public QResource(Class<? extends Resource> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

