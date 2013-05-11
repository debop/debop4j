package kr.debop4j.timeperiod;

/**
 * {@link ITimeCalendar} 설정정보를 바탕으로 하는 {@link ITimeRange}를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 7:17
 */
public interface ICalendarITimeRange extends ITimeRange {

    /** 기간 설정에 사용될 {@link ITimeCalendar} */
    ITimeCalendar getTimeCalendar();
}
