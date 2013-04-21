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

import kr.debop4j.core.IValueObject;

/**
 * 저장 상태 정보를 가지는 엔티티임을 나타내는 인터페이스입니다.
 * Jpa@author sunghyouk.bae@gmail.com
 *
 * @since 12. 11. 19
 */
public interface IStatefulEntity extends IValueObject {

    /**
     * 영구 저장소인 Repository 에 저장된 엔티티 (Persistent Object) 라면 true 를 반환하고, <br />
     * 메모리 상에서만 존재하는 엔티티(Transient Object) 라면 false를 반환한다.
     */
    boolean isPersisted();

    /**
     * 엔티티가 저장된 후 호출되는 함수
     */
    void onSave();

    /**
     * 엔티티가 저장된 후 호출되는 함수
     */
    void onPersist();

    /**
     * 엔티티가 영구 저장소에서 메모리로 로드된 후 호출되는 함수
     */
    void onLoad();
}
