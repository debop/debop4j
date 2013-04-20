package kr.debop4j.core;

/**
 * 인자 4개를 받고, 결과를 반환하는 메소드를 가진 인터페이스
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 27.
 */
public interface Function4<T1, T2, T3, T4, R> {

    R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
}
