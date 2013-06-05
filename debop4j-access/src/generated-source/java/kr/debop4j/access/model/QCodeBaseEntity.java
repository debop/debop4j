package kr.debop4j.access.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QCodeBaseEntity is a Querydsl query type for CodeBaseEntity
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QCodeBaseEntity extends EntityPathBase<CodeBaseEntity> {

    private static final long serialVersionUID = -1411441261;

    public static final QCodeBaseEntity codeBaseEntity = new QCodeBaseEntity("codeBaseEntity");

    public final kr.debop4j.data.model.QLongAnnotatedEntityBase _super = new kr.debop4j.data.model.QLongAnnotatedEntityBase(this);

    public final StringPath code = createString("code");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QCodeBaseEntity(String variable) {
        super(CodeBaseEntity.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QCodeBaseEntity(Path<? extends CodeBaseEntity> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QCodeBaseEntity(PathMetadata<?> metadata) {
        super(CodeBaseEntity.class, metadata);
    }

}

