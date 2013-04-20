package kr.debop4j.core.collection;

import lombok.Getter;

import java.util.List;

import static kr.debop4j.core.Guard.*;

/**
 * 페이징된 목록을 표현하는 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 13
 */
@Getter
public class SimplePagedList<E> implements IPagedList<E> {

    private static final long serialVersionUID = -5027718652421583413L;

    private final List<E> list;
    private final int pageNo;
    private final int pageSize;
    private final long itemCount;
    private final long pageCount;

    public SimplePagedList(List<E> list, int pageNo, int pageSize, long itemCount) {
        this.list = shouldNotBeNull(list, "list");
        this.pageNo = shouldBePositiveNumber(pageNo, "pageNo");
        this.pageSize = shouldBePositiveNumber(pageSize, "pageSize");
        this.itemCount = shouldNotBeNegativeNumber(itemCount, "itemCount");

        this.pageCount = (long) (itemCount / pageSize) + ((itemCount % pageSize) > 0 ? 1 : 0);
    }
}
