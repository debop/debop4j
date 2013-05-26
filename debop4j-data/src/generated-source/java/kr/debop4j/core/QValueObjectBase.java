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

package kr.debop4j.core;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QValueObjectBase is a Querydsl query type for ValueObjectBase */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QValueObjectBase extends BeanPath<ValueObjectBase> {

    private static final long serialVersionUID = 1986431503;

    public static final QValueObjectBase valueObjectBase = new QValueObjectBase("valueObjectBase");

    public QValueObjectBase(String variable) {
        super(ValueObjectBase.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QValueObjectBase(Path<? extends ValueObjectBase> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QValueObjectBase(PathMetadata<?> metadata) {
        super(ValueObjectBase.class, metadata);
    }

}

