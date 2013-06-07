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
import lombok.Getter;

/**
 * 루씬 검색 시 범위로 검색할 때 사용할 파라미터.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 26. 오후 5:29
 */
public class SearchRangeParameter extends SearchParameter {
    private static final long serialVersionUID = -184375236713578263L;

    @Getter
    private final Object from;
    @Getter
    private final Object to;

    /**
     * Instantiates a new Search range parameter.
     *
     * @param name 파라미터 명
     * @param from 하한
     * @param to   상한
     */
    public SearchRangeParameter(String name, Object from, Object to) {
        super(name, from);
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(getName(), getQueryMethod(), from, to);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("from", from)
                .add("to", to);
    }
}
