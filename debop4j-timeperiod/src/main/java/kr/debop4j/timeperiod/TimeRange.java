package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * kr.debop4j.timeperiod.TimeRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:07
 */
public interface TimeRange extends TimePeriod {

    void setStart(DateTime start);

    void setEnd(DateTime end);

    void setDuration(Duration duration);

    void expandStartTo(DateTime moment);

    void expandEndTo(DateTime moment);

    void expandTo(DateTime moment);

    void expandTo(TimePeriod period);

    void shrinkStartTo(DateTime moment);

    void shrinkEndTo(DateTime moment);

    void shrinkTo(DateTime moment);

    void shrinkTo(TimePeriod period);
}
