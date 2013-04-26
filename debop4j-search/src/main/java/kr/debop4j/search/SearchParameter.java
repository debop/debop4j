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

package kr.debop4j.search;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.NamedParameterBase;
import lombok.Getter;

/**
 * 루씬 검색 시의 파라미터들
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 26. 오후 4:25
 */
public class SearchParameter extends NamedParameterBase {
    private static final long serialVersionUID = 5165941010754010563L;

    @Getter
    final QueryMethod queryMethod;

    public SearchParameter(String name, Object value) {
        this(name, value, QueryMethod.Term);
    }

    public SearchParameter(String name, Object value, QueryMethod queryMethod) {
        super(name, value);
        assert queryMethod != QueryMethod.Range : " SearchRangeParameter를 사용하세요.";
        this.queryMethod = queryMethod;
    }

    @Override
    public String getValue() {
        Object v = super.getValue();
        return (v == null) ? "" : v.toString();
    }

    @Override
    public int hashCode() {
        return HashTool.compute(getName(), queryMethod);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("queryMethod", queryMethod);
    }
}
