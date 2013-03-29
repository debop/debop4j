package kr.debop4j.graph;

import java.io.Serializable;

/**
 * kr.debop4j.graph.IEdge
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 14.
 */
public interface IEdge extends Serializable {

    IVertex getStart();

    IVertex getEnd();
}
