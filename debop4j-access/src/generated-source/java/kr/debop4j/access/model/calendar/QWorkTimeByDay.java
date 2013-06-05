package kr.debop4j.access.model.calendar;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QWorkTimeByDay is a Querydsl query type for WorkTimeByDay
 */
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
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
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
        this.workCalendar = _super.workCalendar;
        this.workInMinute = _super.workInMinute;
        this.workTime = _super.workTime;
    }

}

