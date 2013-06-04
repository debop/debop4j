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

package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QProductPreference is a Querydsl query type for ProductPreference */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QProductPreference extends EntityPathBase<ProductPreference> {

    private static final long serialVersionUID = 2068878073;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductPreference productPreference = new QProductPreference("productPreference");

    public final kr.debop4j.access.model.QPreferenceBase _super = new kr.debop4j.access.model.QPreferenceBase(this);

    //inherited
    public final StringPath defaultValue = _super.defaultValue;

    //inherited
    public final StringPath description = _super.description;

    //inherited
    public final StringPath exAttr = _super.exAttr;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath key = _super.key;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QProduct product;

    //inherited
    public final StringPath value = _super.value;

    public QProductPreference(String variable) {
        this(ProductPreference.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QProductPreference(Path<? extends ProductPreference> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductPreference(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductPreference(PathMetadata<?> metadata, PathInits inits) {
        this(ProductPreference.class, metadata, inits);
    }

    public QProductPreference(Class<? extends ProductPreference> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

