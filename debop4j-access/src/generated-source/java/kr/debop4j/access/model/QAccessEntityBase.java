package kr.debop4j.access.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAccessEntityBase is a Querydsl query type for AccessEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAccessEntityBase extends EntityPathBase<AccessEntityBase> {

    private static final long serialVersionUID = -1573857526;

    public static final QAccessEntityBase accessEntityBase = new QAccessEntityBase("accessEntityBase");

    public final kr.debop4j.data.model.QAnnotatedEntityBase _super = new kr.debop4j.data.model.QAnnotatedEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QAccessEntityBase(String variable) {
        super(AccessEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAccessEntityBase(Path<? extends AccessEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QAccessEntityBase(PathMetadata<?> metadata) {
        super(AccessEntityBase.class, metadata);
    }

}

