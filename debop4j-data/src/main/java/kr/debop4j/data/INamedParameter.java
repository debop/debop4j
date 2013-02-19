package kr.debop4j.data;

import java.io.Serializable;

/**
 * SQL 실행 문의 인자 정보를 나타내는 인터페이스입니다.
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
public interface INamedParameter extends Serializable {

    /**
     * 인자 명
     */
    String getName();

    /**
     * 인자 명을 설정합니다.
     *
     * @param name 설정할 인자 명
     */
    void setName(String name);

    /**
     * 인자 값
     */
    Object getValue();

    /**
     * 인자 값을 설정합니다.
     *
     * @param value 설정할 인자 값
     */
    void setValue(Object value);
}
