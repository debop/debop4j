package kr.debop4j.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.core.json.FastJsonSerializer
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오전 12:57
 */
@Slf4j
public class FastJsonSerializer implements IJsonSerializer {

    private SerializerFeature[] features = new SerializerFeature[]{
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteClassName
    };

    @Override
    public String serializeToText(Object graph) {
        if (graph == null)
            return "";
        if (log.isTraceEnabled())
            log.trace("지정한 객체를 JSON 직렬화를 수행합니다. graph=[{}]", graph);
        return JSON.toJSONString(graph, features);
    }

    @Override
    public <T> T deserializeFromText(String jsonText, Class<T> targetType) {
        if (log.isTraceEnabled())
            log.trace("Json 문자열을 역직렬화하여 지정한 클래스롤 빌드합니다. targetType=[{}]", targetType.getName());
        return JSON.parseObject(jsonText, targetType, Feature.AllowISO8601DateFormat);
    }

    @Override
    public byte[] serialize(Object graph) {
        if (graph == null)
            return new byte[0];

        if (log.isTraceEnabled())
            log.trace("지정한 객체를 JSON 직렬화를 수행합니다. graph=[{}]", graph);

        return JSON.toJSONBytes(graph, features);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (log.isTraceEnabled())
            log.trace("Json 배열을 역직렬화하여 지정한 클래스롤 빌드합니다. clazz=[{}]", clazz.getName());
        return JSON.parseObject(bytes, clazz, Feature.AllowISO8601DateFormat);
    }
}
