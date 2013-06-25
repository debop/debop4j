package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QGroup is a Querydsl query type for Group */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QGroup extends EntityPathBase<Group> {

    private static final long serialVersionUID = -516380042;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroup group = new QGroup("group1");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath code = createString("code");

    public final QCompany company;

    public final StringPath description = createString("description");

    public final StringPath ename = createString("ename");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<GroupMember, QGroupMember> members = this.<GroupMember, QGroupMember>createSet("members", GroupMember.class, QGroupMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QGroup(String variable) {
        this(Group.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QGroup(Path<? extends Group> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGroup(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGroup(PathMetadata<?> metadata, PathInits inits) {
        this(Group.class, metadata, inits);
    }

    public QGroup(Class<? extends Group> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

