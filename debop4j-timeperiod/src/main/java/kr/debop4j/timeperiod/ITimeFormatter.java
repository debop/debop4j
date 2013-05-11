package kr.debop4j.timeperiod;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * 시간 정보를 여러가지 문자 포맷으로 제공하기 위한 Formatter입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 6:11
 */
public interface ITimeFormatter {

    /** 문화권 */
    Locale getLocale();

    /** 목록 구분자 */
    String getListSeparator();

    /** 요소 구분자 */
    String getContextSeparator();

    /** 기간의 시작과 완료의 구분자 */
    String getStartEndSeparator();

    /** 기간 구분자 */
    String getDurationSeparator();

    /** 일자에 대한 포맷 */
    String getDateTimeFormat();

    /** 단순 날짜 포맷 */
    String getShortDateFormat();

    /** 긴 시각 포맷 */
    String getLongTimeFormat();

    /** 짧은 시각 포맷 */
    String getShortTimeFormat();

    /** 기간을 문자열로 표현하는 방식 */
    DurationFormatKind getDurationKind();

    /** 기간을 초단위로 표시할 것인가? */
    boolean getUseDurationSeconds();

    /** 컬렉션의 index를 문자열로 표현합니다. */
    String getCollection(int count);

    /** 컬렉션의 기간 정보를 문자열로 표현한다. */
    String getCollectionPeriod(int count, DateTime start, DateTime end, Duration duration);

    /** 지정한 일자를 문자열로 표현합니다. */
    String getDateTime(DateTime value);

    /** 지정된 DateTime을 Short Date 형식으로 문자열을 만듭니다. */
    String getShortDate(DateTime dateTime);

    /** Long Time 형식의 문자열로 표현합니다. */
    String getLongTime(DateTime dateTime);

    /** Short Time 형식의 문자열로 표현합니다. */
    String getShortTime(DateTime dateTime);

    /** 기간을 문자열로 표현합니다. */
    String getDuration(Duration duration);

    /** 기간을 문자열로 표현합니다. */
    String getDuration(Duration Duration, DurationFormatKind durationFormatKind);

    /** 기간을 문자열로 표현합니다. */
    String getDuration(int years, int months, int days, int hours, int minutes, int seconds);

    /** 시작-완료 시각을 문자열로 표현합니다. */
    String getPeriod(DateTime start, DateTime end);

    /** 시작-완료 | 기간 을 문자열로 표현합니다. */
    String getPeriod(DateTime start, DateTime end, Duration duration);

    /** 시간 간격을 문자열로 표현합니다. */
    String getInterval(DateTime start, DateTime end, IntervalEdge startEdge, IntervalEdge endEdge, Duration duration);

    /** Locale 기준으로 시작-완료 | 기간을 문자열로 표현합니다. */
    String getCalendarPeriod(String start, String end, Duration duration);

    /** Locale 기준으로 시작-완료 | 기간을 문자열로 표현합니다. */
    String getCalendarPeriod(String context, String start, String end, Duration duration);

    /** Locale 기준으로 시작-완료 | 기간을 문자열로 표현합니다. */
    String getCalendarPeriod(String startContext, String endContext, String start, String end, Duration duration);
}
