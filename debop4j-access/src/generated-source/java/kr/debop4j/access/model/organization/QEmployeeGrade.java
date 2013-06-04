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


/** QEmployeeGrade is a Querydsl query type for EmployeeGrade */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QEmployeeGrade extends EntityPathBase<EmployeeGrade> {

    private static final long serialVersionUID = 29471424;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployeeGrade employeeGrade = new QEmployeeGrade("employeeGrade");

    public final QEmployeeCodeBase _super;

    //inherited
    public final StringPath code;

    // inherited
    public final QCompany company;

    //inherited
    public final StringPath description;

    //inherited
    public final StringPath exAttr;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath name;

    public final StringPath parentCode = createString("parentCode");

    //inherited
    public final BooleanPath persisted;

    //inherited
    public final NumberPath<Integer> viewOrder;

    public QEmployeeGrade(String variable) {
        this(EmployeeGrade.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QEmployeeGrade(Path<? extends EmployeeGrade> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeGrade(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeGrade(PathMetadata<?> metadata, PathInits inits) {
        this(EmployeeGrade.class, metadata, inits);
    }

    public QEmployeeGrade(Class<? extends EmployeeGrade> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QEmployeeCodeBase(type, metadata, inits);
        this.code = _super.code;
        this.company = _super.company;
        this.description = _super.description;
        this.exAttr = _super.exAttr;
        this.name = _super.name;
        this.persisted = _super.persisted;
        this.viewOrder = _super.viewOrder;
    }

}

