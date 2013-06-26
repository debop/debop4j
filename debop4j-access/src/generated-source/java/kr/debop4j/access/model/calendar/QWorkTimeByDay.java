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


/** QWorkTimeByDay is a Querydsl query type for WorkTimeByDay */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QWorkTimeByDay extends EntityPathBase<WorkTimeByDay> {

    private static final long serialVersionUID = 924205481;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkTimeByDay workTimeByDay = new QWorkTimeByDay("workTimeByDay");

    public final QWorkTimeByTimeBase _super;

    //inherited
    public final NumberPath<Long> cumulatedInMinute;

    public final NumberPath<Integer> dayOfWeek = createNumber("dayOfWeek", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath isWorking;

    //inherited
    public final BooleanPath persisted;

    //inherited
    public final DateTimePath<org.joda.time.DateTime> updatedTime;

    // inherited
    public final QWorkCalendar workCalendar;

    //inherited
    public final NumberPath<Integer> workInMinute;

    //inherited
    public final DateTimePath<java.util.Date> workTime;

    public QWorkTimeByDay(String variable) {
        this(WorkTimeByDay.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QWorkTimeByDay(Path<? extends WorkTimeByDay> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkTimeByDay(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkTimeByDay(PathMetadata<?> metadata, PathInits inits) {
        this(WorkTimeByDay.class, metadata, inits);
    }

    public QWorkTimeByDay(Class<? extends WorkTimeByDay> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QWorkTimeByTimeBase(type, metadata, inits);
        this.cumulatedInMinute = _super.cumulatedInMinute;
        this.isWorking = _super.isWorking;
        this.persisted = _super.persisted;
        this.updatedTime = _super.updatedTime;
        this.workCalendar = _super.workCalendar;
        this.workInMinute = _super.workInMinute;
        this.workTime = _super.workTime;
    }

}

