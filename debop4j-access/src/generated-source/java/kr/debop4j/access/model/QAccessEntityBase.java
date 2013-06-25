package kr.debop4j.access.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QAccessEntityBase is a Querydsl query type for AccessEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAccessEntityBase extends EntityPathBase<AccessEntityBase> {

    private static final long serialVersionUID = -1573857526;

    public static final QAccessEntityBase accessEntityBase = new QAccessEntityBase("accessEntityBase");

    public final kr.debop4j.data.model.QAnnotatedEntityBase _super = new kr.debop4j.data.model.QAnnotatedEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<org.joda.time.DateTime> updatedTime = createDateTime("updatedTime", org.joda.time.DateTime.class);

    public QAccessEntityBase(String variable) {
        super(AccessEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAccessEntityBase(Path<? extends AccessEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QAccessEntityBase(PathMetadata<?> metadata) {
        super(AccessEntityBase.class, metadata);
    }

}

