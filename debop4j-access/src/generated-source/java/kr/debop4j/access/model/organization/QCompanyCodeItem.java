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

package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QCompanyCodeItem is a Querydsl query type for CompanyCodeItem */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QCompanyCodeItem extends EntityPathBase<CompanyCodeItem> {

    private static final long serialVersionUID = 1298865524;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyCodeItem companyCodeItem = new QCompanyCodeItem("companyCodeItem");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final QCompanyCode code;

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public final StringPath value = createString("value");

    public QCompanyCodeItem(String variable) {
        this(CompanyCodeItem.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QCompanyCodeItem(Path<? extends CompanyCodeItem> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyCodeItem(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyCodeItem(PathMetadata<?> metadata, PathInits inits) {
        this(CompanyCodeItem.class, metadata, inits);
    }

    public QCompanyCodeItem(Class<? extends CompanyCodeItem> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.code = inits.isInitialized("code") ? new QCompanyCode(forProperty("code"), inits.get("code")) : null;
    }

}

