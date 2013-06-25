package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QEmployeeCodeBase is a Querydsl query type for EmployeeCodeBase */
@Generated( "com.mysema.query.codegen.SupertypeSerializer" )
public class QEmployeeCodeBase extends EntityPathBase<EmployeeCodeBase> {

    private static final long serialVersionUID = 851911381;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployeeCodeBase employeeCodeBase = new QEmployeeCodeBase("employeeCodeBase");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final StringPath code = createString("code");

    public final QCompany company;

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public final NumberPath<Integer> viewOrder = createNumber("viewOrder", Integer.class);

    public QEmployeeCodeBase(String variable) {
        this(EmployeeCodeBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QEmployeeCodeBase(Path<? extends EmployeeCodeBase> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeCodeBase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeCodeBase(PathMetadata<?> metadata, PathInits inits) {
        this(EmployeeCodeBase.class, metadata, inits);
    }

    public QEmployeeCodeBase(Class<? extends EmployeeCodeBase> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

