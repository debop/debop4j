package kr.debop4j.core.tools;

import kr.debop4j.core.Guard;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * kr.debop4j.core.tools.ListTool
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 23.
 */
public final class ListTool {

    private ListTool() {
    }

    /**
     * 컬렉션에서 최소값을 가지는 요소를 구합니다.
     */
//    @SuppressWarnings("unchecked")
//    public static <T> T min(Collection<? extends Comparable<T>> collection) {
//        if (collection == null) return null;
//
//        return (T) Collections.min(collection, new Comparator<T>());
//    }
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
        if (coll == null) return null;
        return Collections.max(coll, comp);
    }

    /**
     * 컬렉션에서 최대값을 가지는 요소를 구합니다.
     */
//    @SuppressWarnings("unchecked")
//    public static <T> T max(Collection<? extends Comparable<T>> collection) {
//        if (collection == null) return null;
//        return Collections.max(collection);
//    }

    /**
     * 컬렉션에서 최대값을 구합니다.
     */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        Guard.shouldNotBeNull(comp, "comp");
        if (coll == null) return null;

        return Collections.max(coll, comp);
    }
}
