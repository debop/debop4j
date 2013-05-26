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

package kr.debop4j.access.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QPreferenceBase is a Querydsl query type for PreferenceBase */
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
        super((Class) path.getType(), path.getMetadata());
    }

    public QPreferenceBase(PathMetadata<?> metadata) {
        super(PreferenceBase.class, metadata);
    }

}

