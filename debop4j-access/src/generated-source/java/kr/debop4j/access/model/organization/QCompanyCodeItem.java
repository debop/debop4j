package kr.debop4j.access.model.organization;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QCompanyCodeItem is a Querydsl query type for CompanyCodeItem
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCompanyCodeItem extends EntityPathBase<CompanyCodeItem> {

    private static final long serialVersionUID = 1298865524;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyCodeItem companyCodeItem = new QCompanyCodeItem("companyCodeItem");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final QCompanyCode code;

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final StringPath value = createString("value");

    public QCompanyCodeItem(String variable) {
        this(CompanyCodeItem.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QCompanyCodeItem(Path<? extends CompanyCodeItem> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyCodeItem(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyCodeItem(PathMetadata<?> metadata, PathInits inits) {
        this(CompanyCodeItem.class, metadata, inits);
    }

    public QCompanyCodeItem(Class<? extends CompanyCodeItem> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.code = inits.isInitialized("code") ? new QCompanyCode(forProperty("code"), inits.get("code")) : null;
    }

}

