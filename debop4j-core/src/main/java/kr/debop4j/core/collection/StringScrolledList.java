package kr.debop4j.core.collection;

import java.util.List;

/**
 * 스크롤되는 목록을 표현하는 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 13
 */
public class StringScrolledList<E> extends ScrolledListBase<E, String> {

    private static final long serialVersionUID = 3814255695145481403L;

    public StringScrolledList(List<E> list, String lowerBound, String upperBound) {
        super(list, lowerBound, upperBound);
    }
}
