package kr.debop4j.timeperiod;

/**
 * TimePeriod의 컬렉션을 가지며, 이를 통해 여러 기간에 대한 Union, Intersection, Gap 등을 구할 수 있도록 합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 6:06
 */
public interface TimeLine {

    TimePeriodContainer getPeriods();

    TimePeriod getLimits();

    TimePeriodMapper getPeriodMapper();

    TimePeriodCollection combinePeriods();

    TimePeriodCollection intersectPeriods();

    TimePeriodCollection calculateGaps();
}
