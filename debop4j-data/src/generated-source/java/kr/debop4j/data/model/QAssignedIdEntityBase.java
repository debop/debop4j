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


/** QAssignedIdEntityBase is a Querydsl query type for AssignedIdEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAssignedIdEntityBase extends EntityPathBase<AssignedIdEntityBase<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1839890601;

    public static final QAssignedIdEntityBase assignedIdEntityBase = new QAssignedIdEntityBase("assignedIdEntityBase");

    public final QEntityBase _super = new QEntityBase(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QAssignedIdEntityBase(String variable) {
        super((Class) AssignedIdEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAssignedIdEntityBase(Path<? extends AssignedIdEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QAssignedIdEntityBase(PathMetadata<?> metadata) {
        super((Class) AssignedIdEntityBase.class, metadata);
    }

}

