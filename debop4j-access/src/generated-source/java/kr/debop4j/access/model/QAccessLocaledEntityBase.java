package kr.debop4j.access.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QAccessLocaledEntityBase is a Querydsl query type for AccessLocaledEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAccessLocaledEntityBase extends EntityPathBase<AccessLocaledEntityBase<? extends kr.debop4j.data.model.ILocaleValue>> {

    private static final long serialVersionUID = 1935957960;

    public static final QAccessLocaledEntityBase accessLocaledEntityBase = new QAccessLocaledEntityBase("accessLocaledEntityBase");

    public final kr.debop4j.data.model.QAnnotatedLocaleEntityBase _super = new kr.debop4j.data.model.QAnnotatedLocaleEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<org.joda.time.DateTime> updateTimestamp = createDateTime("updateTimestamp", org.joda.time.DateTime.class);

    @SuppressWarnings("all")
    public QAccessLocaledEntityBase(String variable) {
        super((Class) AccessLocaledEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAccessLocaledEntityBase(Path<? extends AccessLocaledEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QAccessLocaledEntityBase(PathMetadata<?> metadata) {
        super((Class) AccessLocaledEntityBase.class, metadata);
    }

}

