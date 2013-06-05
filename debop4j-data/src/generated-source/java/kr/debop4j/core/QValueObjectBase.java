package kr.debop4j.core;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QValueObjectBase is a Querydsl query type for ValueObjectBase
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QValueObjectBase extends BeanPath<ValueObjectBase> {

    private static final long serialVersionUID = 1986431503;

    public static final QValueObjectBase valueObjectBase = new QValueObjectBase("valueObjectBase");

    public QValueObjectBase(String variable) {
        super(ValueObjectBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QValueObjectBase(Path<? extends ValueObjectBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QValueObjectBase(PathMetadata<?> metadata) {
        super(ValueObjectBase.class, metadata);
    }

}

