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


/** QLocaleMetaEntityBase is a Querydsl query type for LocaleMetaEntityBase */
@Generated( "com.mysema.query.codegen.SupertypeSerializer" )
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

    @SuppressWarnings( "all" )
    public QLocaleMetaEntityBase(String variable) {
        super((Class) LocaleMetaEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings( "all" )
    public QLocaleMetaEntityBase(Path<? extends LocaleMetaEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings( "all" )
    public QLocaleMetaEntityBase(PathMetadata<?> metadata) {
        super((Class) LocaleMetaEntityBase.class, metadata);
    }

}

