package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStatefulEntityBase is a Querydsl query type for StatefulEntityBase
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QStatefulEntityBase extends BeanPath<StatefulEntityBase> {

    private static final long serialVersionUID = -617739348;

    public static final QStatefulEntityBase statefulEntityBase = new QStatefulEntityBase("statefulEntityBase");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final BooleanPath persisted = createBoolean("persisted");

    public QStatefulEntityBase(String variable) {
        super(StatefulEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QStatefulEntityBase(Path<? extends StatefulEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QStatefulEntityBase(PathMetadata<?> metadata) {
        super(StatefulEntityBase.class, metadata);
    }

}

