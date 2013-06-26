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


/** QWorkTimeByTimeBase is a Querydsl query type for WorkTimeByTimeBase */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QWorkTimeByTimeBase extends EntityPathBase<WorkTimeByTimeBase> {

    private static final long serialVersionUID = -393830063;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkTimeByTimeBase workTimeByTimeBase = new QWorkTimeByTimeBase("workTimeByTimeBase");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final NumberPath<Long> cumulatedInMinute = createNumber("cumulatedInMinute", Long.class);

    public final BooleanPath isWorking = createBoolean("isWorking");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime = _super.updatedTime;

    public final QWorkCalendar workCalendar;

    public final NumberPath<Integer> workInMinute = createNumber("workInMinute", Integer.class);

    public final DateTimePath<java.util.Date> workTime = createDateTime("workTime", java.util.Date.class);

    public QWorkTimeByTimeBase(String variable) {
        this(WorkTimeByTimeBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QWorkTimeByTimeBase(Path<? extends WorkTimeByTimeBase> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkTimeByTimeBase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkTimeByTimeBase(PathMetadata<?> metadata, PathInits inits) {
        this(WorkTimeByTimeBase.class, metadata, inits);
    }

    public QWorkTimeByTimeBase(Class<? extends WorkTimeByTimeBase> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workCalendar = inits.isInitialized("workCalendar") ? new QWorkCalendar(forProperty("workCalendar"), inits.get("workCalendar")) : null;
    }

}

