package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 시간 간격을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:58
 */
public interface ITimeInterval extends ITimePeriod {

    /** 시작시작이 열린 구간인가? 즉 start time 에 값이 없다 */
    boolean isStartOpen();

    /** 완료시작이 열린 구간인가? 즉 end time 에 값이 없다 */
    boolean isEndOpen();

    /** 개구간인가? */
    boolean isOpen();

    /** 시작시각이 닫힌 구간인가? 즉 시작시각에 값이 있는가? */
    boolean isStartClosed();

    /** 완료시각이 닫힌 구간인가? 즉 완료시각에 값이 있는가? */
    boolean isEndClosed();

    /** 폐구간인가? */
    boolean isClosed();

    /** 빈 간격인가? */
    boolean isEmpty();

    /** Interval로 쓸 수 없는 경우 (isMoment 이면서, isClosed 인 경우) */
    boolean isDegenerate();

    /** 사용가능한 시간간격인가? */
    boolean isIntervalEnabled();

    DateTime getStartInterval();

    void setStartInterval(DateTime start);

    DateTime getEndInterval();

    void setEndInterval(DateTime end);

    IntervalEdge getStartEdge();

    void setStartEdge(IntervalEdge startEdge);

    IntervalEdge getEndEdge();

    void setEndEdge(IntervalEdge startEdge);

    void expandStartTo(DateTime moment);

    void expandEndTo(DateTime moment);

    void expandTo(DateTime moment);

    void expandTo(ITimePeriod period);

    void shrinkStartTo(DateTime moment);

    void shrinkEndTo(DateTime moment);

    void shrinkTo(DateTime moment);

    void shrinkTo(ITimePeriod period);

    /** 현재 IInterval에서 오프셋만큼 이동한 {@link ITimeInterval}을 반환합니다. */
    @Override
    ITimeInterval copy(Duration offset);
}


