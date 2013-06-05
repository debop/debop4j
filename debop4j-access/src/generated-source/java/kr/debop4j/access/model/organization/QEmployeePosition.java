package kr.debop4j.access.model.organization;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEmployeePosition is a Querydsl query type for EmployeePosition
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmployeePosition extends EntityPathBase<EmployeePosition> {

    private static final long serialVersionUID = -1826729568;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployeePosition employeePosition = new QEmployeePosition("employeePosition");

    public final QEmployeeCodeBase _super;

    //inherited
    public final StringPath code;

    // inherited
    public final QCompany company;

    //inherited
    public final StringPath description;

    //inherited
    public final StringPath exAttr;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath name;

    //inherited
    public final BooleanPath persisted;

    //inherited
    public final NumberPath<Integer> viewOrder;

    public QEmployeePosition(String variable) {
        this(EmployeePosition.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QEmployeePosition(Path<? extends EmployeePosition> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeePosition(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeePosition(PathMetadata<?> metadata, PathInits inits) {
        this(EmployeePosition.class, metadata, inits);
    }

    public QEmployeePosition(Class<? extends EmployeePosition> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QEmployeeCodeBase(type, metadata, inits);
        this.code = _super.code;
        this.company = _super.company;
        this.description = _super.description;
        this.exAttr = _super.exAttr;
        this.name = _super.name;
        this.persisted = _super.persisted;
        this.viewOrder = _super.viewOrder;
    }

}

