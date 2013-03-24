package kr.debop4j.graph;

import java.util.List;

/**
 * kr.debop4j.graph.IEdgeGraph
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 24. 오후 5:36
 */
public interface IEdgeGraph extends IGraph {

    List<IEdge> getEdges();
}
