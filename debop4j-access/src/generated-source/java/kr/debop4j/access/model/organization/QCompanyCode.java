package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QCompanyCode is a Querydsl query type for CompanyCode */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QCompanyCode extends EntityPathBase<CompanyCode> {

    private static final long serialVersionUID = -1521256767;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyCode companyCode = new QCompanyCode("companyCode");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final StringPath code = createString("code");

    public final QCompany company;

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<CompanyCodeItem, QCompanyCodeItem> items = this.<CompanyCodeItem, QCompanyCodeItem>createSet("items", CompanyCodeItem.class, QCompanyCodeItem.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QCompanyCode(String variable) {
        this(CompanyCode.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QCompanyCode(Path<? extends CompanyCode> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyCode(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyCode(PathMetadata<?> metadata, PathInits inits) {
        this(CompanyCode.class, metadata, inits);
    }

    public QCompanyCode(Class<? extends CompanyCode> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

