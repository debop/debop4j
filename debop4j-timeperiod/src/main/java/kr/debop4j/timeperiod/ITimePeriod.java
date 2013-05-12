package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.Serializable;

/**
 * kr.debop4j.timeperiod.ITimePeriod
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 10:49
 */
public interface ITimePeriod extends Comparable<ITimePeriod>, Serializable {

    DateTime getStart();

    DateTime getEnd();

    Duration getDuration();

    String getDurationDescription();

    boolean hasStart();

    boolean hasEnd();

    boolean hasPeriod();

    /** 시작시각과 완료시각의 값이 있고, 같은 값인가? */
    boolean isMoment();

    /** 시작시각과 완료시각 모두 정해지지 않은 경우 */
    boolean isAnytime();

    /** 읽기 전용 여부 */
    boolean isReadonly();

    void setup(DateTime newStart, DateTime newEnd);

    ITimePeriod copy(Duration offset);

    void move(Duration offset);

    boolean isSamePeriod(ITimePeriod other);

    boolean hasInside(DateTime moment);

    boolean hasInside(ITimePeriod other);

    boolean intersectsWith(ITimePeriod other);

    boolean overlapsWith(ITimePeriod other);

    /** 기간을 미정으로 초기화합니다. */
    void reset();

    /** 다른 ITimePeriod 와의 관계를 나타냅니다. */
    PeriodRelation getRelation(ITimePeriod other);

    /** ITimePeriod 정보를 문자열로 표현합니다. */
    String getDescription(TimeFormatter formatter);

    /** 두 기간이 겹치는 (교집합) 기간을 반환합니다. */
    ITimePeriod getIntersection(ITimePeriod other);

    /** 두 기간의 합집합 기간을 반환합니다. */
    ITimePeriod getUnion(ITimePeriod other);

}
