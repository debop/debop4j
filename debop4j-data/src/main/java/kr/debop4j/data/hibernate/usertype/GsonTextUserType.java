package kr.debop4j.data.hibernate.usertype;

import kr.debop4j.core.json.GsonSerializer;
import kr.debop4j.core.json.IJsonSerializer;

/**
 * {@link GsonSerializer} 를 이용하여, 객체를 Json 직렬화하여 저장하는 사용자 타입입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public class GsonTextUserType extends AbstractJsonTextUserType {

    private static final IJsonSerializer serializer = new GsonSerializer();

    @Override
    public IJsonSerializer getJsonSerializer() {
        return serializer;
    }
}
