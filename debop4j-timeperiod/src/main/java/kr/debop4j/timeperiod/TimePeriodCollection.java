package kr.debop4j.timeperiod;

import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.TimePeriodCollection
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:36
 */
public interface TimePeriodCollection extends TimePeriodContainer {

    boolean hasInsidePeriods(TimePeriod target);

    boolean hasOverlapPeriods(TimePeriod target);

    boolean hasIntersectionPeriods(DateTime moment);

    boolean hasIntersectionPeriods(TimePeriod target);

    /** 대상 TimePeriod 기간을 포함하는 TimePeriod 들을 열거합니다. */
    Iterable<TimePeriod> insidePeriods(TimePeriod target);

    Iterable<TimePeriod> overlapPeriods(TimePeriod target);

    /** 지정한 moment 시각과 교집합이 존재하는 TimePeriod를 열거합니다. */
    Iterable<TimePeriod> intersectionPeriods(DateTime moment);

    /** 지정한 target 기간과 교집합이 존재하는 TimePeriod를 열거합니다. */
    Iterable<TimePeriod> intersectionPeriods(TimePeriod target);

    /** 대상 TimePeriod 와 특정 관계를 가지는 TimePeriod 요소들을 열거합니다. */
    Iterable<TimePeriod> relationPeriods(TimePeriod target, PeriodRelation relation, PeriodRelation... relations);
}
