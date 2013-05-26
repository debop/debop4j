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


/** QLongAnnotatedEntityBase is a Querydsl query type for LongAnnotatedEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QLongAnnotatedEntityBase extends EntityPathBase<LongAnnotatedEntityBase> {

    private static final long serialVersionUID = -108666492;

    public static final QLongAnnotatedEntityBase longAnnotatedEntityBase = new QLongAnnotatedEntityBase("longAnnotatedEntityBase");

    public final QAnnotatedEntityBase _super = new QAnnotatedEntityBase(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QLongAnnotatedEntityBase(String variable) {
        super(LongAnnotatedEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QLongAnnotatedEntityBase(Path<? extends LongAnnotatedEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QLongAnnotatedEntityBase(PathMetadata<?> metadata) {
        super(LongAnnotatedEntityBase.class, metadata);
    }

}

