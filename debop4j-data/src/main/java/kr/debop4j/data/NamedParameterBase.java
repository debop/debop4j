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

package kr.debop4j.data;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;

import static kr.debop4j.core.Guard.shouldNotBeEmpty;

/**
 * SQL 실행 문의 인자 정보를 나타내는 {@link INamedParameter} 의 추상클래스입니다.
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19
 */
@Getter
@Setter
public abstract class NamedParameterBase extends ValueObjectBase implements INamedParameter {

    private static final long serialVersionUID = -5298525421726000937L;

    private String name;
    private Object value;

    protected NamedParameterBase(String name, Object value) {
        shouldNotBeEmpty(name, "name");

        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("value", value);
    }
}
