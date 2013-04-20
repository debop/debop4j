package kr.debop4j.core;

/**
 * 객체를 직렬화/역직렬화를 수행하는 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 22.
 */
public interface ISerializer {

    /**
     * 객체를 직렬화하여 바이트 배열로 변환합니다.
     */
    byte[] serialize(Object graph);

    /**
     * 직렬화된 객체 정보를 역직렬화하여 객체로 변환합니다.
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
