package kr.debop4j.core.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * debop4j.timeperiod.TimePeriodBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 11.
 */
@Getter
@Setter
public abstract class TimePeriodBase extends ValueObjectBase implements ITimePeriod {

    private static final long serialVersionUID = -2693028430637125747L;

    protected TimePeriodBase() {}

    public TimePeriodBase(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }


    private DateTime start;

    private DateTime end;

    @Override
    public int hashCode() {
        return HashTool.compute(start, end);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("start", start)
                .add("stop", end);
    }
}
