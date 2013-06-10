package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAnnotatedEntityBase is a Querydsl query type for AnnotatedEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAnnotatedEntityBase extends EntityPathBase<AnnotatedEntityBase> {

    private static final long serialVersionUID = 2041099168;

    public static final QAnnotatedEntityBase annotatedEntityBase = new QAnnotatedEntityBase("annotatedEntityBase");

    public final QStatefulEntityBase _super = new QStatefulEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QAnnotatedEntityBase(String variable) {
        super(AnnotatedEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAnnotatedEntityBase(Path<? extends AnnotatedEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QAnnotatedEntityBase(PathMetadata<?> metadata) {
        super(AnnotatedEntityBase.class, metadata);
    }

}

