package kr.debop4j.core.tools;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * 날짜 관련 Utility Class
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 27.
 */
@Slf4j
public class DateTool {

    private DateTool() {}

    public static DateTime getStartOfDay(DateTime time) {
        return time.withTimeAtStartOfDay();
    }

    public static DateTime getEndOfDay(DateTime time) {
        return getStartOfDay(time).minus(1);
    }

    public static DateTime getStartOfWeek(DateTime time) {
        int add = DateTimeConstants.MONDAY - time.getDayOfWeek();
        if (add > 0)
            add -= 7;
        return time.withTimeAtStartOfDay().plusDays(add);
    }

    public static DateTime getEndOfWeek(DateTime time) {
        return getStartOfWeek(time).plusDays(DateTimeConstants.DAYS_PER_WEEK).minus(1);
    }

    public static DateTime getStartOfMonth(DateTime time) {
        return new DateTime().withDate(time.getYear(), time.getMonthOfYear(), 1);
    }

    public static DateTime getEndOfMonth(DateTime time) {
        return getStartOfMonth(time).plusMonths(1).minus(1);
    }

    public static DateTime getStartOfYear(int year) {
        return new DateTime().withDate(year, 1, 1);
    }

    public static DateTime getEndOfYear(int year) {
        return getStartOfYear(year + 1).minus(1);
    }
}
