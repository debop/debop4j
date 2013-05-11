package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 시작 시각 ~ 완료시각이라는 시간의 범위를 나타내는 자료구조이고, 기간(Duration) 값은 계산됩니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:07
 */
public interface ITimeRange extends ITimePeriod {

    /** 시작시각을 설정합니다. */
    void setStart(DateTime start);

    /** 완료시각을 설정합니다. */
    void setEnd(DateTime end);

    /** 시작시각을 기준으로 기간을 설정합니다. */
    void setDuration(Duration duration);

    /** 시작시각을 지정된 시각으로 설정합니다. 시작시각 이전이여야 합니다. */
    void expandStartTo(DateTime moment);

    /** 완료시각을 지정된 시각으로 설정합니다. 완료시각 이후여야 합니다. */
    void expandEndTo(DateTime moment);

    /** 시작시각, 완료시각을 지정된 시각으로 설정합니다. */
    void expandTo(DateTime moment);

    /** 시작시각과 완료시각을 지정된 기간으로 설정합니다. */
    void expandTo(ITimePeriod period);

    /** 시작시각을 지정된 시각으로 설정합니다. 시작시각 이후여야 합니다. */
    void shrinkStartTo(DateTime moment);

    /** 완료시각을 지정된 시각으로 설정합니다. 완료시각 이전이어야 합니다. */
    void shrinkEndTo(DateTime moment);

    /** 시작시각과 완료시각을 지정된 기간으로 설정합니다. */
    void shrinkTo(ITimePeriod period);
}
