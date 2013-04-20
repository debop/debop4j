package kr.debop4j.data.model;

import java.io.Serializable;

/**
 * 엔티티의 기본 인터페이스입니다.
 * Jpa@author sunghyouk.bae@gmail.com
 *
 * @since 12. 11. 19
 */
public interface IEntity<TId extends Serializable> extends Serializable {

    /**
     * 엔티티의 Identifier 입니다.
     *
     * @param <TId> Identifier의 수형
     * @return 엔티티의 Identifier
     */
    <TId> TId getId();
}
