package kr.debop4j.core.collection;

import java.util.List;

/**
 * 스크롤되는 목록을 표현하는 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 13
 */
public class IntScrolledList<E> extends ScrolledListBase<E, Integer> {

    private static final long serialVersionUID = 290234509250517728L;

    public IntScrolledList(List<E> list, int lowerBound, int upperBound) {
        super(list, lowerBound, upperBound);
    }
}
