package kr.debop4j.timeperiod;

import java.io.Serializable;

/**
 * TimePeriod의 컬렉션을 가지며, 이를 통해 여러 기간에 대한 Union, Intersection, Gap 등을 구할 수 있도록 합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 6:06
 */
public interface ITimeLine extends Serializable {

    ITimePeriodContainer getPeriods();

    ITimePeriod getLimits();

    ITimePeriodMapper getPeriodMapper();

    ITimePeriodCollection combinePeriods();

    ITimePeriodCollection intersectPeriods();

    ITimePeriodCollection calculateGaps();
}
