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

package kr.debop4j.timeperiod;

import lombok.Getter;

/**
 * 반기 (Halfyear)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:03
 */
public enum Halfyear {

    /**
     * 상반기
     */
    First(1),

    /**
     * 하반기
     */
    Second(2);

    @Getter private final int value;

    Halfyear(int value) {
        this.value = value;
    }

    public static Halfyear valueOf(int halfyear) {
        if (halfyear == 1)
            return First;
        else if (halfyear == 2)
            return Second;
        else
            throw new IllegalArgumentException("Halfyear 는 1,2 값만 가질 수 있습니다. halfyear=" + halfyear);
    }
}
