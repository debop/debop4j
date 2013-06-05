package kr.debop4j.access.model.calendar;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QWorkCalendarRule is a Querydsl query type for WorkCalendarRule
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QWorkCalendarRule extends EntityPathBase<WorkCalendarRule> {

    private static final long serialVersionUID = 519367945;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkCalendarRule workCalendarRule = new QWorkCalendarRule("workCalendarRule");

    public final kr.debop4j.access.model.QAccessEntityBase _super = new kr.debop4j.access.model.QAccessEntityBase(this);

    public final NumberPath<Integer> dayOrException = createNumber("dayOrException", Integer.class);

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final StringPath exceptionClassName = createString("exceptionClassName");

    public final NumberPath<Integer> exceptionType = createNumber("exceptionType", Integer.class);

    public final StringPath exeptionPattern = createString("exeptionPattern");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final SimplePath<kr.debop4j.timeperiod.ITimePeriod> rulePeriod = createSimple("rulePeriod", kr.debop4j.timeperiod.ITimePeriod.class);

    public final SetPath<kr.debop4j.timeperiod.ITimePeriod, SimplePath<kr.debop4j.timeperiod.ITimePeriod>> rulePeriods = this.<kr.debop4j.timeperiod.ITimePeriod, SimplePath<kr.debop4j.timeperiod.ITimePeriod>>createSet("rulePeriods", kr.debop4j.timeperiod.ITimePeriod.class, SimplePath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> viewOrder = createNumber("viewOrder", Integer.class);

    public final QWorkCalendar workCalendar;

    public QWorkCalendarRule(String variable) {
        this(WorkCalendarRule.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QWorkCalendarRule(Path<? extends WorkCalendarRule> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkCalendarRule(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QWorkCalendarRule(PathMetadata<?> metadata, PathInits inits) {
        this(WorkCalendarRule.class, metadata, inits);
    }

    public QWorkCalendarRule(Class<? extends WorkCalendarRule> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.workCalendar = inits.isInitialized("workCalendar") ? new QWorkCalendar(forProperty("workCalendar"), inits.get("workCalendar")) : null;
    }

}

