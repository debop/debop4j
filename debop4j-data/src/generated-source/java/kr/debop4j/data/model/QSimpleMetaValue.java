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
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QSimpleMetaValue is a Querydsl query type for SimpleMetaValue */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QSimpleMetaValue extends BeanPath<SimpleMetaValue> {

    private static final long serialVersionUID = -2041450898;

    public static final QSimpleMetaValue simpleMetaValue = new QSimpleMetaValue("simpleMetaValue");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final StringPath label = createString("label");

    public final StringPath value = createString("value");

    public QSimpleMetaValue(String variable) {
        super(SimpleMetaValue.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QSimpleMetaValue(Path<? extends SimpleMetaValue> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QSimpleMetaValue(PathMetadata<?> metadata) {
        super(SimpleMetaValue.class, metadata);
    }

}

