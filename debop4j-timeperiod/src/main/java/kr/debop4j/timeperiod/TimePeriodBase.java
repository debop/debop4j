package kr.debop4j.timeperiod;

import kr.debop4j.core.Guard;
import kr.debop4j.core.NotImplementedException;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.TimeTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;

import java.util.Objects;

/**
 * 기간을 표현하는 기본 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:12
 */
@Slf4j
public class TimePeriodBase implements ITimePeriod {

    private static final long serialVersionUID = -7255762434105570062L;

    // region << Constructors >>

    protected TimePeriodBase() {
        this((DateTime) null, (DateTime) null, false);
    }

    protected TimePeriodBase(boolean readonly) {
        this((DateTime) null, (DateTime) null, readonly);
    }

    protected TimePeriodBase(DateTime moment) {
        this(moment, moment, false);
    }

    protected TimePeriodBase(DateTime moment, boolean readonly) {
        this(moment, moment, readonly);
    }

    protected TimePeriodBase(DateTime start, DateTime end) {
        this(start, end, false);
    }

    protected TimePeriodBase(DateTime start, DateTime end, boolean readonly) {
        this.start = Guard.firstNotNull(start, TimeSpec.MinPeriodTime);
        this.end = Guard.firstNotNull(end, TimeSpec.MaxPeriodTime);
        this.readonly = readonly;
    }

    protected TimePeriodBase(DateTime start, Duration duration) {
        this(start, duration, false);
    }

    protected TimePeriodBase(DateTime start, Duration duration, boolean readonly) {
        // TODO: TimeTool.adjustPeriod() 메소드를 사용해야한다.
        // TimeTool.adjustPeriod(start, duration)
        this.start = start;
        setDuration(duration);
        this.readonly = readonly;
    }

    protected TimePeriodBase(ITimePeriod source) {
        Guard.shouldNotBeNull(source, "source");

        this.start = source.getStart();
        this.end = source.getEnd();
        this.readonly = source.isReadonly();
    }

    protected TimePeriodBase(ITimePeriod source, boolean readonly) {
        this.readonly = readonly;
    }

    // endregion

    @Getter
    protected DateTime start;

    @Getter
    protected DateTime end;

    @Getter
    @Setter( AccessLevel.PROTECTED )
    private boolean readonly;

    /** 기간을 TimeSpan으료 표현, 기간이 정해지지 않았다면 <see cref="TimeSpec.MaxPeriodTime"/> 을 반환합니다. */
    @Override
    public Duration getDuration() {
        return Duration.millis(end.getMillis() - start.getMillis());
    }

    public void setDuration(Duration duration) {
        assert duration.getMillis() >= Duration.ZERO.getMillis() : "Duration의 크기가 0보다 크거나 같아야 합니다.";
        if (hasStart())
            end = start.plus(duration);
    }

    @Override
    public String getDurationDescription() {
        // TODO: ITimeFormatter 제작하여 구현해야 함.
        // ITimeFormatter.Instance.GetDuration(Duration, DurationFormatKind.Detailed);
        throw new NotImplementedException("구현 중");
    }

    @Override
    public boolean hasStart() {
        return start != TimeSpec.MinPeriodTime;
    }

    @Override
    public boolean hasEnd() {
        return end != TimeSpec.MaxPeriodTime;
    }

    @Override
    public boolean hasPeriod() {
        return hasStart() && hasEnd();
    }

    @Override
    public boolean isMoment() {
        return Objects.equals(start, end);
    }

    @Override
    public boolean isAnytime() {
        return !hasStart() && !hasEnd();
    }

    @Override
    public void setup(DateTime newStart, DateTime newEnd) {
        if (TimePeriodBase.log.isTraceEnabled())
            TimePeriodBase.log.trace("기간을 새로 설정합니다. newStart=[{}], newEnd=[{}]", newStart, newEnd);
        this.start = newStart;
        this.end = newEnd;
    }

    public ITimePeriod copy() {
        return copy(Duration.ZERO);
    }

    @Override
    public ITimePeriod copy(Duration offset) {
        if (TimePeriodBase.log.isTraceEnabled())
            TimePeriodBase.log.trace("기간 [{}]에 offset[{}]을 준 기간을 반환합니다...", this, offset);

        if (offset == Duration.ZERO)
            return new TimeRange(this);

        return new TimeRange(hasStart() ? start.plus(offset.getMillis()) : start,
                             hasEnd() ? end.plus(offset.getMillis()) : end,
                             readonly);
    }

    @Override
    public void move(Duration offset) {
        if (offset == Duration.ZERO)
            return;
        assertMutable();

        if (TimePeriodBase.log.isTraceEnabled()) TimePeriodBase.log.trace("기간[{}]을 offset[{}]만큼 이동합니다.", this, offset);

        if (hasStart())
            start = start.plus(offset.getMillis());
        if (hasEnd())
            end = end.plus(offset.getMillis());
    }

    @Override
    public boolean isSamePeriod(ITimePeriod other) {
        return (other != null) &&
                Objects.equals(start, other.getStart()) &&
                Objects.equals(end, other.getEnd());
    }

    @Override
    public boolean hasInside(DateTime moment) {
        return TimeTool.hasInside(this, moment);
    }

    @Override
    public boolean hasInside(ITimePeriod other) {
        return TimeTool.hasInside(this, other);
    }

    @Override
    public boolean intersectsWith(ITimePeriod other) {
        return TimeTool.intersectsWith(this, other);
    }

    @Override
    public boolean overlapsWith(ITimePeriod other) {
        return TimeTool.overlapsWith(this, other);
    }

    @Override
    public void reset() {
        assertMutable();
        start = TimeSpec.MinPeriodTime;
        end = TimeSpec.MaxPeriodTime;
        if (TimePeriodBase.log.isTraceEnabled())
            TimePeriodBase.log.trace("기간을 리셋했습니다. start=[{}], end=[{}]", start, end);
    }

    @Override
    public PeriodRelation getRelation(ITimePeriod other) {
        return TimeTool.getRelation(this, other);
    }

    @Override
    public String getDescription(DateTimeFormatter formatter) {
        return format(Guard.firstNotNull(formatter, ITimeFormatter.getInstance()));
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        return TimeTool.getIntersectionRange(this, other);
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        return TimeTool.getUnionRange(this, other);
    }

    @Override
    public int compareTo(ITimePeriod o) {
        return start.compareTo(o.getStart());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) &&
                (obj instanceof ITimePeriod) &&
                (hashCode() == obj.hashCode());
    }

    @Override
    public int hashCode() {
        return HashTool.compute(start, end, readonly);
    }

    @Override
    public String toString() {
        return getClass().getName() + "# " + getDescription(null);
    }

    protected final void assertMutable() {
        assert !readonly : "readonly 입니다.";
    }
}
