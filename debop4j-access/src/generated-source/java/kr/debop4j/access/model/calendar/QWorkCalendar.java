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

package kr.debop4j.access.model.calendar;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QWorkCalendar is a Querydsl query type for WorkCalendar */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QWorkCalendar extends EntityPathBase<WorkCalendar> {

    private static final long serialVersionUID = 1790828653;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkCalendar workCalendar = new QWorkCalendar("workCalendar");

    public final kr.debop4j.data.model.QAnnotatedTreeEntityBase _super;

    public final SetPath<WorkCalendar, QWorkCalendar> children = this.<WorkCalendar, QWorkCalendar>createSet("children", WorkCalendar.class, QWorkCalendar.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final kr.debop4j.access.model.organization.QCompany company;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    // inherited
    public final kr.debop4j.data.model.QTreeNodePosition nodePosition;

    public final QWorkCalendar parent;

    //inherited
    public final BooleanPath persisted;

    public QWorkCalendar(String variable) {
        this(WorkCalendar.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QWorkCalendar(Path<? extends WorkCalendar> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkCalendar(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkCalendar(PathMetadata<?> metadata, PathInits inits) {
        this(WorkCalendar.class, metadata, inits);
    }

    public QWorkCalendar(Class<? extends WorkCalendar> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new kr.debop4j.data.model.QAnnotatedTreeEntityBase(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new kr.debop4j.access.model.organization.QCompany(forProperty("company")) : null;
        this.nodePosition = _super.nodePosition;
        this.parent = inits.isInitialized("parent") ? new QWorkCalendar(forProperty("parent"), inits.get("parent")) : null;
        this.persisted = _super.persisted;
    }

}

