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

package kr.debop4j.core;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;

import java.io.Serializable;

/**
 * 2개의 값을 가진 Tuple
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오전 11:03
 */
public class Pair<V1, V2> extends ValueObjectBase implements Serializable {

    private static final long serialVersionUID = 4213705024392671643L;

    /**
     * Create pair.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the pair
     */
    public static <V1, V2> Pair<V1, V2> create(V1 v1, V2 v2) {
        return new Pair<V1, V2>(v1, v2);
    }

    @Getter
    public final V1 v1;
    @Getter
    public final V2 v2;
    private final int hash;

    /**
     * Instantiates a new Pair.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     */
    public Pair(V1 v1, V2 v2) {
        this.v1 = v1;
        this.v2 = v2;
        hash = HashTool.compute(v1, v2);
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("v1", v1)
                .add("v2", v2);
    }
}
