package com.kt.vital.domain.model.statistics;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 그래프(Measure)의 하나의 요소 - (x,y) 값을 표현한다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class SeriesItemDto implements Serializable {

    private static final long serialVersionUID = -4022356634960283305L;

    public SeriesItemDto(Comparable x, Comparable y) {
        this.x = x;
        this.y = y;
    }

    /**
     * X 축에 해당하는 값
     */
    private Comparable x;

    /**
     * Y 축에 해당하는 값
     */
    private Comparable y;

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }
}
