/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QLocaleMetaTreeEntityBase is a Querydsl query type for LocaleMetaTreeEntityBase */
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
        this((Class) LocaleMetaTreeEntityBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QLocaleMetaTreeEntityBase(Path<? extends LocaleMetaTreeEntityBase> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QLocaleMetaTreeEntityBase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    @SuppressWarnings("all")
    public QLocaleMetaTreeEntityBase(PathMetadata<?> metadata, PathInits inits) {
        this((Class) LocaleMetaTreeEntityBase.class, metadata, inits);
    }

    public QLocaleMetaTreeEntityBase(Class<? extends LocaleMetaTreeEntityBase<? extends IEntity<? extends java.io.Serializable>, ? extends java.io.Serializable, ? extends ILocaleValue>> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nodePosition = inits.isInitialized("nodePosition") ? new QTreeNodePosition(forProperty("nodePosition")) : null;
    }

}

