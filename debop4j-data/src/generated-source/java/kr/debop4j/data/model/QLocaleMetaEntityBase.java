package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QLocaleMetaEntityBase is a Querydsl query type for LocaleMetaEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QLocaleMetaEntityBase extends EntityPathBase<LocaleMetaEntityBase<? extends java.io.Serializable, ? extends ILocaleValue>> {

    private static final long serialVersionUID = -376732385;

    public static final QLocaleMetaEntityBase localeMetaEntityBase = new QLocaleMetaEntityBase("localeMetaEntityBase");

    public final QLocaleEntityBase _super = new QLocaleEntityBase(this);

    //inherited
    public final SimplePath<ILocaleValue> currentLocaleValue = _super.currentLocaleValue;

    //inherited
    public final SimplePath<ILocaleValue> defaultLocale = _super.defaultLocale;

    //inherited
    public final SimplePath<ILocaleValue> defaultLocaleValue = _super.defaultLocaleValue;

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final MapPath<java.util.Locale, ILocaleValue, SimplePath<ILocaleValue>> localeMap = _super.localeMap;

    //inherited
    public final SetPath<java.util.Locale, SimplePath<java.util.Locale>> locales = _super.locales;

    public final SetPath<String, StringPath> metaKeys = this.<String, StringPath>createSet("metaKeys", String.class, StringPath.class, PathInits.DIRECT2);

    public final MapPath<String, IMetaValue, SimplePath<IMetaValue>> metaMap = this.<String, IMetaValue, SimplePath<IMetaValue>>createMap("metaMap", String.class, IMetaValue.class, SimplePath.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QLocaleMetaEntityBase(String variable) {
        super((Class)LocaleMetaEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QLocaleMetaEntityBase(Path<? extends LocaleMetaEntityBase> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QLocaleMetaEntityBase(PathMetadata<?> metadata) {
        super((Class)LocaleMetaEntityBase.class, metadata);
    }

}

