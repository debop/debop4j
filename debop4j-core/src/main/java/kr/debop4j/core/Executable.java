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

/**
 * kr.debop4j.core.Executable
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 16.
 */
public interface Executable {

    /**
     * 특정 코드를 수행합니다.
     */
    public void execute();

    /**
     * 설정한 타임아웃이 되었을 때 호출되는 메소드입니다.
     */
    public void timedOut();
}
