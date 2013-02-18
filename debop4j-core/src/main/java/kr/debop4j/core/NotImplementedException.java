package kr.debop4j.core;

import lombok.extern.slf4j.Slf4j;

/**
 * 구현되지 않은 코드에 예외를 발생시켜, 코드 구현을 해야 함을 알려주고, 구현 중에 컴파일 에러로 나타나게 하지 않게 하기 위함이다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 25.
 */
@Slf4j
public class NotImplementedException extends RuntimeException {

    private static final long serialVersionUID = -1276105737644188535L;

    public NotImplementedException() {
        this("구현이 되지 않았습니다. 구현해 주시기 바랍니다.");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
