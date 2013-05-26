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


/** QDepartmentMember is a Querydsl query type for DepartmentMember */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDepartmentMember extends EntityPathBase<DepartmentMember> {

    private static final long serialVersionUID = 1741123637;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepartmentMember departmentMember = new QDepartmentMember("departmentMember");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final QDepartment department;

    public final QEmployee employee;

    public final QEmployeeTitle empTitle;

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<java.util.Date> startTime = createDateTime("startTime", java.util.Date.class);

    public QDepartmentMember(String variable) {
        this(DepartmentMember.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QDepartmentMember(Path<? extends DepartmentMember> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartmentMember(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartmentMember(PathMetadata<?> metadata, PathInits inits) {
        this(DepartmentMember.class, metadata, inits);
    }

    public QDepartmentMember(Class<? extends DepartmentMember> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.department = inits.isInitialized("department") ? new QDepartment(forProperty("department"), inits.get("department")) : null;
        this.employee = inits.isInitialized("employee") ? new QEmployee(forProperty("employee"), inits.get("employee")) : null;
        this.empTitle = inits.isInitialized("empTitle") ? new QEmployeeTitle(forProperty("empTitle"), inits.get("empTitle")) : null;
    }

}

