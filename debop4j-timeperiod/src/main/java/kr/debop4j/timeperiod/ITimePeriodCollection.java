package kr.debop4j.timeperiod;

import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.ITimePeriodCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:36
 */
public interface ITimePeriodCollection extends ITimePeriodContainer {

    boolean hasInsidePeriods(ITimePeriod target);

    boolean hasOverlapPeriods(ITimePeriod target);

    boolean hasIntersectionPeriods(DateTime moment);

    boolean hasIntersectionPeriods(ITimePeriod target);

    /** 대상 ITimePeriod 기간을 포함하는 ITimePeriod 들을 열거합니다. */
    Iterable<ITimePeriod> insidePeriods(ITimePeriod target);

    Iterable<ITimePeriod> overlapPeriods(ITimePeriod target);

    /** 지정한 moment 시각과 교집합이 존재하는 TimePeriod를 열거합니다. */
    Iterable<ITimePeriod> intersectionPeriods(DateTime moment);

    /** 지정한 target 기간과 교집합이 존재하는 TimePeriod를 열거합니다. */
    Iterable<ITimePeriod> intersectionPeriods(ITimePeriod target);

    /** 대상 ITimePeriod 와 특정 관계를 가지는 ITimePeriod 요소들을 열거합니다. */
    Iterable<ITimePeriod> relationPeriods(ITimePeriod target, PeriodRelation relation, PeriodRelation... relations);
}
