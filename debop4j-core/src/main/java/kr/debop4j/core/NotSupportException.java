package kr.debop4j.core;

/**
 * 시스템에서 지원하지 않는 기능을 호출했을 때 발생하는 예외입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 27.
 */
public class NotSupportException extends RuntimeException {

    private static final long serialVersionUID = 231848084992090602L;

    public NotSupportException() {
        this("지원하지 않는 코드입니다.");
    }

    public NotSupportException(String message) {
        super(message);
    }
}
