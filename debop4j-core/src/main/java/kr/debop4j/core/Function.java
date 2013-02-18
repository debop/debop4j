package kr.debop4j.core;

/**
 * 인자 0개를 받고, 결과를 반환하는 메소드를 가진 인터페이스
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 27.
 */
public interface Function<R> {

    R execute();
}
