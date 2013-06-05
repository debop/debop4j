package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QEntityBase is a Querydsl query type for EntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QEntityBase extends EntityPathBase<EntityBase<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 2011387712;

    public static final QEntityBase entityBase = new QEntityBase("entityBase");

    public final QStatefulEntityBase _super = new QStatefulEntityBase(this);

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QEntityBase(String variable) {
        super((Class)EntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QEntityBase(Path<? extends EntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QEntityBase(PathMetadata<?> metadata) {
        super((Class)EntityBase.class, metadata);
    }

}

