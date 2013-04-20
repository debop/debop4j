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
