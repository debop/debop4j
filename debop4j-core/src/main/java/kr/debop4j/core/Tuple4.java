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
 * 4개의 값을 가지는 Tuple
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오전 11:13
 */
public class Tuple4<V1, V2, V3, V4> extends ValueObjectBase implements Serializable {

    private static final long serialVersionUID = -1939638392275865110L;

    public static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> create(V1 v1, V2 v2, V3 v3, V4 v4) {
        return new Tuple4<V1, V2, V3, V4>(v1, v2, v3, v4);
    }

    @Getter public final V1 v1;
    @Getter public final V2 v2;
    @Getter public final V3 v3;
    @Getter public final V4 v4;

    private final int hash;

    public Tuple4(V1 v1, V2 v2, V3 v3, V4 v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;

        this.hash = HashTool.compute(v1, v2, v3, v4);
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("v1", v1)
                .add("v2", v2)
                .add("v3", v3)
                .add("v4", v4);
    }
}
