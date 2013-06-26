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
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QCodeBaseEntity is a Querydsl query type for CodeBaseEntity */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QCodeBaseEntity extends EntityPathBase<CodeBaseEntity> {

    private static final long serialVersionUID = -1411441261;

    public static final QCodeBaseEntity codeBaseEntity = new QCodeBaseEntity("codeBaseEntity");

    public final kr.debop4j.data.model.QLongAnnotatedEntityBase _super = new kr.debop4j.data.model.QLongAnnotatedEntityBase(this);

    public final StringPath code = createString("code");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public QCodeBaseEntity(String variable) {
        super(CodeBaseEntity.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QCodeBaseEntity(Path<? extends CodeBaseEntity> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QCodeBaseEntity(PathMetadata<?> metadata) {
        super(CodeBaseEntity.class, metadata);
    }

}

