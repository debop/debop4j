package kr.debop4j.timeperiod;

import org.joda.time.DateTime;

/**
 * {@link ITimePeriod} 요소들을 LinkedList 방식으로 연속해서 나열하여 관리하는 컬렉션입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 11:44
 */
public interface ITimePeriodChain extends ITimePeriodContainer {

    /** Chain의 시작 시각 */
    void setStart(DateTime start);

    /** Chain의 완료 시각 */
    void setEnd(DateTime end);

    /** Chain의 첫번째 Period */
    ITimePeriod getFirst();

    /** Chain의 마지막 Period */
    ITimePeriod getLast();
}
