package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QIntAnnotatedEntityBase is a Querydsl query type for IntAnnotatedEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QIntAnnotatedEntityBase extends EntityPathBase<IntAnnotatedEntityBase> {

    private static final long serialVersionUID = -2098397303;

    public static final QIntAnnotatedEntityBase intAnnotatedEntityBase = new QIntAnnotatedEntityBase("intAnnotatedEntityBase");

    public final QAnnotatedEntityBase _super = new QAnnotatedEntityBase(this);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QIntAnnotatedEntityBase(String variable) {
        super(IntAnnotatedEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QIntAnnotatedEntityBase(Path<? extends IntAnnotatedEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QIntAnnotatedEntityBase(PathMetadata<?> metadata) {
        super(IntAnnotatedEntityBase.class, metadata);
    }

}

