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


/** QProductCodeItem is a Querydsl query type for ProductCodeItem */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductCodeItem extends EntityPathBase<ProductCodeItem> {

    private static final long serialVersionUID = -1624398786;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductCodeItem productCodeItem = new QProductCodeItem("productCodeItem");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final QProductCode code;

    public final StringPath descripton = createString("descripton");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final StringPath value = createString("value");

    public QProductCodeItem(String variable) {
        this(ProductCodeItem.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QProductCodeItem(Path<? extends ProductCodeItem> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductCodeItem(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QProductCodeItem(PathMetadata<?> metadata, PathInits inits) {
        this(ProductCodeItem.class, metadata, inits);
    }

    public QProductCodeItem(Class<? extends ProductCodeItem> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.code = inits.isInitialized("code") ? new QProductCode(forProperty("code"), inits.get("code")) : null;
    }

}

