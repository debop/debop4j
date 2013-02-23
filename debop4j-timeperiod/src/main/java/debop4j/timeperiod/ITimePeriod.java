package debop4j.timeperiod;

import org.joda.time.DateTime;

/**
 * 시간 간격을 나타내는 인터페이스
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
public interface ITimePeriod {

    /**
     * 기간의 시작 시각
     *
     * @return 시작 시각
     */
    DateTime getStart();

    /**
     * 기간의 완료 시각
     *
     * @return 완료 시각
     */
    DateTime getEnd();
}
