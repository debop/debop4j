package kr.debop4j.timeperiod;

/**
 * 분기
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 11:31
 */
public enum QuarterKind {
    First(1), Second(2), Third(3), Fouth(4);

    private final int quarter;

    QuarterKind(int quarter) {
        this.quarter = quarter;
    }

    public static QuarterKind valueOf(int quarter) {
        switch (quarter) {
            case 1:
                return First;
            case 2:
                return Second;
            case 3:
                return Third;
            case 4:
                return Fouth;
        }
        throw new IllegalArgumentException("Invalid quarter number. [1-4]");
    }
}
