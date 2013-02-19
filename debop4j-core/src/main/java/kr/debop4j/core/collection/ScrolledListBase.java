package kr.debop4j.core.collection;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * 스크롤되는 목록을 표현하는 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 13
 */
@Slf4j
public abstract class ScrolledListBase<E, N extends Comparable<N>> implements IScrolledList<E, N> {

    private static final long serialVersionUID = -5077876937253068976L;

    @Getter
    private final List<E> list;

    @Getter
    private final N lowerBound;

    @Getter
    private final N upperBound;

    public ScrolledListBase(List<E> list, N lowerBound, N upperBound) {
        this.list = shouldNotBeNull(list, "list");
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public String toString() {
        return Objects.toStringHelper(this)
                .add("lowerBound", lowerBound)
                .add("upperBound", upperBound)
                .add("list", list)
                .toString();
    }
}
