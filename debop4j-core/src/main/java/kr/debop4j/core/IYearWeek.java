package kr.debop4j.core;

/**
 * 주차 (WeekOfYear) 를 표협합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 19.
 */
public interface IYearWeek {

    /**
     * 년도
     */
    int getYear();

    /**
     * 년도를 설정합니다.
     */
    void setYear(int year);

    /**
     * 주차 정보
     */
    int getWeek();

    /**
     * 주차정보를 설정합니다.
     */
    void setWeek(int weekOfYear);
}
