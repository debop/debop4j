package kr.debop4j.access.model.product;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QUserFavorite is a Querydsl query type for UserFavorite
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUserFavorite extends EntityPathBase<UserFavorite> {

    private static final long serialVersionUID = -2141098664;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFavorite userFavorite = new QUserFavorite("userFavorite");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath content = createString("content");

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final NumberPath<Integer> preference = createNumber("preference", Integer.class);

    public final DateTimePath<java.util.Date> registDate = createDateTime("registDate", java.util.Date.class);

    public final QUser user;

    public QUserFavorite(String variable) {
        this(UserFavorite.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QUserFavorite(Path<? extends UserFavorite> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUserFavorite(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUserFavorite(PathMetadata<?> metadata, PathInits inits) {
        this(UserFavorite.class, metadata, inits);
    }

    public QUserFavorite(Class<? extends UserFavorite> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

