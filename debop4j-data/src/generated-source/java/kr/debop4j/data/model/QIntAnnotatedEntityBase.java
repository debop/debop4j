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
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QIntAnnotatedEntityBase is a Querydsl query type for IntAnnotatedEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QIntAnnotatedEntityBase extends EntityPathBase<IntAnnotatedEntityBase> {

    private static final long serialVersionUID = -2098397303;

    public static final QIntAnnotatedEntityBase intAnnotatedEntityBase = new QIntAnnotatedEntityBase("intAnnotatedEntityBase");

    public final QAnnotatedEntityBase _super = new QAnnotatedEntityBase(this);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QIntAnnotatedEntityBase(String variable) {
        super(IntAnnotatedEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QIntAnnotatedEntityBase(Path<? extends IntAnnotatedEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QIntAnnotatedEntityBase(PathMetadata<?> metadata) {
        super(IntAnnotatedEntityBase.class, metadata);
    }

}

