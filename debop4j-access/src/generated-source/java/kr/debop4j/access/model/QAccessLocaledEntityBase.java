package kr.debop4j.access.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAccessLocaledEntityBase is a Querydsl query type for AccessLocaledEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAccessLocaledEntityBase extends EntityPathBase<AccessLocaledEntityBase<? extends kr.debop4j.data.model.ILocaleValue>> {

    private static final long serialVersionUID = 1935957960;

    public static final QAccessLocaledEntityBase accessLocaledEntityBase = new QAccessLocaledEntityBase("accessLocaledEntityBase");

    public final kr.debop4j.data.model.QAnnotatedLocaleEntityBase _super = new kr.debop4j.data.model.QAnnotatedLocaleEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<java.util.Date> updateTimestamp = createDateTime("updateTimestamp", java.util.Date.class);

    @SuppressWarnings("all")
    public QAccessLocaledEntityBase(String variable) {
        super((Class)AccessLocaledEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAccessLocaledEntityBase(Path<? extends AccessLocaledEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QAccessLocaledEntityBase(PathMetadata<?> metadata) {
        super((Class)AccessLocaledEntityBase.class, metadata);
    }

}

