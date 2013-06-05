package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QMetaEntityBase is a Querydsl query type for MetaEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QMetaEntityBase extends EntityPathBase<MetaEntityBase<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1556181285;

    public static final QMetaEntityBase metaEntityBase = new QMetaEntityBase("metaEntityBase");

    public final QEntityBase _super = new QEntityBase(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    public final SetPath<String, StringPath> metaKeys = this.<String, StringPath>createSet("metaKeys", String.class, StringPath.class, PathInits.DIRECT2);

    public final MapPath<String, IMetaValue, SimplePath<IMetaValue>> metaMap = this.<String, IMetaValue, SimplePath<IMetaValue>>createMap("metaMap", String.class, IMetaValue.class, SimplePath.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QMetaEntityBase(String variable) {
        super((Class)MetaEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QMetaEntityBase(Path<? extends MetaEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QMetaEntityBase(PathMetadata<?> metadata) {
        super((Class)MetaEntityBase.class, metadata);
    }

}

