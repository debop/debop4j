package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QUserActivityLog is a Querydsl query type for UserActivityLog */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUserActivityLog extends EntityPathBase<UserActivityLog> {

    private static final long serialVersionUID = 94510201;

    public static final QUserActivityLog userActivityLog = new QUserActivityLog("userActivityLog");

    public final kr.debop4j.data.model.QAnnotatedEntityBase _super = new kr.debop4j.data.model.QAnnotatedEntityBase(this);

    public final StringPath activityKind = createString("activityKind");

    public final DateTimePath<java.util.Date> activityTime = createDateTime("activityTime", java.util.Date.class);

    public final StringPath companyCode = createString("companyCode");

    public final StringPath companyName = createString("companyName");

    public final StringPath departmentCode = createString("departmentCode");

    public final StringPath departmentName = createString("departmentName");

    public final StringPath employeeCode = createString("employeeCode");

    public final StringPath employeeName = createString("employeeName");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SimplePath<java.util.Locale> locale = createSimple("locale", java.util.Locale.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final StringPath productCode = createString("productCode");

    public final StringPath productName = createString("productName");

    public final StringPath username = createString("username");

    public QUserActivityLog(String variable) {
        super(UserActivityLog.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QUserActivityLog(Path<? extends UserActivityLog> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QUserActivityLog(PathMetadata<?> metadata) {
        super(UserActivityLog.class, metadata);
    }

}

