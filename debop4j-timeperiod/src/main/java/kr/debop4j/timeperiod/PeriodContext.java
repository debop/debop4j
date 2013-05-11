package kr.debop4j.timeperiod;

import kr.debop4j.core.Local;

import java.util.Locale;

/**
 * 현재 Thread 하에서 TimePeriod 관련하여 제공할 정보를 제공합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:49
 */
public class PeriodContext {

    private static final String TimeCalendarKey = PeriodContext.class.getName() + ".Current";

    /** 현 Thread Context 하에서 설정된 TimeCalendar 관련 설정 정보 */
    public static class Current {

        /** 현재 Thread Context하에서 사용할 TimeCalendar입니다. */
        public static ITimeCalendar getTimeCalendar() {
            ITimeCalendar calendar = Local.get(TimeCalendarKey, ITimeCalendar.class);
            if (calendar == null) {
                calendar = TimeCalendar.create();
                Local.put(TimeCalendarKey, calendar);
            }
            return calendar;
        }

        public static void setTimeCalendar(ITimeCalendar calendar) {
            Local.put(TimeCalendarKey, calendar);
        }

        /** 현 Thread context의 {@link Locale} 정보 */
        public static Locale getLocale() {
            return getTimeCalendar().getLocale();
        }

        /** 한 주의 첫번째 요일 */
        public static DayOfWeek getFirstDayOfWeek() {
            return getTimeCalendar().getFirstDayOfWeek();
        }
    }
}
