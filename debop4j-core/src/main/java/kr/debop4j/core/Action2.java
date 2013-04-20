package kr.debop4j.core;

/**
 * 인자 2개를 받고, void 형을 반환하는 메소드를 가진 인터페이스
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 27.
 */
public interface Action2<T1, T2> {

    public void perform(T1 arg1, T2 arg2);
}
