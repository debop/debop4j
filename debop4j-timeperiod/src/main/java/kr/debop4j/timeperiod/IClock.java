package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * kr.debop4j.timeperiod.IClock
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 7:14
 */
public interface IClock {

    /** 현재 시각 */
    DateTime now();

    /** 현재 시각의 date part 만 */
    DateTime today();

    /** 현재 시각의 일자를 제외한 time part 만 */
    Duration timeOfDay();
}
