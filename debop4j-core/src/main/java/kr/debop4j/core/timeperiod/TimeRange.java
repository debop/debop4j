package kr.debop4j.core.timeperiod;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

/**
 * debop4j.timeperiod.TimeRange
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 11.
 */
@Getter
@Setter
public class TimeRange extends TimePeriodBase {

    private static final long serialVersionUID = -8368542575772797121L;

    public TimeRange() {}

    public TimeRange(DateTime start, DateTime end) {
        super(start, end);
    }
}
