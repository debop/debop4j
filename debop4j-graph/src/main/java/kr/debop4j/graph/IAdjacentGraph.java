package kr.debop4j.graph;

import java.util.List;

/**
 * kr.debop4j.graph.IAdjacentGraph
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 5:37
 */
public interface IAdjacentGraph extends IGraph {

    /**
     * Graph 에 있는 Vertex 컬렉션
     */
    List<IVertex> getVertices();

    /**
     * 지정한 Vertex의 인접한 Vertex를 나타낸다.
     */
    List<IVertex> getAdjacentVertices(IVertex vertex);
}
