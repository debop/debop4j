package kr.debop4j.core.collection;

import java.io.Serializable;
import java.util.List;

/**
 * 스크롤되는 목록을 표현하는 인터페이스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
public interface IScrolledList<E, N extends Comparable<N>> extends Serializable {

    /**
     * 스크롤 영역의 목록
     */
    List<E> getList();

    /**
     * 스크롤 영역의 하한 값
     */
    N getLowerBound();

    /**
     * 스크롤 영역의 상한 값
     */
    N getUpperBound();

}
