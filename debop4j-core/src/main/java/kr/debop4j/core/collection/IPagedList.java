package kr.debop4j.core.collection;

import java.io.Serializable;
import java.util.List;

/**
 * 페이지 처리된 목록을 표현하는 인터페이스
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
public interface IPagedList<E> extends Serializable {

    /**
     * 페이지 처리된 목록
     */
    List<E> getList();

    /**
     * 페이지 번호 (1부터 시작)
     */
    int getPageNo();

    /**
     * 한 페이지의 크기 (1 이상)
     */
    int getPageSize();

    /**
     * 전체 페이지 수
     */
    long getPageCount();

    /**
     * 전체 항목의 수
     */
    long getItemCount();
}
