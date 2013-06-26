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


/** QDepartment is a Querydsl query type for Department */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDepartment extends EntityPathBase<Department> {

    private static final long serialVersionUID = -292851269;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepartment department = new QDepartment("department");

    public final kr.debop4j.data.model.QAnnotatedEntityBase _super = new kr.debop4j.data.model.QAnnotatedEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final SetPath<Department, QDepartment> children = this.<Department, QDepartment>createSet("children", Department.class, QDepartment.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final QCompany company;

    public final StringPath description = createString("description");

    public final StringPath enam = createString("enam");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<DepartmentMember, QDepartmentMember> members = this.<DepartmentMember, QDepartmentMember>createSet("members", DepartmentMember.class, QDepartmentMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final kr.debop4j.data.model.QTreeNodePosition nodePosition;

    public final QDepartment parent;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final DateTimePath<org.joda.time.DateTime> updateTimestamp = createDateTime("updateTimestamp", org.joda.time.DateTime.class);

    public QDepartment(String variable) {
        this(Department.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QDepartment(Path<? extends Department> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartment(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDepartment(PathMetadata<?> metadata, PathInits inits) {
        this(Department.class, metadata, inits);
    }

    public QDepartment(Class<? extends Department> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.nodePosition = inits.isInitialized("nodePosition") ? new kr.debop4j.data.model.QTreeNodePosition(forProperty("nodePosition")) : null;
        this.parent = inits.isInitialized("parent") ? new QDepartment(forProperty("parent"), inits.get("parent")) : null;
    }

}

