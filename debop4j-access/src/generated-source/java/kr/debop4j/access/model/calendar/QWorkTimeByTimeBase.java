package kr.debop4j.access.model.calendar;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QWorkTimeByTimeBase is a Querydsl query type for WorkTimeByTimeBase
 */
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

    public final QWorkCalendar workCalendar;

    public final NumberPath<Integer> workInMinute = createNumber("workInMinute", Integer.class);

    public final DateTimePath<java.util.Date> workTime = createDateTime("workTime", java.util.Date.class);

    public QWorkTimeByTimeBase(String variable) {
        this(WorkTimeByTimeBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QWorkTimeByTimeBase(Path<? extends WorkTimeByTimeBase> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
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

