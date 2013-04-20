package kr.debop4j.core.tools;

/**
 * 해시코드를 생성합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 27
 */
public class HashTool {

    public static final int NullValue = 0;
    public static final int OneValue = 1;
    public static final int Factor = 31;

    private static int computeInternal(Object x) {
        return (x != null) ? x.hashCode() : NullValue;
    }

    /**
     * 지정된 객체들의 Hash Code를 조합한 Hash Code를 생성합니다.
     *
     * @param objs 해쉬코드를 생성할 객체 배열
     * @return 조합된 Hash code
     */
    public static int compute(Object... objs) {
        if (objs == null || objs.length == 0)
            return NullValue;

        int hash = NullValue;
        for (Object x : objs) {
            hash = hash * Factor + computeInternal(x);
        }
        return hash;
    }
}
