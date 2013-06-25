package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QCompany is a Querydsl query type for Company */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -1662923084;

    public static final QCompany company = new QCompany("company");

    public final kr.debop4j.access.model.QAccessLocaledEntityBase _super = new kr.debop4j.access.model.QAccessLocaledEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath code = createString("code");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final SetPath<Employee, QEmployee> employees = this.<Employee, QEmployee>createSet("employees", Employee.class, QEmployee.class, PathInits.DIRECT2);

    public final StringPath ename = createString("ename");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final MapPath<java.util.Locale, Company.CompanyLocale, QCompany_CompanyLocale> localeMap = this.<java.util.Locale, Company.CompanyLocale, QCompany_CompanyLocale>createMap("localeMap", java.util.Locale.class, Company.CompanyLocale.class, QCompany_CompanyLocale.class);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<java.sql.Timestamp> updatedTimestamp = createDateTime("updatedTimestamp", java.sql.Timestamp.class);

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updateTimestamp = _super.updateTimestamp;

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QCompany(Path<? extends Company> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata<?> metadata) {
        super(Company.class, metadata);
    }

}

