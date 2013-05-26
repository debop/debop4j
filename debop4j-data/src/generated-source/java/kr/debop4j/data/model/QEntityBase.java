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
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.SimplePath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QEntityBase is a Querydsl query type for EntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QEntityBase extends EntityPathBase<EntityBase<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 2011387712;

    public static final QEntityBase entityBase = new QEntityBase("entityBase");

    public final QStatefulEntityBase _super = new QStatefulEntityBase(this);

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QEntityBase(String variable) {
        super((Class) EntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QEntityBase(Path<? extends EntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QEntityBase(PathMetadata<?> metadata) {
        super((Class) EntityBase.class, metadata);
    }

}

