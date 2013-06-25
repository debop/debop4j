package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QEmployee is a Querydsl query type for Employee */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final long serialVersionUID = 1782225751;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployee employee = new QEmployee("employee");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath code = createString("code");

    public final QCompany company;

    public final StringPath description = createString("description");

    public final QEmployeeGrade empGrade;

    public final QEmployeePosition empPosition;

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QDepartmentMember member;

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QEmployee(String variable) {
        this(Employee.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QEmployee(Path<? extends Employee> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployee(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployee(PathMetadata<?> metadata, PathInits inits) {
        this(Employee.class, metadata, inits);
    }

    public QEmployee(Class<? extends Employee> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.empGrade = inits.isInitialized("empGrade") ? new QEmployeeGrade(forProperty("empGrade"), inits.get("empGrade")) : null;
        this.empPosition = inits.isInitialized("empPosition") ? new QEmployeePosition(forProperty("empPosition"), inits.get("empPosition")) : null;
        this.member = inits.isInitialized("member") ? new QDepartmentMember(forProperty("member"), inits.get("member")) : null;
    }

}

