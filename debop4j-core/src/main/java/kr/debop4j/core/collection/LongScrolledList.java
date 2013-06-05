package kr.debop4j.core.collection;


import java.util.List;

/**
 * 스크롤되는 목록을 표현하는 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
public class LongScrolledList<E> extends ScrolledListBase<E, Long> {

    private static final long serialVersionUID = -7568596501307808532L;

    /**
     * Instantiates a new Long scrolled list.
     *
     * @param list       the list
     * @param lowerBound the lower bound
     * @param upperBound the upper bound
     */
    public LongScrolledList(List<E> list, long lowerBound, long upperBound) {
        super(list, lowerBound, upperBound);
    }
}
