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

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;

/**
 * 객체를 JSON 직렬화를 수행하여, 그 결과를 저장하려고 할 때 사용한다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public class JsonTextObject extends ValueObjectBase {

    private static final long serialVersionUID = 8434059177726276296L;

    public static final JsonTextObject Empty = new JsonTextObject(null, null);

    @Getter
    @Setter
    private String className;
    @Getter
    @Setter
    private String jsonText;

    public JsonTextObject(String className, String jsonText) {
        this.className = className;
        this.jsonText = jsonText;
    }

    public JsonTextObject(JsonTextObject src) {
        this(src.className, src.jsonText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(className, jsonText);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("className", className)
                .add("jsonText", jsonText);
    }
}
