package kr.debop4j.data.model;

import java.sql.Timestamp;

/**
 * 최근 수정일자를 속성으로 가지고 있는 엔티티를 표현하는 인터페이스
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 21.
 */
public interface IUpdateTimestampedEntity {

    /**
     * 엔티티의 최근 갱신 일자를 반환합니다.
     */
    Timestamp getUpdateTimestamp();

    /**
     * 엔티티의 최근 갱신 일자를 수정합니다.
     */
    void updateUpdateTimestamp();
}
