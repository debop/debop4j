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
 * 검색 방향
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:02
 */
public enum SeekDirection {

    /** 미래로 (시간 값을 증가 시키는 방향) */
    Forward(1),

    /** 과거로 (시간 값을 감소 시키는 방향) */
    Backward(-1);

    @Getter private final int value;

    SeekDirection(int value) {
        this.value = value;
    }

    public static SeekDirection valueOf(int value) {
        switch (value) {
            case 1:
                return Forward;
            case -1:
                return Backward;
            default:
                throw new IllegalArgumentException("not supported value. value=" + value);
        }
    }

}
