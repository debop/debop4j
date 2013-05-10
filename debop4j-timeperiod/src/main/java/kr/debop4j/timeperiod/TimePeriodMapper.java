package kr.debop4j.timeperiod;

import org.joda.time.DateTime;

/**
 * 기간의 시작시각, 완료시각에 대해 영역의 포함여부를 조절할 수 있도록 offset에 대한 처리를 제공합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:55
 */
public interface TimePeriodMapper {

    /** Start offset을 적용합니다. */
    DateTime mapStart(DateTime moment);

    /** End offset을 적용합니다. */
    DateTime mapEnd(DateTime moment);

    /** Offset이 적용된 시각에서 Start offset을 제거합니다. */
    DateTime unmapStart(DateTime moment);

    /** Offset이 적용된 시각에서 End offset을 제거합니다. */
    DateTime unmapEnd(DateTime moment);
}
