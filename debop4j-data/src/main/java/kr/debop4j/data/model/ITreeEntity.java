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
 * 트리 구조를 가지는 엔티티를 표현하는 인터페이스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 13.
 */
public interface ITreeEntity<T extends ITreeEntity<T>> extends IStatefulEntity {

    /** 현재 노드의 부모 노드를 반환합니다. null이면, 현 노드가 최상위 노드 (root node) 입니다. */
    T getParent();

    /** 부모 노드를 설정합니다. (null 을 설정하면, 최상위 노드(root note) 가 됩니다.) */
    void setParent(T parent);

    /** 자식 노드 집합 (Set) */
    Set<T> getChildren();

    /** 현재 노드의 트리 구조상의 위치를 나타냅니다. */
    TreeNodePosition getNodePosition();
}
