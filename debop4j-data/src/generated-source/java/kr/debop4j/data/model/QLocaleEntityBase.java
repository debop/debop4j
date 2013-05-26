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


/** QLocaleEntityBase is a Querydsl query type for LocaleEntityBase */
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
        super((Class) LocaleEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QLocaleEntityBase(Path<? extends LocaleEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QLocaleEntityBase(PathMetadata<?> metadata) {
        super((Class) LocaleEntityBase.class, metadata);
    }

}

