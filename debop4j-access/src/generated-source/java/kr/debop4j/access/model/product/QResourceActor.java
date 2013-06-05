package kr.debop4j.access.model.product;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QResourceActor is a Querydsl query type for ResourceActor
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QResourceActor extends EntityPathBase<ResourceActor> {

    private static final long serialVersionUID = -1176905258;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResourceActor resourceActor = new QResourceActor("resourceActor");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final NumberPath<Long> actorId = createNumber("actorId", Long.class);

    public final EnumPath<ActorKind> actorKind = createEnum("actorKind", ActorKind.class);

    public final SetPath<AuthorityKind, EnumPath<AuthorityKind>> authorityKinds = this.<AuthorityKind, EnumPath<AuthorityKind>>createSet("authorityKinds", AuthorityKind.class, EnumPath.class, PathInits.DIRECT2);

    public final kr.debop4j.access.model.organization.QCompany company;

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QResource resource;

    public QResourceActor(String variable) {
        this(ResourceActor.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QResourceActor(Path<? extends ResourceActor> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QResourceActor(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QResourceActor(PathMetadata<?> metadata, PathInits inits) {
        this(ResourceActor.class, metadata, inits);
    }

    public QResourceActor(Class<? extends ResourceActor> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new kr.debop4j.access.model.organization.QCompany(forProperty("company")) : null;
        this.resource = inits.isInitialized("resource") ? new QResource(forProperty("resource"), inits.get("resource")) : null;
    }

}

