package kr.debop4j.timeperiod;

import org.joda.time.DateTime;

import java.util.List;

/**
 * kr.debop4j.timeperiod.ITimePeriodContainer
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:33
 */
public interface ITimePeriodContainer extends List<ITimePeriod>, ITimePeriod {

    void setStart(DateTime start);

    void setEnd(DateTime end);

    boolean isReadonly();

    boolean containsPeriod(ITimePeriod target);

    void addAll(Iterable<ITimePeriod> periods);

    void sortByStart(OrderDirection sortDir);

    void sortByEnd(OrderDirection sortDir);

    void sortByDuration(OrderDirection sortDir);
}
