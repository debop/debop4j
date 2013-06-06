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

package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;


/**
 * 메타 정보를 표현하는 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 19
 */
@Embeddable
@Getter
@Setter
public class SimpleMetaValue extends ValueObjectBase implements IMetaValue {

    public static final SimpleMetaValue Empty = new SimpleMetaValue("");

    private String value;
    private String label;
    private String description;
    private String exAttr;

    public SimpleMetaValue() {
        this("");
    }

    public SimpleMetaValue(Object value) {
        this.value = Guard.firstNotNull(value, "").toString();
    }

    public SimpleMetaValue(SimpleMetaValue metaValue) {
        if (metaValue != null) {
            this.value = metaValue.value;
            this.label = metaValue.label;
            this.description = metaValue.description;
            this.exAttr = metaValue.exAttr;
        }
    }


    @Override
    public int hashCode() {
        return HashTool.compute(value);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("value", value)
                .add("label", label)
                .add("description", description)
                .add("exAttr", exAttr);
    }

    private static final long serialVersionUID = -6675942606392780717L;
}
