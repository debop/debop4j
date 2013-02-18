package kr.debop4j.core;

/**
 * 정렬 방법
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 19.
 */
public enum SortDirection {

    /**
     * 순차 정렬
     */
    ASC("ASC"),

    /**
     * 역순 정렬
     */
    DESC("DESC");

    private final String sortDirection;

    SortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

}
