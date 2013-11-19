package kr.debop4j.timeperiod;

/**
 * 특정년도와 주차를 표현하는 인터페이스
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:28
 */
public interface IYearAndWeekOfYear {

    /**
     * 년도
     */
    int getYear();

    /**
     * 주차의 년도를 설정
     */
    void setYear(int year);

    /**
     * 주차
     */
    int getWeekOfYear();

    /**
     * 주차를 설정합니다.
     */
    void setWeekOfYear(int weekOfYear);

}
