package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSimpleMetaValue is a Querydsl query type for SimpleMetaValue
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QSimpleMetaValue extends BeanPath<SimpleMetaValue> {

    private static final long serialVersionUID = -2041450898;

    public static final QSimpleMetaValue simpleMetaValue = new QSimpleMetaValue("simpleMetaValue");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final StringPath label = createString("label");

    public final StringPath value = createString("value");

    public QSimpleMetaValue(String variable) {
        super(SimpleMetaValue.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QSimpleMetaValue(Path<? extends SimpleMetaValue> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QSimpleMetaValue(PathMetadata<?> metadata) {
        super(SimpleMetaValue.class, metadata);
    }

}

