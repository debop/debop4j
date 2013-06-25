package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QUserPreference is a Querydsl query type for UserPreference */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QUserPreference extends EntityPathBase<UserPreference> {

    private static final long serialVersionUID = 2036490647;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPreference userPreference = new QUserPreference("userPreference");

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

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public final QUser user;

    //inherited
    public final StringPath value = _super.value;

    public QUserPreference(String variable) {
        this(UserPreference.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QUserPreference(Path<? extends UserPreference> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUserPreference(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUserPreference(PathMetadata<?> metadata, PathInits inits) {
        this(UserPreference.class, metadata, inits);
    }

    public QUserPreference(Class<? extends UserPreference> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

