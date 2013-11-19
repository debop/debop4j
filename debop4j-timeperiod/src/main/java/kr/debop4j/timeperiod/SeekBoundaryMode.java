package kr.debop4j.timeperiod;

/**
 * kr.debop4j.timeperiod.SeekBoundaryMode
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 11:40
 */
public enum SeekBoundaryMode {

    /**
     * DateTime 검색 시 검색한 값을 반환하도록 한다.
     */
    Fill,

    /**
     * DateTime 검색 시 검색한 다음 값을 반환하도록 한다.
     */
    Next
}
