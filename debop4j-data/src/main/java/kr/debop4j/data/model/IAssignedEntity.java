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

package kr.debop4j.data.model;

import java.io.Serializable;

/**
 * Assignable Id 를 가지는 엔티티의 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 27.
 */
public interface IAssignedEntity<TId extends Serializable> {

    /**
     * 엔티티의 Id 를 설정합니다.
     *
     * @param newId 새로운 Id 값
     */
    void setId(TId newId);
}
