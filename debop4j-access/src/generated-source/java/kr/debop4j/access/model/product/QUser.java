package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QUser is a Querydsl query type for User */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 821861276;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final kr.debop4j.access.model.organization.QCompany company;

    public final StringPath confirmAnswer = createString("confirmAnswer");

    public final StringPath confirmQuestion = createString("confirmQuestion");

    public final kr.debop4j.access.model.organization.QDepartment department;

    public final kr.debop4j.access.model.organization.QEmployee employee;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QProduct product;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public final StringPath username = createString("username");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QUser(Path<? extends User> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUser(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUser(PathMetadata<?> metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new kr.debop4j.access.model.organization.QCompany(forProperty("company")) : null;
        this.department = inits.isInitialized("department") ? new kr.debop4j.access.model.organization.QDepartment(forProperty("department"), inits.get("department")) : null;
        this.employee = inits.isInitialized("employee") ? new kr.debop4j.access.model.organization.QEmployee(forProperty("employee"), inits.get("employee")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

