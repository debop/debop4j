package kr.debop4j.timeperiod;

import kr.debop4j.core.NotImplementedException;
import kr.debop4j.timeperiod.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 기간을 표현하는 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:05
 */
@Slf4j
public class TimeRange extends TimePeriodBase implements ITimeRange {

    private static final long serialVersionUID = -5665345604375538630L;

    public static final TimeRange AnyTime = new TimeRange(true);

    public static ITimeBlock toTimeBlock(ITimeRange range) {
        throw new NotImplementedException("구현 중");
    }

    public static ITimeInterval toTimeInterval(ITimeRange range) {
        throw new NotImplementedException("구현 중");
    }

    // region << Constructor >>

    public TimeRange() {}

    public TimeRange(boolean readonly) {
        super(readonly);
    }

    public TimeRange(DateTime start, DateTime end) {
        super(start, end);
    }

    public TimeRange(DateTime start, DateTime end, boolean readonly) {
        super(start, end, readonly);
    }

    public TimeRange(DateTime start, Duration duration) {
        super(start, duration);
    }

    public TimeRange(DateTime start, Duration duration, boolean readonly) {
        super(start, duration, readonly);
    }

    public TimeRange(ITimePeriod source) {
        super(source);
    }

    public TimeRange(ITimePeriod source, boolean readonly) {
        super(source, readonly);
    }

    // endregion

    @Override
    public void setStart(DateTime start) {
        assertMutable();
        this.start = start;
    }

    @Override
    public void setEnd(DateTime end) {
        assertMutable();
        this.end = end;
    }

    @Override
    public void expandStartTo(DateTime moment) {
        assertMutable();
        if (start.compareTo(moment) > 0)
            this.start = moment;
    }

    @Override
    public void expandEndTo(DateTime moment) {
        assertMutable();
        if (end.compareTo(moment) < 0)
            this.end = moment;
    }

    @Override
    public void expandTo(DateTime moment) {
        assertMutable();
        expandStartTo(moment);
        expandEndTo(moment);
    }

    @Override
    public void expandTo(ITimePeriod period) {
        assertMutable();

        if (period.hasStart())
            expandStartTo(period.getStart());
        if (period.hasEnd())
            expandEndTo(period.getEnd());
    }

    @Override
    public void shrinkStartTo(DateTime moment) {
        assertMutable();
        if (hasInside(moment) && start.compareTo(moment) < 0)
            start = moment;
    }

    @Override
    public void shrinkEndTo(DateTime moment) {
        assertMutable();
        if (hasInside(moment) && end.compareTo(moment) > 0)
            end = moment;
    }

    @Override
    public void shrinkTo(ITimePeriod period) {
        assertMutable();

        if (period.hasStart())
            shrinkStartTo(period.getStart());
        if (period.hasEnd())
            shrinkEndTo(period.getEnd());
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        assert other != null;
        return TimeTool.getIntersectionRange(this, other);
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        assert other != null;
        return TimeTool.getUnion(this, other);
    }
}
