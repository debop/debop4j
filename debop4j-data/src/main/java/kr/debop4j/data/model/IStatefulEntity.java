package kr.debop4j.data.model;

import kr.debop4j.core.IValueObject;

/**
 * 저장 상태 정보를 가지는 엔티티임을 나타내는 인터페이스입니다.
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
public interface IStatefulEntity extends IValueObject {

    /**
     * 영구 저장소인 Repository 에 저장된 엔티티 (Persistent Object) 라면 true 를 반환하고, <br />
     * 메모리 상에서만 존재하는 엔티티(Transient Object) 라면 false를 반환한다.
     */
    boolean isPersisted();

    /**
     * 엔티티가 저장된 후 호출되는 함수
     */
    void onSave();

    /**
     * 엔티티가 저장된 후 호출되는 함수
     */
    void onPersist();

    /**
     * 엔티티가 영구 저장소에서 메모리로 로드된 후 호출되는 함수
     */
    void onLoad();
}
