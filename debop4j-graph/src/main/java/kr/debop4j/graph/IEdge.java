package kr.debop4j.graph;

import java.io.Serializable;

/**
 * kr.debop4j.graph.IEdge
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 14.
 */
public interface IEdge extends Serializable {

    IVertex getStart();

    IVertex getEnd();
}
