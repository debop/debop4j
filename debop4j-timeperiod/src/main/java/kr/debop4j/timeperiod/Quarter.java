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
 * 분기 (Quarter)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오전 11:31
 */
public enum Quarter {
    First(1), Second(2), Third(3), Fourth(4);

    @Getter
    private final int value;

    Quarter(int value) {
        this.value = value;
    }

    public static Quarter valueOf(int quarter) {
        switch (quarter) {
            case 1:
                return First;
            case 2:
                return Second;
            case 3:
                return Third;
            case 4:
                return Fourth;
        }
        throw new IllegalArgumentException("Invalid quarter number. [1-4], quarter=" + quarter);
    }
}
