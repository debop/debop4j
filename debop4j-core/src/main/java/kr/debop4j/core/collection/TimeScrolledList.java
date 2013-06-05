package kr.debop4j.core.collection;

import java.util.Date;
import java.util.List;

/**
 * 스크롤되는 목록을 표현하는 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 13
 */
public class TimeScrolledList<E> extends ScrolledListBase<E, Date> {

    private static final long serialVersionUID = 8043558335662744201L;

    /**
     * Instantiates a new Time scrolled list.
     *
     * @param list       the list
     * @param lowerBound lower bound
     * @param upperBound upper bound
     */
    public TimeScrolledList(List<E> list, Date lowerBound, Date upperBound) {
        super(list, lowerBound, upperBound);
    }
}
