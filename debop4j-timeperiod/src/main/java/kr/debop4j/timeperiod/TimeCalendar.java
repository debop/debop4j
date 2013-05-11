package kr.debop4j.timeperiod;

import kr.debop4j.core.NotImplementedException;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * 문화권에 따른 날짜 표현, 날짜 계산 등을 제공하는 Calendar 입니다. (ISO 8601, Korean 등)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:56
 */
@Slf4j
public class TimeCalendar implements ITimeCalendar {
    private static final long serialVersionUID = -8731693901249037388L;

    public static final Duration DefaultStartOffset = TimeSpec.NoDuration;
    public static final Duration DefaultEndOffset = TimeSpec.MinNegativeDuration;

    public static TimeCalendar create() {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar create(Locale locale) {
        throw new NotImplementedException("구현 중");
    }

    public static TimeCalendar create(int yearBaseMonth) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public Locale getLocale() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Duration getStartOffset() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Duration getEndOffset() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DayOfWeek getFirstDayOfWeek() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getYear(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMonth(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getHour(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMinute(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDayOfMonth(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DayOfWeek getDayOfWeek(DateTime time) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDaysInMonth(int year, int month) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getYearName(int year) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHalfYearName(HalfyearKind halfyear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHalfYearOfYearName(int year, HalfyearKind halfyear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getQuarterName(QuarterKind quarter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getQuarterOfYearName(int year, QuarterKind quarter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMonthName(int month) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMonthOfYearName(int year, int month) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getWeekOfYearName(int year, int weekOfYear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDayName(DayOfWeek dayOfWeek) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getWeekOfYear(DateTime time) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime getStartOfYearWeek(int year, int weekOfYear) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime mapStart(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime mapEnd(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime unmapStart(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DateTime unmapEnd(DateTime moment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
