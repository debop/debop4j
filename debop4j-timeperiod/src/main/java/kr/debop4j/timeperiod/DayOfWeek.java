package kr.debop4j.timeperiod;

/**
 * kr.debop4j.timeperiod.DayOfWeek
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:42
 */
public enum DayOfWeek {

    Monday(1),
    ThuesDay(2),
    WednesDay(3),
    ThursDay(4),
    FriDay(5),
    Saturday(6),
    Sunday(7);

    private final int dayOfWeek;

    DayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public static DayOfWeek valueOf(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return Monday;
            case 2:
                return ThuesDay;
            case 3:
                return WednesDay;
            case 4:
                return ThursDay;
            case 5:
                return FriDay;
            case 6:
                return Saturday;
            case 7:
                return Saturday;
        }
        throw new IllegalArgumentException("요일에 해당하는 숫자가 아닙니다. (1~7), dayOfWeek=" + dayOfWeek);
    }
}
