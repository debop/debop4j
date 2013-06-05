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

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QAnnotatedEntityBase is a Querydsl query type for AnnotatedEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAnnotatedEntityBase extends EntityPathBase<AnnotatedEntityBase> {

    private static final long serialVersionUID = 2041099168;

    public static final QAnnotatedEntityBase annotatedEntityBase = new QAnnotatedEntityBase("annotatedEntityBase");

    public final QStatefulEntityBase _super = new QStatefulEntityBase(this);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QAnnotatedEntityBase(String variable) {
        super(AnnotatedEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAnnotatedEntityBase(Path<? extends AnnotatedEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QAnnotatedEntityBase(PathMetadata<?> metadata) {
        super(AnnotatedEntityBase.class, metadata);
    }

}

