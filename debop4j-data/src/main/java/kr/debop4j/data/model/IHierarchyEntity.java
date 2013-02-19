package kr.debop4j.data.model;

import java.util.Set;

/**
 * 계층형 자료 구조를 표현하며, 조상과 자손에 대한 컬렉션 속성을 가지는 인터페이스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 24
 */
public interface IHierarchyEntity<T extends IHierarchyEntity<T>> {

    /**
     * 엔티티의 조상들 (트리 구조상의 특정 노드의 모든 부모 노드를 말한다)
     */
    Set<T> getAncestors();

    /**
     * 엔티티의 자손들 (트리 구조상의 특정 노드의 모든 하위 노드를 말한다)
     */
    Set<T> getDescendents();
}
