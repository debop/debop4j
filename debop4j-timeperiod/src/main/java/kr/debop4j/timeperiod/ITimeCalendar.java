package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * 문화권에 따른 날짜 표현, 날짜 계산 등을 제공하는 Calendar 입니다. (ISO 8601, Korean 등)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 6:42
 */
public interface ITimeCalendar extends ITimePeriodMapper {

    /** 문화권 정보 (문화권에 따라 달력에 대한 규칙 및 명칭이 달라집니다.) */
    Locale getLocale();

    /** 시작 오프셋 (시작일자가 1월 1일이 아닌 경우) */
    Duration getStartOffset();

    /** 종료 오프셋 */
    Duration getEndOffset();

    /** 한 주의 시작 요일 (한국, 미국: Sunday, ISO-8601: Monday) */
    DayOfWeek getFirstDayOfWeek();

    /** 지정된 일자의 년 */
    int getYear(DateTime time);

    /** 지정된 일자의 월 */
    int getMonth(DateTime time);

    /** 지정된 시각의 시간 */
    int getHour(DateTime time);

    /** 지정된 시각의 분 */
    int getMinute(DateTime time);

    /** 지정된 날짜의 월 몇번째 일인지 */
    int getDayOfMonth(DateTime time);

    /** 지정된 날짜의 요일 */
    DayOfWeek getDayOfWeek(DateTime time);

    /** 지정된 년,월의 날짜수 */
    int getDaysInMonth(int year, int month);

    /** 년도 이름 */
    String getYearName(int year);

    /** 반기를 표현하는 문자열을 반환합니다. */
    String getHalfYearName(HalfyearKind halfyear);

    /** 지정한 년도의 반기를 표현하는 문자열을 반환합니다. */
    String getHalfYearOfYearName(int year, HalfyearKind halfyear);

    /** 분기를 표현하는 문자열을 반환합니다. (2사분기) */
    String getQuarterName(QuarterKind quarter);

    /** 특정년도의 분기를 표현하는 문자열을 반환합니다. (2011년 2사분기) */
    String getQuarterOfYearName(int year, QuarterKind quarter);

    /** 월을 표현하는 문자열을 반환합니다. */
    String getMonthName(int month);

    /** 특정 년, 월을 표현하는 문자열을 반환합니다. */
    String getMonthOfYearName(int year, int month);

    /** 년,주차를 문자열로 표현합니다. */
    String getWeekOfYearName(int year, int weekOfYear);

    /** 지정한 요일을 문자열로 표현합니다. */
    String getDayName(DayOfWeek dayOfWeek);

    /** 지정된 일자의 주차(Week of Year)를 반환합니다. */
    int getWeekOfYear(DateTime time);

    /** 지정된 년, 주차에 해당하는 주의 첫번째 일자를 반환한다. (예: 2011년 3주차의 첫번째 일자는?) */
    DateTime getStartOfYearWeek(int year, int weekOfYear);
}
