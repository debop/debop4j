package kr.debop4j.access.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPreferenceBase is a Querydsl query type for PreferenceBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QPreferenceBase extends EntityPathBase<PreferenceBase> {

    private static final long serialVersionUID = 725283838;

    public static final QPreferenceBase preferenceBase = new QPreferenceBase("preferenceBase");

    public final QAccessEntityBase _super = new QAccessEntityBase(this);

    public final StringPath defaultValue = createString("defaultValue");

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final StringPath key = createString("key");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final StringPath value = createString("value");

    public QPreferenceBase(String variable) {
        super(PreferenceBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QPreferenceBase(Path<? extends PreferenceBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QPreferenceBase(PathMetadata<?> metadata) {
        super(PreferenceBase.class, metadata);
    }

}

