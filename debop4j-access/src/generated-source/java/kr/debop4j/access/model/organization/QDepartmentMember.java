package kr.debop4j.access.model.organization;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDepartmentMember is a Querydsl query type for DepartmentMember
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDepartmentMember extends EntityPathBase<DepartmentMember> {

    private static final long serialVersionUID = 1741123637;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepartmentMember departmentMember = new QDepartmentMember("departmentMember");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final QDepartment department;

    public final QEmployee employee;

    public final QEmployeeTitle empTitle;

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<java.util.Date> startTime = createDateTime("startTime", java.util.Date.class);

    public QDepartmentMember(String variable) {
        this(DepartmentMember.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QDepartmentMember(Path<? extends DepartmentMember> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartmentMember(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartmentMember(PathMetadata<?> metadata, PathInits inits) {
        this(DepartmentMember.class, metadata, inits);
    }

    public QDepartmentMember(Class<? extends DepartmentMember> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.department = inits.isInitialized("department") ? new QDepartment(forProperty("department"), inits.get("department")) : null;
        this.employee = inits.isInitialized("employee") ? new QEmployee(forProperty("employee"), inits.get("employee")) : null;
        this.empTitle = inits.isInitialized("empTitle") ? new QEmployeeTitle(forProperty("empTitle"), inits.get("empTitle")) : null;
    }

}

