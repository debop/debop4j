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

package kr.debop4j.search.hibernate.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;

import javax.persistence.Embeddable;

/**
 * 문서의 추가적인 특성들을 나타냅니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 4. 오후 6:33
 */
@Embeddable
@Getter
@Setter
public class DocumentAttr extends ValueObjectBase {

    private static final long serialVersionUID = -3403306607356587867L;

    protected DocumentAttr() {}

    /**
     * 생성자
     *
     * @param name  특성 명
     * @param value 특성 값
     */
    public DocumentAttr(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 특성 명
     */
    @Field(analyze = Analyze.NO)
    private String name;

    /**
     * 특성 값
     */
    @Field
    @Boost(1.2f)
    private String value;

    @Override
    public int hashCode() {
        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("value", value);
    }
}
