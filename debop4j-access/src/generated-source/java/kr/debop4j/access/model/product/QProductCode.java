package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QProductCode is a Querydsl query type for ProductCode */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductCode extends EntityPathBase<ProductCode> {

    private static final long serialVersionUID = -1154682229;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductCode productCode = new QProductCode("productCode");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final StringPath code = createString("code");

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<ProductCodeItem, QProductCodeItem> items = this.<ProductCodeItem, QProductCodeItem>createSet("items", ProductCodeItem.class, QProductCodeItem.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QProduct product;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QProductCode(String variable) {
        this(ProductCode.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QProductCode(Path<? extends ProductCode> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductCode(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductCode(PathMetadata<?> metadata, PathInits inits) {
        this(ProductCode.class, metadata, inits);
    }

    public QProductCode(Class<? extends ProductCode> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

