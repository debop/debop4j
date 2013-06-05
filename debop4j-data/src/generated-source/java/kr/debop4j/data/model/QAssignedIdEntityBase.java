package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAssignedIdEntityBase is a Querydsl query type for AssignedIdEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAssignedIdEntityBase extends EntityPathBase<AssignedIdEntityBase<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1839890601;

    public static final QAssignedIdEntityBase assignedIdEntityBase = new QAssignedIdEntityBase("assignedIdEntityBase");

    public final QEntityBase _super = new QEntityBase(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QAssignedIdEntityBase(String variable) {
        super((Class)AssignedIdEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAssignedIdEntityBase(Path<? extends AssignedIdEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QAssignedIdEntityBase(PathMetadata<?> metadata) {
        super((Class)AssignedIdEntityBase.class, metadata);
    }

}

