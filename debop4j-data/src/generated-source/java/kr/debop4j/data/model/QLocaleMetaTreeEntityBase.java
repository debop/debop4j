package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QLocaleMetaTreeEntityBase is a Querydsl query type for LocaleMetaTreeEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QLocaleMetaTreeEntityBase extends EntityPathBase<LocaleMetaTreeEntityBase<? extends IEntity<? extends java.io.Serializable>, ? extends java.io.Serializable, ? extends ILocaleValue>> {

    private static final long serialVersionUID = 1790151133;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLocaleMetaTreeEntityBase localeMetaTreeEntityBase = new QLocaleMetaTreeEntityBase("localeMetaTreeEntityBase");

    public final QLocaleMetaEntityBase _super = new QLocaleMetaEntityBase(this);

    public final SetPath<IEntity<? extends java.io.Serializable>, SimplePath<IEntity<? extends java.io.Serializable>>> children = this.<IEntity<? extends java.io.Serializable>, SimplePath<IEntity<? extends java.io.Serializable>>>createSet("children", IEntity.class, SimplePath.class, PathInits.DIRECT2);

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

    //inherited
    public final SetPath<String, StringPath> metaKeys = _super.metaKeys;

    //inherited
    public final MapPath<String, IMetaValue, SimplePath<IMetaValue>> metaMap = _super.metaMap;

    public final QTreeNodePosition nodePosition;

    public final SimplePath<IEntity<? extends java.io.Serializable>> parent = createSimple("parent", IEntity.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QLocaleMetaTreeEntityBase(String variable) {
        this((Class)LocaleMetaTreeEntityBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QLocaleMetaTreeEntityBase(Path<? extends LocaleMetaTreeEntityBase> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QLocaleMetaTreeEntityBase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    @SuppressWarnings("all")
    public QLocaleMetaTreeEntityBase(PathMetadata<?> metadata, PathInits inits) {
        this((Class)LocaleMetaTreeEntityBase.class, metadata, inits);
    }

    public QLocaleMetaTreeEntityBase(Class<? extends LocaleMetaTreeEntityBase<? extends IEntity<? extends java.io.Serializable>, ? extends java.io.Serializable, ? extends ILocaleValue>> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nodePosition = inits.isInitialized("nodePosition") ? new QTreeNodePosition(forProperty("nodePosition")) : null;
    }

}

