package org.hibernate.redis.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.ArrayTool;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson 라이브러리를 이용한 Json Serializer 입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 11:45
 */
public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    private static final Logger log = LoggerFactory.getLogger(JacksonRedisSerializer.class);

    @Getter
    private final ObjectMapper mapper;

    public JacksonRedisSerializer() {
        this(new ObjectMapper());
    }

    public JacksonRedisSerializer(ObjectMapper mapper) {
        this.mapper = Guard.firstNotNull(mapper, new ObjectMapper());
    }

    @Override
    public byte[] serialize(T graph) {
        try {
            if (graph == null)
                return EMPTY_BYTES;

            if (log.isTraceEnabled()) log.trace("인스턴스를 JSON 포맷으로 직렬화합니다. graph=[{}]", graph);

            return mapper.writeValueAsBytes(graph);
        } catch (Exception e) {
            log.error("객체를 Json 직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public T deserialize(byte[] bytes) {
        try {
            if (ArrayTool.isEmpty(bytes))
                return (T) null;

            if (log.isDebugEnabled())
                log.debug("JSON 역직렬화를 수행합니다...");

            return (T) mapper.readValue(bytes, Object.class);
        } catch (Exception e) {
            log.error("Json 역직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }
}
