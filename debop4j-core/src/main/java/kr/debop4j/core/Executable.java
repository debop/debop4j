package kr.debop4j.core;

/**
 * kr.nsoft.commons.Executable
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 16.
 */
public interface Executable {

    /**
     * 특정 코드를 수행합니다.
     */
    public void execute();

    /**
     * 설정한 타임아웃이 되었을 때 호출되는 메소드입니다.
     */
    public void timedOut();
}
