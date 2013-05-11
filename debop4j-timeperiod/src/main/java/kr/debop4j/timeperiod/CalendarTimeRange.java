package kr.debop4j.timeperiod;

/**
 * {@link TimeCalendar} 설정정보를 바탕으로 하는 {@link TimeRange}를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 7:17
 */
public interface CalendarTimeRange extends TimeRange {

    /** 기간 설정에 사용될 {@link TimeCalendar} */
    TimeCalendar getTimeCalendar();
}
