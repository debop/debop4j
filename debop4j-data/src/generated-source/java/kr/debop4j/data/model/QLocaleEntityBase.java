package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QLocaleEntityBase is a Querydsl query type for LocaleEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QLocaleEntityBase extends EntityPathBase<LocaleEntityBase<? extends java.io.Serializable, ? extends ILocaleValue>> {

    private static final long serialVersionUID = -679839686;

    public static final QLocaleEntityBase localeEntityBase = new QLocaleEntityBase("localeEntityBase");

    public final QEntityBase _super = new QEntityBase(this);

    public final SimplePath<ILocaleValue> currentLocaleValue = createSimple("currentLocaleValue", ILocaleValue.class);

    public final SimplePath<ILocaleValue> defaultLocale = createSimple("defaultLocale", ILocaleValue.class);

    public final SimplePath<ILocaleValue> defaultLocaleValue = createSimple("defaultLocaleValue", ILocaleValue.class);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    public final MapPath<java.util.Locale, ILocaleValue, SimplePath<ILocaleValue>> localeMap = this.<java.util.Locale, ILocaleValue, SimplePath<ILocaleValue>>createMap("localeMap", java.util.Locale.class, ILocaleValue.class, SimplePath.class);

    public final SetPath<java.util.Locale, SimplePath<java.util.Locale>> locales = this.<java.util.Locale, SimplePath<java.util.Locale>>createSet("locales", java.util.Locale.class, SimplePath.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QLocaleEntityBase(String variable) {
        super((Class)LocaleEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QLocaleEntityBase(Path<? extends LocaleEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QLocaleEntityBase(PathMetadata<?> metadata) {
        super((Class)LocaleEntityBase.class, metadata);
    }

}

