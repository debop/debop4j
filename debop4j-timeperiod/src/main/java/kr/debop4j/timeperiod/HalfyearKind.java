package kr.debop4j.timeperiod;

/**
 * 반기
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:03
 */
public enum HalfyearKind {

    /** 상반기 */
    First(1),

    /** 하반기 */
    Second(2);

    private final int halfyear;

    HalfyearKind(int halfyear) {
        this.halfyear = halfyear;
    }

    public int getValue() {
        return halfyear;
    }
}
