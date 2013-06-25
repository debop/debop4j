package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QProduct is a Querydsl query type for Product */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -1701527682;

    public static final QProduct product = new QProduct("product");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final StringPath abbrName = createString("abbrName");

    public final BooleanPath active = createBoolean("active");

    public final StringPath code = createString("code");

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QProduct(Path<? extends Product> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata<?> metadata) {
        super(Product.class, metadata);
    }

}

