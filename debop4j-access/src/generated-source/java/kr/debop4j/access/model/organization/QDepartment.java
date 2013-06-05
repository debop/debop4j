package kr.debop4j.access.model.organization;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QDepartment is a Querydsl query type for Department
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDepartment extends EntityPathBase<Department> {

    private static final long serialVersionUID = -292851269;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepartment department = new QDepartment("department");

    public final kr.debop4j.data.model.QAnnotatedTreeEntityBase _super;

    public final BooleanPath active = createBoolean("active");

    public final SetPath<Department, QDepartment> children = this.<Department, QDepartment>createSet("children", Department.class, QDepartment.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final QCompany company;

    public final StringPath description = createString("description");

    public final StringPath enam = createString("enam");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<DepartmentMember, QDepartmentMember> members = this.<DepartmentMember, QDepartmentMember>createSet("members", DepartmentMember.class, QDepartmentMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    // inherited
    public final kr.debop4j.data.model.QTreeNodePosition nodePosition;

    public final QDepartment parent;

    //inherited
    public final BooleanPath persisted;

    public final DateTimePath<java.util.Date> updateTimestamp = createDateTime("updateTimestamp", java.util.Date.class);

    public QDepartment(String variable) {
        this(Department.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QDepartment(Path<? extends Department> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartment(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartment(PathMetadata<?> metadata, PathInits inits) {
        this(Department.class, metadata, inits);
    }

    public QDepartment(Class<? extends Department> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new kr.debop4j.data.model.QAnnotatedTreeEntityBase(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.nodePosition = _super.nodePosition;
        this.parent = inits.isInitialized("parent") ? new QDepartment(forProperty("parent"), inits.get("parent")) : null;
        this.persisted = _super.persisted;
    }

}

