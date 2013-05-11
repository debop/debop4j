package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Duration에 따라 시작시각, 완료시각을 계산되는 자료 구조입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 2:47
 */
public interface TimeBlock extends TimePeriod {

    /** 시작시각을 설정합니다. */
    void setStart(DateTime start);

    /** 완료시각을 설정합니다. */
    void setEnd(DateTime end);

    /** 시작시각을 기준으로 기간을 설정합니다. */
    void setDuration(Duration duration);

    /** 기간 설정 */
    void setup(DateTime newStart, DateTime newEnd);

    /** 시작시각은 고정, 기간(duration)으로 완료시각를 재설정 */
    void durationFromStart(Duration newDuration);

    /** 완료시각은 고정, 이전 기간(duration)으로 시작시각을 계산하여, 기간으로 재설정 */
    void durationFromEnd(Duration newDuration);

    /** 지정된 Offset만큼 기간이 이전 시간으로 이동한 TimeBlock을 반환한다. */
    TimeBlock getPreviousBlock(Duration offset);

    /** 지정된 Offset만큼 기간이 이후 시간으로 이동한 TimeBlock을 반환한다. */
    TimeBlock GetNextBlock(Duration offset);

}
