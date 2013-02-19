package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * kr.nsoft.data.domain.kr.debop4j.data.mapping.model.DateTimeRange
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 22.
 */
@Getter
@Setter
public class DateTimeRange extends ValueObjectBase {

    public static final long ZeroMillis = 0L;
    public static final long MinMillis = 0L;
    public static final long MillisPerDay = 24 * 60 * 60 * 1000;
    public static final long MaxMillis = 3652059 * MillisPerDay - 1;

    public static final DateTime MinPeriodTime = new DateTime(MinMillis);
    public static final DateTime MaxPeriodTime = new DateTime(MaxMillis);

    private DateTime start;
    private DateTime end;
    private boolean readonly;

    public DateTimeRange() {
        this(MinPeriodTime, MaxPeriodTime, false);
    }

    public DateTimeRange(DateTime start, DateTime end) {
        this(start, end, false);
    }

    public DateTimeRange(DateTime start, DateTime end, boolean readonly) {
        this.start = (start != null) ? start : MinPeriodTime;
        this.end = (end != null) ? end : MaxPeriodTime;
        this.readonly = readonly;
    }

    public DateTimeRange(DateTimeRange source) {
        this.start = source.start;
        this.end = source.end;
        this.readonly = source.readonly;
    }

    public DateTimeRange(DateTimeRange source, boolean readonly) {
        this.start = source.start;
        this.end = source.end;
        this.readonly = readonly;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(start, end, readonly);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("start", start)
                .add("end", end)
                .add("readonly", readonly);
    }
}
