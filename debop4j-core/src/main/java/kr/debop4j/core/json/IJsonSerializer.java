package kr.debop4j.core.json;

import kr.debop4j.core.ISerializer;

/**
 * JSON 포맷으로 직렬화 / 역직렬화를 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 14
 */
public interface IJsonSerializer extends ISerializer {

    /**
     * JSON 포맷으로 직렬화하여 Json Text 형식의 문자열로 반환합니다.
     */
    String serializeToText(Object graph);

    /**
     * Json Text 형식의 문자열을 역직렬화하여, 객체로 빌드합니다.
     */
    <T> T deserializeFromText(String jsonText, Class<T> targetType);
}
