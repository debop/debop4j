package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QLongAnnotatedEntityBase is a Querydsl query type for LongAnnotatedEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QLongAnnotatedEntityBase extends EntityPathBase<LongAnnotatedEntityBase> {

    private static final long serialVersionUID = -108666492;

    public static final QLongAnnotatedEntityBase longAnnotatedEntityBase = new QLongAnnotatedEntityBase("longAnnotatedEntityBase");

    public final QAnnotatedEntityBase _super = new QAnnotatedEntityBase(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QLongAnnotatedEntityBase(String variable) {
        super(LongAnnotatedEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QLongAnnotatedEntityBase(Path<? extends LongAnnotatedEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QLongAnnotatedEntityBase(PathMetadata<?> metadata) {
        super(LongAnnotatedEntityBase.class, metadata);
    }

}

