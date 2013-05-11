package kr.debop4j.timeperiod.tools;

import kr.debop4j.timeperiod.HalfyearKind;
import kr.debop4j.timeperiod.QuarterKind;

import java.util.Locale;

/**
 * kr.debop4j.timeperiod.tools.TimeSettings
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 8:03
 */
public class TimeSettings {

    // region << Year >>

    public static final String SystemYearNameFormat = "%s";
    public static final String CalendarYearNameFormat = "CY%s";
    public static final String FiscalYearNameFormat = "FY%s";
    public static final String SchoolYearNameFormat = "SY%s";

    public static String SystemYearName(int year) {
        return format(SystemYearNameFormat, year);
    }

    public static String CalendarYearName(int year) {
        return format(CalendarYearNameFormat, year);
    }

    public static String FiscalYearName(int year) {
        return format(FiscalYearNameFormat, year);
    }

    public static String SchoolYearName(int year) {
        return format(SchoolYearNameFormat, year);
    }

    // endregion

    // region << Halfyear >>

    public static final String SystemHalfYearNameFormat = "HY%s";
    public static final String CalendarHalfYearNameFormat = "CHY%s";
    public static final String FiscalHalfYearNameFormat = "FHY%s";
    public static final String SchoolHalfYearNameFormat = "SHY%s";

    public static final String SystemHalfyearOfYearNameFormat = "HY%s %s";
    public static final String CalendarHalfyearOfYearNameFormat = "CHY%s %s";
    public static final String FiscalHalfyearOfYearNameFormat = "FHY%s %s";
    public static final String SchoolHalfyearOfYearNameFormat = "SHY%s %s";

    public static String SystemHalfyearName(HalfyearKind halfyear) {
        return format(SystemHalfYearNameFormat, halfyear);
    }

    public static String CalendarHalfyearName(HalfyearKind halfyear) {
        return format(CalendarHalfYearNameFormat, halfyear);
    }

    public static String FiscalHalfyearName(HalfyearKind halfyear) {
        return format(FiscalHalfYearNameFormat, halfyear);
    }

    public static String SchoolHalfyearName(HalfyearKind halfyear) {
        return format(SchoolHalfYearNameFormat, halfyear);
    }

    public static String SystemHalfyearOfYearName(HalfyearKind halfyear, int year) {
        return format(SystemHalfyearOfYearNameFormat, halfyear, year);
    }

    public static String CalendarHalfyearOfYearName(HalfyearKind halfyear, int year) {
        return format(CalendarHalfyearOfYearNameFormat, halfyear, year);
    }

    public static String FiscalHalfyearOfYearName(HalfyearKind halfyear, int year) {
        return format(FiscalHalfyearOfYearNameFormat, halfyear, year);
    }

    public static String SchoolHalfyearOfYearName(HalfyearKind halfyear, int year) {
        return format(SchoolHalfyearOfYearNameFormat, halfyear, year);
    }

    // endregion

    // region << Quarter >>

    public static final String SystemQuarterNameFormat = "Q%s";
    public static final String CalendarQuarterNameFormat = "CQ%s";
    public static final String FiscalQuarterNameFormat = "FQ%s";
    public static final String SchoolQuarterNameFormat = "SQ%s";

    public static final String SystemQuarterOfYearNameFormat = "Q%s %s";
    public static final String CalendarQuarterOfYearNameFormat = "CQ%s %s";
    public static final String FiscalQuarterOfYearNameFormat = "FQ%s %s";
    public static final String SchoolQuarterOfYearNameFormat = "SQ%s %s";

    public static String SystemQuarterName(QuarterKind quarter) {
        return format(SystemQuarterNameFormat, quarter);
    }

    public static String CalendarQuarterName(QuarterKind quarter) {
        return format(CalendarQuarterNameFormat, quarter);
    }

    public static String FiscalQuarterName(QuarterKind quarter) {
        return format(FiscalQuarterNameFormat, quarter);
    }

    public static String SchoolQuarterName(QuarterKind quarter) {
        return format(SchoolQuarterNameFormat, quarter);
    }

    public static String SystemQuarterOfYearName(QuarterKind quarter, int year) {
        return format(SystemQuarterOfYearNameFormat, quarter, year);
    }

    public static String CalendarQuarterOfYearName(QuarterKind quarter, int year) {
        return format(CalendarQuarterOfYearNameFormat, quarter, year);
    }

    public static String FiscalQuarterOfYearName(QuarterKind quarter, int year) {
        return format(FiscalQuarterOfYearNameFormat, quarter, year);
    }

    public static String SchoolQuarterOfYearName(QuarterKind quarter, int year) {
        return format(SchoolQuarterOfYearNameFormat, quarter, year);
    }

    // endregion

    // region << Month >>

    public static final String MonthOfYearNameFormat = "%s %s";

    public static String MonthOfYearName(String monthName, String yearName) {
        return format(MonthOfYearNameFormat, monthName, yearName);
    }

    // endregion

    // region << Week >>

    public static final String WeekOfYearNameFormat = "w/c %s %s";

    public static String WeekOfYearName(int weekOfYear, String yearName) {
        return format(WeekOfYearNameFormat, weekOfYear, yearName);
    }

    // endregion

    // region Time Formatter

    public static String getTimeSpanYears() {
        return "Years";
    }

    public static String getTimeSpanYear() {
        return "Year";
    }

    public static String getTimeSpanMonths() {
        return "Months";
    }

    public static String getTimeSpanMonth() {
        return "Month";
    }

    public static String getTimeSpanWeeks() {
        return "Weeks";
    }

    public static String getTimeSpanWeek() {
        return "Week";
    }

    public static String getTimeSpanDays() {
        return "Days";
    }

    public static String getTimeSpanDay() {
        return "Day";
    }

    public static String getTimeSpanHours() {
        return "Hours";
    }

    public static String getTimeSpanHour() {
        return "Hour";
    }

    public static String getTimeSpanMinutes() {
        return "Minutes";
    }

    public static String getTimeSpanMinute() {
        return "Minute";
    }

    public static String getTimeSpanSeconds() {
        return "Seconds";
    }

    public static String getTimeSpanSecond() {
        return "Second";
    }

    // endregion

    static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }
}
