package kr.debop4j.core.timeperiod;

import org.joda.time.DateTime;

/**
 * 시간의 기간을 나타냅니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
public interface ITimePeriod {

    /**
     * 기간의 시작 시각
     */
    DateTime getStart();

    /**
     * 기간의 완료 시각
     */
    DateTime getEnd();
}
