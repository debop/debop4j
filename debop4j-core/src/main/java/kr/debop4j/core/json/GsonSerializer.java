package kr.debop4j.core.json;

import com.google.common.base.Defaults;
import com.google.gson.Gson;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <a href="http://code.google.com/p/google-gson/">google-gson</a> 을 이용한 json serializer 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 14
 */
@Slf4j
public class GsonSerializer implements IJsonSerializer {

    @Getter
    private final Gson gson;

    public GsonSerializer() {
        this(new Gson());
    }

    public GsonSerializer(Gson gson) {
        this.gson = Guard.firstNotNull(gson, new Gson());
    }


    @Override
    public String serializeToText(Object graph) {
        if (graph == null)
            return "";

        if (log.isDebugEnabled())
            log.debug("Json 직렬화를 수행합니다... graph=[{}]", graph);

        return gson.toJson(graph);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> targetType) {
        return deserializeFromText(StringTool.getUtf8String(bytes), targetType);
    }

    @Override
    public <T> T deserializeFromText(String jsonText, Class<T> targetType) {
        if (StringTool.isWhiteSpace(jsonText))
            return Defaults.defaultValue(targetType);

        if (log.isDebugEnabled())
            log.debug("Json 역직렬화를 수행합니다. jsonText=[{}], targetType=[{}]",
                      StringTool.ellipsisChar(jsonText, 255), targetType);

        return gson.fromJson(jsonText, targetType);
    }

    @Override
    public byte[] serialize(Object graph) {
        return StringTool.getUtf8Bytes(serializeToText(graph));
    }
}
