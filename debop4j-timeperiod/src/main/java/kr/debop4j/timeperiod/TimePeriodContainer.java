package kr.debop4j.timeperiod;

import org.joda.time.DateTime;

import java.util.List;

/**
 * kr.debop4j.timeperiod.TimePeriodContainer
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:33
 */
public interface TimePeriodContainer extends List<TimePeriod>, TimePeriod {

    void setStart(DateTime start);

    void setEnd(DateTime end);

    boolean isReadonly();

    boolean containsPeriod(TimePeriod target);

    void addAll(Iterable<TimePeriod> periods);

    void sortByStart(OrderDirection sortDir);

    void sortByEnd(OrderDirection sortDir);

    void sortByDuration(OrderDirection sortDir);
}
