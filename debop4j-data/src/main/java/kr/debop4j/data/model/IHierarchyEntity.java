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

import java.util.Set;

/**
 * 계층형 자료 구조를 표현하며, 조상과 자손에 대한 컬렉션 속성을 가지는 인터페이스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 24
 */
public interface IHierarchyEntity<T extends IHierarchyEntity<T>> {

    /** 엔티티의 조상들 (트리 구조상의 특정 노드의 모든 부모 노드를 말한다) */
    Set<T> getAncestors();

    /** 엔티티의 자손들 (트리 구조상의 특정 노드의 모든 하위 노드를 말한다) */
    Set<T> getDescendents();
}
