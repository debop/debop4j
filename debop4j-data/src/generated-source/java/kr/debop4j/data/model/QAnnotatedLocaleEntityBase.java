package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAnnotatedLocaleEntityBase is a Querydsl query type for AnnotatedLocaleEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAnnotatedLocaleEntityBase extends EntityPathBase<AnnotatedLocaleEntityBase<? extends ILocaleValue>> {

    private static final long serialVersionUID = 1159793818;

    public static final QAnnotatedLocaleEntityBase annotatedLocaleEntityBase = new QAnnotatedLocaleEntityBase("annotatedLocaleEntityBase");

    public final QAnnotatedEntityBase _super = new QAnnotatedEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QAnnotatedLocaleEntityBase(String variable) {
        super((Class)AnnotatedLocaleEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAnnotatedLocaleEntityBase(Path<? extends AnnotatedLocaleEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QAnnotatedLocaleEntityBase(PathMetadata<?> metadata) {
        super((Class)AnnotatedLocaleEntityBase.class, metadata);
    }

}

