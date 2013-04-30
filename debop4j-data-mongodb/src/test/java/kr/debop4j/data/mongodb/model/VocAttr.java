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

package kr.debop4j.data.mongodb.model;

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
 * kr.debop4j.search.hibernate.model.VocAttr
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 29. 오후 4:05
 */
@Embeddable
@Getter
@Setter
//@Analyzer(impl = StandardAnalyzer.class)
public class VocAttr extends ValueObjectBase {

    private static final long serialVersionUID = 5459929566638977310L;

    protected VocAttr() {}

    public VocAttr(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Field(analyze = Analyze.NO)
    private String name;

    @Field(analyze = Analyze.NO)
    @Boost(1.1f)
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
