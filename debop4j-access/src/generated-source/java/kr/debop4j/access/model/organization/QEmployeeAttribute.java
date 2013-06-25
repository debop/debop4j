package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QEmployeeAttribute is a Querydsl query type for EmployeeAttribute */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QEmployeeAttribute extends EntityPathBase<EmployeeAttribute> {

    private static final long serialVersionUID = 1806893317;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployeeAttribute employeeAttribute = new QEmployeeAttribute("employeeAttribute");

    public final kr.debop4j.data.model.QAnnotatedEntityBase _super = new kr.debop4j.data.model.QAnnotatedEntityBase(this);

    public final QEmployee employee;

    public final ArrayPath<byte[], Byte> faceImage = createArray("faceImage", byte[].class);

    public final StringPath faceImageUrl = createString("faceImageUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final ArrayPath<byte[], Byte> signImage = createArray("signImage", byte[].class);

    public final StringPath signImageUrl = createString("signImageUrl");

    public QEmployeeAttribute(String variable) {
        this(EmployeeAttribute.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QEmployeeAttribute(Path<? extends EmployeeAttribute> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeAttribute(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeAttribute(PathMetadata<?> metadata, PathInits inits) {
        this(EmployeeAttribute.class, metadata, inits);
    }

    public QEmployeeAttribute(Class<? extends EmployeeAttribute> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.employee = inits.isInitialized("employee") ? new QEmployee(forProperty("employee"), inits.get("employee")) : null;
    }

}

