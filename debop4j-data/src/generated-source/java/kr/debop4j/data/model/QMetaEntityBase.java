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


/** QMetaEntityBase is a Querydsl query type for MetaEntityBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QMetaEntityBase extends EntityPathBase<MetaEntityBase<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1556181285;

    public static final QMetaEntityBase metaEntityBase = new QMetaEntityBase("metaEntityBase");

    public final QEntityBase _super = new QEntityBase(this);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    public final SetPath<String, StringPath> metaKeys = this.<String, StringPath>createSet("metaKeys", String.class, StringPath.class, PathInits.DIRECT2);

    public final MapPath<String, IMetaValue, SimplePath<IMetaValue>> metaMap = this.<String, IMetaValue, SimplePath<IMetaValue>>createMap("metaMap", String.class, IMetaValue.class, SimplePath.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QMetaEntityBase(String variable) {
        super((Class) MetaEntityBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QMetaEntityBase(Path<? extends MetaEntityBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QMetaEntityBase(PathMetadata<?> metadata) {
        super((Class) MetaEntityBase.class, metadata);
    }

}

