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


/** QGroupMember is a Querydsl query type for GroupMember */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QGroupMember extends EntityPathBase<GroupMember> {

    private static final long serialVersionUID = -438832976;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupMember groupMember = new QGroupMember("groupMember");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath exAttr = createString("exAttr");

    public final QGroup group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final EnumPath<OrganizationKind> memberKind = createEnum("memberKind", OrganizationKind.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public QGroupMember(String variable) {
        this(GroupMember.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QGroupMember(Path<? extends GroupMember> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGroupMember(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGroupMember(PathMetadata<?> metadata, PathInits inits) {
        this(GroupMember.class, metadata, inits);
    }

    public QGroupMember(Class<? extends GroupMember> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroup(forProperty("group"), inits.get("group")) : null;
    }

}

