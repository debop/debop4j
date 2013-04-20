/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;

/**
 * jackson 라이브러리를 이용한 Json Serializer
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 14
 */
public class JacksonSerializer implements IJsonSerializer {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JacksonSerializer.class);
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Getter
    private final ObjectMapper mapper;

    public JacksonSerializer() {
        this(new ObjectMapper());
    }

    public JacksonSerializer(ObjectMapper mapper) {
        this.mapper = Guard.firstNotNull(mapper, new ObjectMapper());
    }

    @Override
    public byte[] serialize(Object graph) {
        try {
            if (graph == null)
                return ArrayTool.EmptyByteArray;

            if (log.isDebugEnabled())
                log.debug("인스턴스를 JSON 포맷으로 직렬화합니다. graph=[{}]", graph);

            return mapper.writeValueAsBytes(graph);
        } catch (Exception e) {

            log.error("객체를 Json 직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String serializeToText(Object graph) {
        try {
            if (graph == null)
                return "";

            if (log.isDebugEnabled())
                log.debug("인스턴스를 JSON 포맷으로 직렬화합니다. graph=[{}]", graph);

            return mapper.writeValueAsString(graph);
        } catch (Exception e) {

            log.error("객체를 Json 직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserializeFromText(String jsonText, Class<T> targetType) {
        try {
            if (StringTool.isWhiteSpace(jsonText))
                return (T) null;

            if (log.isDebugEnabled())
                log.debug("JSON 역직렬화를 수행합니다. valueType=[{}]", targetType.getName());

            return mapper.readValue(jsonText, targetType);
        } catch (Exception e) {

            log.error("Json 역직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> targetType) {
        try {
            if (ArrayTool.isEmpty(bytes))
                return (T) null;

            if (log.isDebugEnabled())
                log.debug("JSON 역직렬화를 수행합니다. targetType=[{}]", targetType.getName());

            return mapper.readValue(bytes, targetType);
        } catch (Exception e) {

            log.error("Json 역직렬화하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
    }
}
